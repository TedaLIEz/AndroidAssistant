package com.hustunique.androidassistant.model;

import android.content.pm.PackageInfo;

/**
 * Created by CoXier on 17-6-13.
 */

public class PackageInfoWrapper {
    private PackageInfo mPackageInfo;

    public PackageInfoWrapper(PackageInfo packageInfo) {
        mPackageInfo = packageInfo;
    }

    public PackageInfo getPackageInfo() {
        return mPackageInfo;
    }

    public void setPackageInfo(PackageInfo packageInfo) {
        mPackageInfo = packageInfo;
    }
}
