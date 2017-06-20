package com.hustunique.androidassistant.db;

import com.hustunique.androidassistant.model.BlockedCallModel;
import com.hustunique.androidassistant.model.BlockedCallModel_Table;
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
    private ModelAdapter<BlockedCallModel> adapter;

    public BlockedCallSaver() {
        adapter = FlowManager.getModelAdapter(BlockedCallModel.class);
    }

    public boolean addBlockedCall(String number, boolean autoblocked) {
        long newRowId = adapter.insert(new BlockedCallModel(number, autoblocked));
        LogUtil.d(TAG, "add blocked call");

        return newRowId != -1;
    }

    public boolean deleteBlockedCall(long time) {
        SQLite.delete(BlockedCallModel.class)
                .where(BlockedCallModel_Table.time.is(time))
//                .async()
                .execute();

        return true;
    }

    public List<BlockedCallModel> getAllBlockedCall(){
        List<BlockedCallModel> blocked = SQLite.select()
                .from(BlockedCallModel.class)
                .where()
                .orderBy(BlockedCallModel_Table.time, false)
                .queryList();

        return blocked;
    }

}
