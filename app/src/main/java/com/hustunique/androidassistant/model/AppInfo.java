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

/**
 * Created by JianGuo on 6/13/17.
 */

public class AppInfo {
    private String mPackageName;

    private float mPriority;

    public AppInfo(String packageName, float priority) {
        mPackageName = packageName;
        mPriority = priority;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public float getPriority() {
        return mPriority;
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

        if (Float.compare(appInfo.mPriority, mPriority) != 0) {
            return false;
        }
        return mPackageName != null ? mPackageName.equals(appInfo.mPackageName)
            : appInfo.mPackageName == null;

    }

    @Override
    public int hashCode() {
        int result = mPackageName != null ? mPackageName.hashCode() : 0;
        result = 31 * result + (mPriority != +0.0f ? Float.floatToIntBits(mPriority) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
            "mPackageName='" + mPackageName + '\'' +
            ", mPriority=" + mPriority +
            '}';
    }
}
