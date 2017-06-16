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

package com.hustunique.androidassistant.manager;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build.VERSION_CODES;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.widget.Toast;
import com.hustunique.androidassistant.BuildConfig;
import com.hustunique.androidassistant.R;
import com.hustunique.androidassistant.model.AppInfo;
import com.hustunique.androidassistant.util.LogUtil;
import com.hustunique.androidassistant.util.Util;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by JianGuo on 6/13/17.
 * Management of power usage
 */

public class MyPowerManager {

    private static final String TAG = "MyPowerManager";
    private Context mContext;
    private static final int SAVE_POWER_BRIGHTNESS = 51;
    private static final int POWER_SAVE_MODE_BRIGHTNESS = 1;
    private int mBrightness = -1;
    private boolean mSaveMode = false;

    public MyPowerManager(Context context) {
        mContext = context;
    }


    private List<AppInfo> getRecentUsedApps() {
        List<AppInfo> rst;
        if (Util.isLollipop()) {
            rst = getRecentUsedApps21Internal();
        } else {
            rst = getRecentUsedAppsInternal();
        }
        dump(rst);
        return rst;
    }


    @RequiresApi(api = VERSION_CODES.LOLLIPOP)
    private boolean checkUsageAccess() {
        AppOpsManager appOpsManager = (AppOpsManager) mContext.getSystemService(
            Context.APP_OPS_SERVICE);
        final int mode = appOpsManager
            .checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(),
                mContext.getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    @NonNull
    private List<AppInfo> getRecentUsedAppsInternal() {
        List<AppInfo> rst = new ArrayList<>();
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> infos = am.getRunningAppProcesses();
        Class<RunningAppProcessInfo> clz = RunningAppProcessInfo.class;
        try {
            Field flagField = clz.getField("flags");
            for (RunningAppProcessInfo info : infos) {
                if (info.pid != 0) {
                    String[] pkgList = info.pkgList;
                    int flags = flagField.getInt(info);
                    if (flags == 0) {
                        // no last time used
                        // we use importance here instead
                        float importance = info.importance;
                        if (importance >= RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                            //  importance over VISIBLE should be foreground
                            for (String p : pkgList) {
                                if (!p.equals(mContext.getPackageName())) {
                                    AppInfo appInfo = new AppInfo(p, importance);
                                    rst.add(appInfo);
                                }

                            }
                        }
                    }

                }
            }
        } catch (NoSuchFieldException e) {
            LogUtil.wtf(TAG, e);
        } catch (IllegalAccessException e) {
            LogUtil.wtf(TAG, e);
        }

        Collections.sort(rst, new Comparator<AppInfo>() {
            @Override
            public int compare(AppInfo o1, AppInfo o2) {
                return -Float.compare(o1.getPriority(), o2.getPriority());
            }
        });

        return rst;
    }

    @NonNull
    @RequiresApi(api = VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("ResourceType")
    // As usagestats is explicitly declared until api 22 but exists in api 21, we use it manually
    private List<AppInfo> getRecentUsedApps21Internal() {
        List<AppInfo> rst = new ArrayList<>();
        UsageStatsManager mUsageStatsManager = (UsageStatsManager) mContext.getSystemService(
            "usagestats");
        long time = java.lang.System.currentTimeMillis();
        // We get usage stats for the last 5 mins
        List<UsageStats> stats = mUsageStatsManager
            .queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 300, time);
        if (stats != null) {
            for (UsageStats stat : stats) {
                if (!isSystemApp(stat.getPackageName()) && !stat.getPackageName()
                    .equals(mContext.getPackageName())) {
                    float p = stat.getTotalTimeInForeground() == 0 ? Float.MAX_VALUE :
                        stat.getLastTimeUsed() / stat.getTotalTimeInForeground();
                    AppInfo appInfo = new AppInfo(stat.getPackageName(), p);
                    rst.add(appInfo);
                }
            }
            // Sort the stats by the priority
            // priority = lastTimeUsed / totalTimeInForeground
            Collections.sort(rst, new Comparator<AppInfo>() {
                @Override
                public int compare(AppInfo o1, AppInfo o2) {
                    return -Float.compare(o1.getPriority(), o2.getPriority());
                }
            });
            return rst;
        } else {
            Toast.makeText(mContext, mContext.getString(R.string.get_usage_fail), Toast.LENGTH_LONG)
                .show();
            LogUtil.e(TAG, "get recent used apps failed");
            return rst;
        }
    }


    public boolean savePower() {
        if (grantAccess()) {
            changeBrightnessInternal(SAVE_POWER_BRIGHTNESS);
            killProcesses();
            Toast.makeText(mContext, R.string.save_power_success, Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public void powerSaveMode() {
        if (grantAccess()) {
            mBrightness =
                android.provider.Settings.System.getInt(mContext.getContentResolver(),
                    android.provider.Settings.System.SCREEN_BRIGHTNESS, -1);
            changeBrightnessInternal(POWER_SAVE_MODE_BRIGHTNESS);
            killProcesses();
            Toast.makeText(mContext, R.string.enter_power_save_mode, Toast.LENGTH_SHORT).show();
            mSaveMode = true;
        }
    }

    public boolean isPowerSaveMode() {
        return mSaveMode;
    }


    private boolean grantAccess() {
        return grantDataAccess() && grantSettingsAccess();
    }

    private boolean grantSettingsAccess() {
        if (Util.isM()) {
            if (!checkSettingsAccess()) {
                Toast.makeText(mContext, R.string.grant_settings_access, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + mContext.getPackageName()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                LogUtil.e(TAG, "grant settings access failed");
                return false;
            }
        }
        return true;
    }

    private boolean grantDataAccess() {
        if (Util.isLollipop()) {
            if (!checkUsageAccess()) {
                Toast.makeText(mContext, mContext.getString(R.string.grant_usage_access),
                    Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                return false;
            }
        }
        return true;
    }

    public void cleanBackground() {
        if (grantDataAccess()) {
            killProcesses();
            Toast.makeText(mContext, R.string.clr_background_success, Toast.LENGTH_SHORT).show();
        }
    }


    @RequiresApi(api = VERSION_CODES.M)
    private boolean checkSettingsAccess() {
        return Settings.System.canWrite(mContext);
    }


    private void changeBrightnessInternal(int brightness) {
        Settings.System
            .putInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);  //this will set the manual mode (set the automatic mode off)
        Settings.System.putInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS,
            brightness);
    }

    private boolean isSystemApp(String packageName) {
        final PackageManager pm = mContext.getPackageManager();
        try {
            ApplicationInfo info = pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            return (info.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
        } catch (NameNotFoundException e) {
            LogUtil.wtf(TAG, e);
            return false;
        }
    }

    private void killProcesses() {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        for (AppInfo i : getRecentUsedApps()) {
            am.killBackgroundProcesses(i.getPackageName());
        }
    }

    private void dump(List<AppInfo> list) {
        if (BuildConfig.DEBUG) {
            for (AppInfo u : list) {
                LogUtil.d(TAG, "usage: " + u.getPackageName()
                    + " priority " + u.getPriority());
            }
        }
    }


    public void restoreSaveMode() {
        changeBrightnessInternal(mBrightness);
        mBrightness = -1;
        mSaveMode = false;
    }
}
