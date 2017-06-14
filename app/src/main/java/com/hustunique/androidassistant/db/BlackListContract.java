package com.hustunique.androidassistant.db;

import android.provider.BaseColumns;

/**
 * Created by sunpe on 2017/6/14.
 */

public final class BlackListContract {
    private BlackListContract(){}

    public static class BlackListEntry implements BaseColumns {
        public static final String TABLE_NAME = "blacklist";
        public static final String COLUMN_NUMBER = "phonenumber";
        public static final String COLUMN_TIME = "time";
    }
}
