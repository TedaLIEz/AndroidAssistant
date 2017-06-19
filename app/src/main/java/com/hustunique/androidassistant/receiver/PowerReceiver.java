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

package com.hustunique.androidassistant.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import com.hustunique.androidassistant.model.BatInfo;
import com.hustunique.androidassistant.util.LogUtil;

/**
 * Created by JianGuo on 6/12/17.
 * BroadcastReceiver used for monitoring battery status and power usage
 */
// TODO: 6/12/17 Use this receiver in service should be better or not
public class PowerReceiver extends BroadcastReceiver {

    private static final String TAG = "PowerReceiver";
    private float batteryPct = Float.MIN_NORMAL;
    private BatInfo mInfo = new BatInfo();
    public interface BatteryCallback {
        void onUpdated(BatInfo power);
    }


    private BatteryCallback mCallback;
    public PowerReceiver(BatteryCallback callback) {
        super();
        mCallback = callback;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharged = status == BatteryManager.BATTERY_STATUS_CHARGING;
            float batteryPct = level / (float) scale;
            mInfo.setPct((int) (batteryPct * 100));
            mInfo.setCharged(isCharged);
            LogUtil.d(TAG, "battery status" + mInfo);
        }

        mCallback.onUpdated(mInfo);
    }
}
