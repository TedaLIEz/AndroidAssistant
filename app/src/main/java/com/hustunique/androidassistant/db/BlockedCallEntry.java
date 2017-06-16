package com.hustunique.androidassistant.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

/**
 * Created by sunpe on 2017/6/16.
 */

@Table(database = AppDatabase.class)
public class BlockedCallEntry {
    @Column
    public String number;

    @Column
    public boolean autoblocked;

    @PrimaryKey
    public long time;

    public BlockedCallEntry(){

    }

    public BlockedCallEntry(String number, boolean autoblocked) {
        this.number = number;
        this.autoblocked = autoblocked;
        this.time = System.currentTimeMillis();
    }

}