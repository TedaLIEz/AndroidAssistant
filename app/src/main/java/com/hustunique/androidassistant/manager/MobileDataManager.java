package com.hustunique.androidassistant.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;

import com.hustunique.androidassistant.db.AppDatabase;
import com.hustunique.androidassistant.model.AppInfo;
import com.hustunique.androidassistant.model.AppInfo_Table;
import com.hustunique.androidassistant.util.LogUtil;
import com.hustunique.androidassistant.util.Util;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CoXier on 17-6-13.
 */

public class MobileDataManager {
    private static final String TAG = "MobileDataManager";

    private static PackageManager sPM;
    private final static String sInternetPermission = "android.permission.INTERNET";
    private static MobileDataManager sInstance;
    private SharedPreferences mSharedPreferences;
    private List<AppInfo> mAppInfos;
    //  record the number of mobile data
    private long mBytes;

    private MobileDataManager(Context context) {
        sPM = context.getPackageManager();
        mSharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        mBytes = retrieveMobileData();
        mAppInfos = retrieveAppInfo();
    }

    public static MobileDataManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new MobileDataManager(context);
        }
        return sInstance;
    }

    private List<AppInfo> getAppInfoWithPermission() {
        List<AppInfo> appInfos = new ArrayList<>();
        List<PackageInfo> packageInfoList = sPM.getInstalledPackages(PackageManager.GET_PERMISSIONS);
        for (PackageInfo packageInfo : packageInfoList) {
            String[] permissions = packageInfo.requestedPermissions;
            if (permissions == null) continue;
            for (String permission : permissions) {
                if (permission.equals(sInternetPermission)) {
                    LogUtil.d(TAG, packageInfo.packageName);
                    AppInfo appInfo = new AppInfo();
                    appInfo.setPackageName(packageInfo.packageName);
                    appInfo.setTimestamp(Util.constructTimestamp());
                    appInfos.add(appInfo);
                    break;
                }
            }
        }
        return appInfos;
    }

    public long getTotalMobileData() {
        long bytes = TrafficStats.getMobileTxBytes() + TrafficStats.getMobileRxBytes();
        if (mBytes < bytes) {
            mBytes = bytes;
        } else {
            mBytes += bytes;
        }
        return mBytes;
    }

    public void saveMobileData() {
        mSharedPreferences.edit().putLong("mobileData", mBytes).apply();
    }

    private long retrieveMobileData() {
        return mSharedPreferences.getLong("mobileData", 0);
    }

    private List<AppInfo> retrieveAppInfo() {
        String timestamp = Util.constructTimestamp();
        List<AppInfo> appInfos = SQLite.select().from(AppInfo.class).where(AppInfo_Table.timestamp.eq(timestamp)).queryList();
        if (appInfos.size() == 0) {
            appInfos = getAppInfoWithPermission();
        }
        return appInfos;
    }

    public void updateAppInfo() {
        for (AppInfo appInfo : mAppInfos) {
            int uid = appInfo.getUid();
            long bytes = TrafficStats.getUidTxBytes(uid) + TrafficStats.getUidRxBytes(uid);
            bytes += appInfo.getMobileBytes();
            LogUtil.d(TAG,appInfo.getPackageName() + ": " + bytes);
            appInfo.setMobileBytes(bytes);
        }
    }

    public void saveAppInfoWithinDB() {
        final ModelAdapter<AppInfo> adapter = FlowManager.getModelAdapter(AppInfo.class);
        FlowManager.getDatabase(AppDatabase.class).executeTransaction(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                for (AppInfo appInfo:mAppInfos){
                    adapter.save(appInfo);
                }
            }
        });
    }

    private boolean isAppInstalled(String packageName) {
        try {
            sPM.getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
