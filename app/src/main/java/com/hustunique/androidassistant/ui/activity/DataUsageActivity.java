package com.hustunique.androidassistant.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Button;
import android.widget.TextView;

import com.hustunique.androidassistant.R;
import com.hustunique.androidassistant.manager.MobileDataManager;
import com.hustunique.androidassistant.model.AppInfo;
import com.hustunique.androidassistant.service.MobileDataService;
import com.hustunique.androidassistant.ui.widget.PieChart;
import com.hustunique.androidassistant.ui.widget.PieData;
import com.hustunique.androidassistant.util.LogUtil;
import com.hustunique.androidassistant.util.Util;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DataUsageActivity extends BaseActivity {
    private static final String TAG = "DataUsageActivity";
    @BindView(R.id.tv_data_used)
    TextView mTvDataUsed;
    @BindView(R.id.btn_data_setting)
    Button mBtnDataSetting;

    private ArrayList<PieData> mPieDatas = new ArrayList<>();
    // 颜色表
    private int[] mColors = {0xFFE32636, 0xFFFF8C69, 0xFF808080,
            0xFFE6B800, 0xFF7CFC00};

    private Unbinder mUnbinder;
    private MobileDataService.MobileDataBinder mMobileBinder;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMobileBinder = (MobileDataService.MobileDataBinder) service;
            mMobileBinder.setDataUsageTextView(new WeakReference<>(mTvDataUsed));
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_usage);
        mUnbinder = ButterKnife.bind(this);
        setupToolbar(R.string.item_data);
        // bind service
        Intent intent = new Intent(this, MobileDataService.class);
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
        // set mobile data
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        long mobileDataBytes = sharedPreferences.getLong("mobileData", 0);
        mTvDataUsed.setText(getString(R.string.data_used, Util.longToStringFormat(mobileDataBytes)));
        initData();
        PieChart pieChart = (PieChart) findViewById(R.id.pieChart);
        pieChart.setAnimatorDuration(2000);
        pieChart.setPieData(mPieDatas);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        unbindService(mServiceConnection);
    }

    private void initData() {
        List<AppInfo> appInfos = MobileDataManager.getInstance(this).getAppInfos();
        Collections.sort(appInfos, new Comparator<AppInfo>() {
            @Override
            public int compare(AppInfo o1, AppInfo o2) {
                if (o1.getMobileBytes() < o2.getMobileBytes()) return 1;
                else if (o1.getMobileBytes() > o2.getMobileBytes()) return -1;
                return 0;
            }
        });
        for (int i = 0; i < 5; i++) {
            AppInfo appInfo = appInfos.get(i);
            LogUtil.d(TAG,appInfo.getAppName()+":"+appInfo.getMobileBytes());
            PieData pieData = new PieData();
            pieData.setName(appInfo.getAppName());
            pieData.setValue(appInfo.getMobileBytes());
            pieData.setColor(mColors[i]);
            mPieDatas.add(pieData);
        }
    }

}
