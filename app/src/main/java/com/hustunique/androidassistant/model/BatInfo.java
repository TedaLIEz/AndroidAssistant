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
 * Created by JianGuo on 6/12/17.
 */

public class BatInfo {
    private int mPct;
    private boolean mCharged;

    public BatInfo() {
    }

    public int getPct() {
        return mPct;
    }

    public boolean isCharged() {
        return mCharged;
    }

    public void setCharged(boolean charged) {
        mCharged = charged;
    }

    public void setPct(int pct) {
        mPct = pct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BatInfo power = (BatInfo) o;

        if (mPct != power.mPct) {
            return false;
        }
        return mCharged == power.mCharged;

    }

    @Override
    public int hashCode() {
        int result = mPct;
        result = 31 * result + (mCharged ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BatInfo{" +
            "mPct=" + mPct +
            ", mCharged=" + mCharged +
            '}';
    }
}
