package com.hustunique.androidassistant.db;

import com.hustunique.androidassistant.util.LogUtil;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

import java.util.List;

/**
 * Created by sunpe on 2017/6/14.
 */

public class BlockedCallSaver {
    private static final String TAG = "BlockedCallSaver";
    private ModelAdapter<BlockedCallEntry> adapter;

    public BlockedCallSaver() {
        adapter = FlowManager.getModelAdapter(BlockedCallEntry.class);
    }

    public boolean addBlockedCall(String number, boolean autoblocked) {
        long newRowId = adapter.insert(new BlockedCallEntry(number, autoblocked));
        LogUtil.d(TAG, "add blocked call");

        return newRowId != -1;
    }

    public boolean deleteBlockedCall(long time) {
        SQLite.delete(BlockedCallEntry.class)
                .where(BlockedCallEntry_Table.time.is(time))
//                .async()
                .execute();

        return true;
    }

    public List<BlockedCallEntry> getAllBlockedCall(){
        List<BlockedCallEntry> blocked = SQLite.select()
                .from(BlockedCallEntry.class)
                .where()
                .orderBy(BlockedCallEntry_Table.time, false)
                .queryList();

        // FIXME: debug
        for (BlockedCallEntry b : blocked) {
            LogUtil.d(TAG, "blocked call number: " + b.number);
            LogUtil.d(TAG, "blocked time: " + b.time);
        }

        return blocked;
    }

}
