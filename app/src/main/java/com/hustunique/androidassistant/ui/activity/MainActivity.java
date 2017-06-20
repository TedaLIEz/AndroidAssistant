/*
 * Copyright 2017 TedaLIEz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hustunique.androidassistant.ui.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.TextView;

import com.hustunique.androidassistant.R;
import com.hustunique.androidassistant.db.AutoBlockListManager;
import com.hustunique.androidassistant.db.BlackList;
import com.hustunique.androidassistant.db.BlockedCallSaver;
import com.hustunique.androidassistant.db.BlockedSMSSaver;
import com.hustunique.androidassistant.manager.MyPowerManager;
import com.hustunique.androidassistant.manager.PrefManager;
import com.hustunique.androidassistant.model.AutoBlockListModel;
import com.hustunique.androidassistant.model.BatInfo;
import com.hustunique.androidassistant.receiver.PowerReceiver;
import com.hustunique.androidassistant.receiver.PowerReceiver.BatteryCallback;
import com.hustunique.androidassistant.service.MobileDataService;
import com.hustunique.androidassistant.util.LogUtil;
import com.hustunique.androidassistant.util.Util;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    private final int MY_PERMISSIONS_REQUEST_PHONE_CALL = 1;

    @BindView(R.id.main_tv_mem)
    TextView mTvMem;

    @BindView(R.id.power_detail)
    TextView mTvPowDetail;

    @BindView(R.id.main_btn_clean)
    Button mBtnClean;

    @BindView(R.id.data_detail)
    TextView mTVMobile;

    @BindView(R.id.block_detail)
    TextView mTvBlockDetail;


    private Unbinder mUnbinder;
    private PowerReceiver mPowerReceiver;

    private MobileDataService.MobileDataBinder mMobileBinder;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMobileBinder = (MobileDataService.MobileDataBinder) service;
            mMobileBinder.setMainTextView(new WeakReference<>(mTVMobile));
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);

        mPowerReceiver = new PowerReceiver(new BatteryCallback() {
            @Override
            public void onUpdated(BatInfo info) {
                mTvPowDetail.setText(info.isCharged() ?
                    getString(R.string.charging) :
                    getString(R.string.item_power_detail, info.getPct()));
            }
        });

        // bind service
        Intent intent = new Intent(this,MobileDataService.class);
        bindService(intent,mServiceConnection,BIND_AUTO_CREATE);
        // set mobile data
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(),MODE_PRIVATE);
        long mobileDataBytes = sharedPreferences.getLong("mobileData", 0);
        mTVMobile.setText(getString(R.string.item_data_detail,Util.longToStringFormat(mobileDataBytes)));



        requestPermission();

        // FIXME: test BlackList
        BlackList blackList = new BlackList();
        blackList.addNewBlackNumber("15171508722");
        blackList.addNewBlackNumber("17777777777");
        blackList.addNewBlackNumber("1515");
        blackList.getAllBlackNumbers();
        blackList.deleteNumberFromBlackList("17777777777");
        blackList.getAllBlackNumbers();

        //FIXME: test BlockedSMS
        BlockedSMSSaver smsSaver = new BlockedSMSSaver();
        smsSaver.addBlockedSMS("15171508722", "test", false);
        smsSaver.getAllBlockedSMS();

        BlockedCallSaver callSaver = new BlockedCallSaver();
        callSaver.getAllBlockedCall();

        AutoBlockListManager autoBlock = new AutoBlockListManager();
        List<AutoBlockListModel> t = new ArrayList<AutoBlockListModel>();
        t.add(new AutoBlockListModel("13912345678"));
        t.add(new AutoBlockListModel("13987654321"));
        autoBlock.updateList(t);
        autoBlock.getAllAutoBlocks();
        t.add(new AutoBlockListModel("123"));
        autoBlock.updateList(t);
        autoBlock.getAllAutoBlocks();



    }

    private void requestPermission() {
        if (Util.isM()) {
            LogUtil.d(TAG, "code: " + ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE));
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                LogUtil.d(TAG, "request call phone&sms");
                ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE,
                        Manifest.permission.RECEIVE_SMS},
                    MY_PERMISSIONS_REQUEST_PHONE_CALL);
            }
            else if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {
                LogUtil.d(TAG, "request sms permission");
                ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECEIVE_SMS},
                    MY_PERMISSIONS_REQUEST_PHONE_CALL);
            }
            else {
                LogUtil.d(TAG, "request call permission");
                ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_PHONE_CALL);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_PHONE_CALL: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LogUtil.d(TAG, "call phone granted!");
                } else {
                    LogUtil.d(TAG, "call phone denied!!!");
                }
            }
        }
    }

    @OnClick(R.id.main_btn_clean)
    public void onCleanBtnClicked() {
        new MyPowerManager(this).cleanBackground();
        mTvMem.setText(String.valueOf(Util.getRemainMemory(this)));
    }

    @OnClick(R.id.power_mang)
    public void onPowerMangClicked() {
        Intent intent = new Intent(this, PowerActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.data_mang)
    public void onDataMangClicked() {
        Intent intent = new Intent(this,DataUsageActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.block_mang)
    public void onBlockMangClicked() {
        Intent intent = new Intent(this, BlockActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();

        mTvMem.setText(String.valueOf(Util.getRemainMemory(this)));
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mPowerReceiver, intentFilter);

        boolean block_enable = PrefManager.getInstance()
                .getDefaultPreferences(this)
                .getBoolean("BlockEnable", false);
        mTvBlockDetail.setText(block_enable?getString(R.string.item_block_detail_enable):
                getString(R.string.item_block_detail_disable));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mPowerReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mUnbinder.unbind();
        unbindService(mServiceConnection);
    }
}
