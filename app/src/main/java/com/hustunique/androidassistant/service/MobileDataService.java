package com.hustunique.androidassistant.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.widget.TextView;

import com.hustunique.androidassistant.R;
import com.hustunique.androidassistant.manager.MobileDataManager;
import com.hustunique.androidassistant.util.LogUtil;
import com.hustunique.androidassistant.util.Util;

import java.lang.ref.WeakReference;

public class MobileDataService extends Service {
    private static final String TAG = "MobileDataService";

    private Handler mHandler;
    private CountDataRunnable mCountDataRunnable;
    private MobileDataManager mMobileDataManager;

    private MobileDataBinder mBinder;
    private boolean shouldPostRunnable = false;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Util.isMobileType(context)) {
                LogUtil.d(TAG, "Network type is mobile");
                shouldPostRunnable = true;
                if (!mCountDataRunnable.isRunning)
                    mHandler.post(mCountDataRunnable);
            } else {
                LogUtil.d(TAG, "Mobile net is offline");
                // stop posting runnable
                shouldPostRunnable = false;
                mHandler.removeCallbacks(mCountDataRunnable);
                mMobileDataManager.saveMobileData();
                mMobileDataManager.saveAppInfoWithinDB();
            }
        }
    };

    @Override
    public void onCreate() {
        LogUtil.d(TAG, "create");
        mHandler = new Handler(getMainLooper());
        mMobileDataManager = MobileDataManager.getInstance(this);
        mCountDataRunnable = new CountDataRunnable();
        mBinder = new MobileDataBinder();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(mBroadcastReceiver, intentFilter);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        LogUtil.d(TAG, "destroy");
        unregisterReceiver(mBroadcastReceiver);
        mHandler.removeCallbacks(mCountDataRunnable);
        mMobileDataManager.saveMobileData();
        mMobileDataManager.saveAppInfoWithinDB();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.d(TAG, "bind");
        if (Util.isMobileType(this) && !mCountDataRunnable.isRunning) {
            mHandler.post(mCountDataRunnable);
        }
        return mBinder;
    }

    class CountDataRunnable implements Runnable {
        boolean isRunning = false;

        @Override
        public void run() {
            isRunning = true;
            LogUtil.d(TAG, "Mobile data bytes: " + mMobileDataManager.getTotalMobileData());
            String bytes = mBinder.bytesInStringFormat();
            if (mBinder.mainTextView != null && mBinder.mainTextView.get() != null) {
                mBinder.mainTextView.get().setText(getString(R.string.item_data_detail,bytes));
            }
            if (mBinder.dataUsageTextView != null && mBinder.dataUsageTextView.get() != null) {
                mBinder.dataUsageTextView.get().setText(getString(R.string.data_used,bytes));
            }
            mMobileDataManager.updateAppInfo();
            if (shouldPostRunnable)
                mHandler.postDelayed(this, 500);
            isRunning = false;
        }
    }

    public class MobileDataBinder extends Binder {
        private WeakReference<TextView> mainTextView;
        private WeakReference<TextView> dataUsageTextView;

        public void setMainTextView(WeakReference<TextView> mainTextView) {
            this.mainTextView = mainTextView;
        }

        public void setDataUsageTextView(WeakReference<TextView> dataUsageTextView) {
            this.dataUsageTextView = dataUsageTextView;
        }

        private String bytesInStringFormat() {
            long bytes = mMobileDataManager.getTotalMobileData();
            return Util.longToStringFormat(bytes);
        }


    }
}
