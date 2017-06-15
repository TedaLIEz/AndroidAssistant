package com.hustunique.androidassistant.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.hustunique.androidassistant.manager.MobileDataManager;
import com.hustunique.androidassistant.util.LogUtil;

public class MobileDataService extends Service {
    private static final String TAG = "MobileDataService";

    private Handler mHandler;
    private CountDataRunnable mCountDataRunnable;
    private MobileDataManager mMobileDataManager;

    @Override
    public void onCreate() {
        LogUtil.d(TAG,"MobileDataService create");
        mHandler = new Handler(getMainLooper());
        mMobileDataManager = MobileDataManager.getInstance(this);
        mCountDataRunnable = new CountDataRunnable();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d(TAG,"MobileDataService start");
        mHandler.postDelayed(mCountDataRunnable, 500);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        LogUtil.d(TAG,"MobileDataService destroy");
        mHandler.removeCallbacks(mCountDataRunnable);
        mMobileDataManager.saveMobileData();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class CountDataRunnable implements Runnable {
        @Override
        public void run() {
            LogUtil.d(TAG, "Mobile data bytes: " + mMobileDataManager.getTotalMobileData());
            mHandler.postDelayed(this,500);
        }
    }
}
