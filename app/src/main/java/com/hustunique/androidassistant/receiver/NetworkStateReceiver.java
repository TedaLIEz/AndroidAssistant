package com.hustunique.androidassistant.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.hustunique.androidassistant.service.MobileDataService;
import com.hustunique.androidassistant.util.LogUtil;

public class NetworkStateReceiver extends BroadcastReceiver {
    private static final String TAG = "NetworkStateReceiver";

    private Intent mMobileSeviceIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isMobile = activeNetwork != null &&
                activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;
        if (isMobile){
            LogUtil.d(TAG, "now network type is mobile");
            LogUtil.i(TAG,"start service:------------------------------");
            mMobileSeviceIntent = new Intent(context, MobileDataService.class);
            context.startService(mMobileSeviceIntent);
        }else{
            // stop service
            mMobileSeviceIntent = new Intent(context, MobileDataService.class);
            context.stopService(mMobileSeviceIntent);
        }
    }
}
