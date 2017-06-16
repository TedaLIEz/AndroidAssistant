package com.hustunique.androidassistant.db;

import com.hustunique.androidassistant.util.LogUtil;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

import java.util.List;

/**
 * Created by sunpe on 2017/6/14.
 */

public class BlockedSMSSaver {
    private static final String TAG = "BlockedSMSSaver";
    private ModelAdapter<BlockedSMSEntry> adapter;

    public BlockedSMSSaver() {
        adapter = FlowManager.getModelAdapter(BlockedSMSEntry.class);
    }

    public boolean addBlockedSMS(String sourceNumber, String text, boolean autoblocked) {
        long newRowId = adapter.insert(new BlockedSMSEntry(sourceNumber, text, autoblocked));

        return newRowId != -1;
    }

    public boolean deleteBlockedSMS(long time) {
        SQLite.delete(BlockedSMSEntry.class)
                .where(BlockedSMSEntry_Table.time.is(time))
//                .async()
                .execute();

        return true;
    }

    public List<BlockedSMSEntry> getAllBlockedSMS(){
        List<BlockedSMSEntry> blocked = SQLite.select()
                .from(BlockedSMSEntry.class)
                .where()
                .orderBy(BlockedSMSEntry_Table.time, false)
                .queryList();

        //FIXME: debug
        for (BlockedSMSEntry b : blocked) {
            LogUtil.d(TAG, "blocked sms number: " + b.number);
            LogUtil.d(TAG, "blocked sms text: " + b.text);
        }

        return blocked;
    }

}
