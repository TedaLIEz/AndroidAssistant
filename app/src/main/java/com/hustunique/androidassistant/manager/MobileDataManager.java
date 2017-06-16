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
    private long mTempBytes;

    private MobileDataManager(Context context) {
        sPM = context.getPackageManager();
        mSharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        mBytes = retrieveMobileData();
        retrieveAppInfo();
    }

    private void detectNewAppInfo() {
        List<PackageInfo> packageInfoList = sPM.getInstalledPackages(PackageManager.GET_PERMISSIONS);
        for (PackageInfo packageInfo : packageInfoList) {
            String[] permissions = packageInfo.requestedPermissions;
            if (permissions == null) continue;
            for (String permission : permissions) {
                if (permission.equals(sInternetPermission)) {
                    AppInfo appInfo = new AppInfo();
                    appInfo.setPackageName(packageInfo.packageName);
                    appInfo.setAppName((String) sPM.getApplicationLabel(packageInfo.applicationInfo));
                    appInfo.setTimestamp(Util.constructTimestamp());
                    appInfo.setUid(packageInfo.applicationInfo.uid);
                    if (!mAppInfos.contains(appInfo)){
                        mAppInfos.add(appInfo);
                    }
                    break;
                }
            }
        }
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
                    appInfo.setAppName((String) sPM.getApplicationLabel(packageInfo.applicationInfo));
                    appInfo.setTimestamp(Util.constructTimestamp());
                    appInfo.setUid(packageInfo.applicationInfo.uid);
                    appInfos.add(appInfo);
                    break;
                }
            }
        }
        return appInfos;
    }

    public long getTotalMobileData() {
        long bytes = TrafficStats.getMobileTxBytes() + TrafficStats.getMobileRxBytes();
        if(bytes == 0) return mBytes;
        if (mBytes < bytes) {
            mBytes = bytes;
        } else {
            mBytes += bytes - mTempBytes;
            mTempBytes = bytes;
        }
        return mBytes;
    }

    public List<AppInfo> getAppInfos() {
        return mAppInfos;
    }

    public void saveMobileData() {
        mSharedPreferences.edit().putLong("mobileData", mBytes).apply();
    }

    private long retrieveMobileData() {
        return mSharedPreferences.getLong("mobileData", 0);
    }

    private void retrieveAppInfo() {
        String timestamp = Util.constructTimestamp();
        mAppInfos = SQLite.select().from(AppInfo.class).where(AppInfo_Table.timestamp.eq(timestamp)).queryList();
        if (mAppInfos.size() == 0) {
            mAppInfos = getAppInfoWithPermission();
        }else{
            detectNewAppInfo();
        }
    }

    public void updateAppInfo() {
        AppInfo tmp = null;
        for (AppInfo appInfo : mAppInfos) {
            if (!isAppInstalled(appInfo.getPackageName())) {
                removeAppInfo(appInfo);
                tmp = appInfo;
                continue;
            }
            int uid = appInfo.getUid();
            long bytes = Util.getTotalBytesManual(uid);
            long currentBytes = bytes;
            if (appInfo.getTotalUidBytes() == 0){
                appInfo.setTotalUidBytes(bytes);
                continue;
            }else{
                bytes = bytes - appInfo.getTotalUidBytes() + appInfo.getMobileBytes();
                appInfo.setTotalUidBytes(currentBytes);
            }
            appInfo.setMobileBytes(bytes);
        }

        if (tmp != null){
            mAppInfos.remove(tmp);
        }
    }

    private void removeAppInfo(AppInfo appInfo) {
        final ModelAdapter<AppInfo> adapter = FlowManager.getModelAdapter(AppInfo.class);
        adapter.delete(appInfo);
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
