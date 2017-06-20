package com.hustunique.androidassistant.model;

import com.hustunique.androidassistant.db.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sunpe on 2017/6/16.
 */

@Table(database = AppDatabase.class)
public class BlockedSMSModel {

    @Column
    public String number;

    @Column
    public String text;

    @Column
    public boolean autoblocked;

    @PrimaryKey
    public long time;

    public BlockedSMSModel(){

    }

    public BlockedSMSModel(String number, String text, boolean autoblocked) {
        this.number = number;
        this.text = text;
        this.autoblocked = autoblocked;
        this.time = System.currentTimeMillis();
    }

    public String getTime() {
        return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(this.time));
    }

    public String getPhoneNum() {
        return number;
    }

    public String getContent() {
        return text;
    }

    public int getType() {
        return autoblocked?0:1;
    }

}
