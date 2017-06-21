package com.hustunique.androidassistant.model;

import com.hustunique.androidassistant.db.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by sunpe on 2017/6/16.
 */

@Table(database = AppDatabase.class)
public class BlockedCallModel {
    @Column
    public String number;

    @Column
    public boolean autoblocked;

    @PrimaryKey
    public long time;

    public BlockedCallModel(){

    }

    public BlockedCallModel(String number, boolean autoblocked) {
        this.number = number;
        this.autoblocked = autoblocked;
        this.time = System.currentTimeMillis();
    }

    public String getTime() {
        return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(new Date(this.time));
    }

    public String getPhoneNum() {
        return number;
    }

    public int getType() {
        return autoblocked?0:1;
    }

}