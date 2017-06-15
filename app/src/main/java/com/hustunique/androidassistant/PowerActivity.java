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

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.hustunique.androidassistant.receiver.PowerReceiver;
import com.hustunique.androidassistant.receiver.PowerReceiver.BatteryCallback;
import com.hustunique.androidassistant.manager.MyPowerManager;
import com.hustunique.androidassistant.util.LogUtil;

public class PowerActivity extends BaseActivity {

    private static final String TAG = "PowerActivity";
    private PowerReceiver mPowerReceiver;

    @BindView(R.id.power_tv)
    TextView mTvPow;
    private Unbinder mUnbinder;
    private MyPowerManager mPowerManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power);
        mUnbinder = ButterKnife.bind(this);
        mPowerManager = new MyPowerManager(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mPowerReceiver = new PowerReceiver(new BatteryCallback() {
            @Override
            public void onUpdated(int pct) {
                mTvPow.setText(String.valueOf(pct));
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(mPowerReceiver, intentFilter);
    }


    @OnClick(R.id.btn_power_mode)
    public void enablePowerSaveMode() {
        Toast.makeText(this, "TODO", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btn_save_power)
    public void savePower() {
        // TODO: 6/15/17 Kill background processes and change system brightness


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                Toast.makeText(this, R.string.grant_settings_access, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            if (Settings.System.canWrite(this)) {
                mPowerManager.savePower();
            } else {
                LogUtil.e(TAG, "can't change system brightness");
            }
        } else {
            mPowerManager.savePower();
        }



    }


    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mPowerReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}

