package com.hustunique.androidassistant.app;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;

import com.hustunique.androidassistant.service.MobileDataService;
import com.hustunique.androidassistant.util.LogUtil;
import com.hustunique.androidassistant.util.Util;

/**
 * Created by CoXier on 17-6-13.
 */

public class AssistantApp extends Application {
    private static final String TAG = "AssistantApp";

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d(TAG, "Application create");
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        LogUtil.d(TAG, "Mobile data: " + sharedPreferences.getLong("mobileData", -1));
        if (Util.isMobileType(this)) {
            Intent intent = new Intent(this, MobileDataService.class);
            startService(intent);
        }
    }
}
