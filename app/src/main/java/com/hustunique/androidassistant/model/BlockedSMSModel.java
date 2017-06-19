package com.hustunique.androidassistant.model;

import com.hustunique.androidassistant.db.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

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

}
