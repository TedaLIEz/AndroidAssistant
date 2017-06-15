package com.hustunique.androidassistant.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hustunique.androidassistant.service.MobileDataService;
import com.hustunique.androidassistant.util.LogUtil;
import com.hustunique.androidassistant.util.Util;

public class NetworkStateReceiver extends BroadcastReceiver {
    private static final String TAG = "NetworkStateReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent mobileServiceIntent;
        if (Util.isMobileType(context)){
            LogUtil.d(TAG, "Network type is mobile");
            mobileServiceIntent = new Intent(context, MobileDataService.class);
            context.startService(mobileServiceIntent);
        }else{
            // stop service
            mobileServiceIntent = new Intent(context, MobileDataService.class);
            context.stopService(mobileServiceIntent);
        }
    }
}
