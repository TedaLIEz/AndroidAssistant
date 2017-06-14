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

package com.hustunique.androidassistant;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import com.hustunique.androidassistant.db.BlackList;
import com.hustunique.androidassistant.receiver.PhoneCallReceiver;
import com.hustunique.androidassistant.receiver.PowerReceiver;
import com.hustunique.androidassistant.receiver.PowerReceiver.BatteryCallback;
import com.hustunique.androidassistant.service.MyPowerManager;
import com.hustunique.androidassistant.util.LogUtil;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private PowerReceiver mPowerReceiver;
    private PhoneCallReceiver mCallReceiver;
    private MyPowerManager mPowerManager;
    private final int MY_PERMISSIONS_REQUEST_PHONE_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPowerManager = new MyPowerManager(this);

        mPowerReceiver = new PowerReceiver(new BatteryCallback() {
            @Override
            public void onUpdated(int pct) {
                LogUtil.d(TAG, "battery pct: " + pct + "%");
            }
        });

        mCallReceiver = new PhoneCallReceiver();

        // FIXME: request permission
        LogUtil.d("MainActivity", "code: " + ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE));
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            LogUtil.d("MainActivity", "request call phone");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_PHONE_CALL);
        }

        // FIXME: test BlackList
        BlackList blackList = new BlackList(getApplicationContext());
        blackList.addNewBlackNumber("15171508722");
        blackList.addNewBlackNumber("17777777777");
        blackList.addNewBlackNumber("1515");
        blackList.getAllBlackNumbers();
        blackList.deleteNumberFromBlackList("17777777777");
        blackList.getAllBlackNumbers();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_PHONE_CALL: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LogUtil.d("MainActivity", "call phone granted!");
                } else {
                    LogUtil.d("MainActivity", "call phone denied!!!");
                }
                return;
            }
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(mPowerReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mPowerReceiver);
        unregisterReceiver(mCallReceiver);
    }
}
