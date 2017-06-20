package com.hustunique.androidassistant.model;

import com.hustunique.androidassistant.db.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

/**
 * Created by sunpe on 2017/6/16.
 */

@Table(database = AppDatabase.class)
public class BlackListModel {
    @PrimaryKey
    public String number;

    @Column
    public long time;

    public BlackListModel() {

    }

    public BlackListModel(String number) {
        this.number = number;
        this.time = System.currentTimeMillis();
    }

    public String getNum() {
        return number;
    }
}