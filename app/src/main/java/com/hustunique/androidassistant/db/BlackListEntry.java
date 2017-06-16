package com.hustunique.androidassistant.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

/**
 * Created by sunpe on 2017/6/16.
 */

@Table(database = AppDatabase.class)
public class BlackListEntry {
    @PrimaryKey
    public String number;

    @Column
    public long time;

    public BlackListEntry() {

    }

    public BlackListEntry(String number) {
        this.number = number;
        this.time = System.currentTimeMillis();
    }
}