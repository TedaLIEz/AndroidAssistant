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

package com.hustunique.androidassistant.service;

import android.app.ActivityManager;
import android.app.ActivityManager.RecentTaskInfo;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build.VERSION_CODES;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import com.hustunique.androidassistant.model.AppInfo;
import com.hustunique.androidassistant.util.LogUtil;
import com.hustunique.androidassistant.util.Util;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Created by JianGuo on 6/13/17.
 * Management of power usage
 */

public class MyPowerManager {

    private static final String TAG = "MyPowerManager";
    private Context mContext;

    public MyPowerManager(Context context) {
        mContext = context;
    }

    /**
     *
     * Return list of recently used user applications
     * we define the least recently used as :
     * <p>
     *     1) longest  last used timestamp
     *     2) shortest period in foreground
     * </p>
     * @return list of {@link AppInfo}
     */
    public List<AppInfo> getRecentUsedApps() {
        List<AppInfo> rst;
        if (Util.isLollipop()) {
            if (!checkUsageAccess()) {
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                mContext.startActivity(intent);
            }
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

    private List<AppInfo> getRecentUsedAppsInternal() {
        List<AppInfo> rst = new ArrayList<>();
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        // FIXME: 6/13/17 Is Integer.MAX_VALUE Good here
        List<RecentTaskInfo> infos = am
            .getRecentTasks(Integer.MAX_VALUE, ActivityManager.RECENT_WITH_EXCLUDED);
        // TODO: 6/13/17 Filter to this info list
        Class<RecentTaskInfo> clz = RecentTaskInfo.class;
        try {
            Field firstActiveTimeField = clz.getDeclaredField("firstActiveTime");
            Field lastActiveTimeField = clz.getDeclaredField("lastActiveTime");
            for (RecentTaskInfo info : infos) {
                if (info.id != -1) {
                    int firstActiveTime = (int) firstActiveTimeField.get(info);
                    int lastActiveTime = (int) lastActiveTimeField.get(info);
                    String packageName = info.origActivity.getPackageName();
                    float p = 1 - firstActiveTime / lastActiveTime;
                    AppInfo appInfo = new AppInfo(packageName, p);
                    rst.add(appInfo);
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
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
    private List<AppInfo> getRecentUsedApps21Internal() {
        List<AppInfo> rst = new ArrayList<>();
        UsageStatsManager mUsageStatsManager = (UsageStatsManager) mContext.getSystemService(
            "usagestats");
        long time = System.currentTimeMillis();
        // We get usage stats for the last 30 seconds
        List<UsageStats> stats = mUsageStatsManager
            .queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 30, time);

        if (stats != null) {
            List<ApplicationInfo> infos = getUserInstalledApplications();
            for (UsageStats stat : stats) {
                if (stat.getPackageName().equals(mContext.getPackageName())) {
                    // skip this package
                    continue;
                }
                for (ApplicationInfo info : infos) {
                    if (stat.getPackageName().equals(info.packageName)) {
                        float p = stat.getTotalTimeInForeground() == 0 ? Float.MAX_VALUE :
                            stat.getLastTimeUsed() / stat.getTotalTimeInForeground();
                        AppInfo appInfo = new AppInfo(stat.getPackageName(), p);
                        rst.add(appInfo);
                        break;
                    }
                }
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
    }



    private List<ApplicationInfo> getUserInstalledApplications() {
        // Get installed applications
        final PackageManager packageManager = mContext.getPackageManager();
        List<ApplicationInfo> installedApplications =
            packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        // Remove system apps
        Iterator<ApplicationInfo> it = installedApplications.iterator();
        while (it.hasNext()) {
            ApplicationInfo appInfo = it.next();
            if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                it.remove();
            }
        }

        // Return installed applications
        return installedApplications;
    }


    private void dump(List<AppInfo> list) {
        for (AppInfo u : list) {
            LogUtil.d(TAG, "usage: " + u.getPackageName()
                + " priority " + u.getPriority());
        }
    }


}
