package com.hustunique.androidassistant.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;

import com.hustunique.androidassistant.model.PackageInfoWrapper;
import com.hustunique.androidassistant.util.LogUtil;

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
    //  record the number of mobile data
    private long mBytes;

    private MobileDataManager(Context context) {
        sPM = context.getPackageManager();
        mSharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        mBytes = retrieveMobileData();
    }

    public static MobileDataManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new MobileDataManager(context);
        }
        return sInstance;
    }

    private List<PackageInfoWrapper> getInternetPackageInfo() {
        List<PackageInfoWrapper> wrapperList = new ArrayList<>();
        List<PackageInfo> packageInfoList = sPM.getInstalledPackages(PackageManager.GET_PERMISSIONS);
        for (PackageInfo packageInfo : packageInfoList) {
            LogUtil.d(TAG, packageInfo.toString());
            String[] permissions = packageInfo.requestedPermissions;
            if (permissions == null) continue;
            for (String permission : permissions) {
                if (permission.equals(sInternetPermission)) {
                    LogUtil.d(TAG, packageInfo.packageName);
                    PackageInfoWrapper wrapper = new PackageInfoWrapper(packageInfo);
                    wrapperList.add(wrapper);
                    break;
                }
            }
        }
        return wrapperList;
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
}
