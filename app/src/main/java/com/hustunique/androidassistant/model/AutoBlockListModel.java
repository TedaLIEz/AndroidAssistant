package com.hustunique.androidassistant.model;

import com.hustunique.androidassistant.db.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

/**
 * Created by sunpe on 2017/6/19.
 */

@Table(database = AppDatabase.class)
public class AutoBlockListModel {
    @PrimaryKey
    public String number;

    @Column
    public long time;

    public AutoBlockListModel() {

    }

    public AutoBlockListModel(String number) {
        this.number = number;
        this.time = System.currentTimeMillis();
    }
}