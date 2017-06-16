package com.hustunique.androidassistant.db;

import com.hustunique.androidassistant.util.LogUtil;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

import java.util.List;

/**
 * Created by sunpe on 2017/6/14.
 */

public class BlackList {
    private static final String TAG = "BlackList";
    private ModelAdapter<BlackListEntry> adapter;

    public BlackList() {
        adapter = FlowManager.getModelAdapter(BlackListEntry.class);
    }

    public boolean addNewBlackNumber(String number) {
        if (!number.matches("[0-9]{11}")) {
            LogUtil.d(TAG, "regex not match");
            return false;
        }
        if (ifNumberInBlackList(number)) {
            LogUtil.d(TAG, "already in list");
            return false;
        }

        long id = adapter.insert(new BlackListEntry(number));

        return id != -1;
    }

    public List<BlackListEntry> getAllBlackNumbers() {
        List<BlackListEntry> blacklist = SQLite.select()
                .from(BlackListEntry.class)
                .where()
                .orderBy(BlackListEntry_Table.time, false)
                .queryList();

        // FIXME: debug
        for (BlackListEntry b : blacklist) {
            LogUtil.d(TAG, "number: " + b.number);
        }

        return blacklist;
    }

    public boolean ifNumberInBlackList(String number) {
        List<BlackListEntry> blacklist = SQLite.select()
                .from(BlackListEntry.class)
                .where(BlackListEntry_Table.number.eq(number))
                .queryList();
        if (blacklist.size() != 0) {
            return true;
        }
        return false;
    }

    public boolean deleteNumberFromBlackList(String number) {
        if (!number.matches("[0-9]{11}")) {
            LogUtil.d(TAG, "regex not match");
            return false;
        }

        SQLite.delete(BlackListEntry.class)
                .where(BlackListEntry_Table.number.eq(number))
//                .async()
                .execute();


        return true;
    }

}
