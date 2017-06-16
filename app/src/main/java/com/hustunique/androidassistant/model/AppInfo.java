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

package com.hustunique.androidassistant.model;

import com.hustunique.androidassistant.db.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

/**
 * Created by JianGuo on 6/13/17.
 */
@Table(database = AppDatabase.class)
public class AppInfo {
    @PrimaryKey
    private String packageName;
    @PrimaryKey
    private String timestamp;
    @Column
    private long mobileBytes;
    @Column
    private int uid;

    private float priority;

    private long totalUidBytes;

    public AppInfo(){

    }

    public AppInfo(String packageName, int uid, String timestamp, long mobileBytes) {
        this.packageName = packageName;
        this.uid = uid;
        this.timestamp = timestamp;
        this.mobileBytes = mobileBytes;
    }

    public AppInfo(String packageName, float priority) {
        this.packageName = packageName;
        this.priority = priority;
    }

    public long getTotalUidBytes() {
        return totalUidBytes;
    }

    public void setTotalUidBytes(long totalUidBytes) {
        this.totalUidBytes = totalUidBytes;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public long getMobileBytes() {
        return mobileBytes;
    }

    public void setMobileBytes(long mobileBytes) {
        this.mobileBytes = mobileBytes;
    }

    public float getPriority() {
        return priority;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AppInfo appInfo = (AppInfo) o;

        if (Float.compare(appInfo.priority, priority) != 0) {
            return false;
        }
        return packageName != null ? packageName.equals(appInfo.packageName)
            : appInfo.packageName == null;

    }

    @Override
    public int hashCode() {
        int result = packageName != null ? packageName.hashCode() : 0;
        result = 31 * result + (priority != +0.0f ? Float.floatToIntBits(priority) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
            "packageName='" + packageName + '\'' +
            ", priority=" + priority +
            '}';
    }
}
