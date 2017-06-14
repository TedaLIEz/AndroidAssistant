package com.hustunique.androidassistant.manager;

import android.content.Context;
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
    private TrafficStats mTrafficStats;

    private MobileDataManager(Context context) {

    }

    public static MobileDataManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new MobileDataManager(context);
        }
        sInstance.getInternetPackageInfo();
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

    public double getTotalMobileData() {
        long bytes = TrafficStats.getMobileTxBytes() + TrafficStats.getMobileRxBytes();
        double mbFormat = (double) bytes / 1024 / 1024;
        LogUtil.i(TAG, "Mobile data bytes: " + mbFormat + " MB");
        return mbFormat;
    }
}
