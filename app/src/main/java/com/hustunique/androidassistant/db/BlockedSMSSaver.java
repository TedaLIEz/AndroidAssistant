package com.hustunique.androidassistant.db;

import com.hustunique.androidassistant.model.BlockedSMSModel;
import com.hustunique.androidassistant.model.BlockedSMSModel_Table;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

import java.util.List;

/**
 * Created by sunpe on 2017/6/14.
 */

public class BlockedSMSSaver {
    private static final String TAG = "BlockedSMSSaver";
    private ModelAdapter<BlockedSMSModel> adapter;

    public BlockedSMSSaver() {
        adapter = FlowManager.getModelAdapter(BlockedSMSModel.class);
    }

    public boolean addBlockedSMS(String sourceNumber, String text, boolean autoblocked) {
        long newRowId = adapter.insert(new BlockedSMSModel(sourceNumber, text, autoblocked));

        return newRowId != -1;
    }

    public boolean deleteBlockedSMS(long time) {
        SQLite.delete(BlockedSMSModel.class)
                .where(BlockedSMSModel_Table.time.is(time))
//                .async()
                .execute();

        return true;
    }

    public List<BlockedSMSModel> getAllBlockedSMS(){
        List<BlockedSMSModel> blocked = SQLite.select()
                .from(BlockedSMSModel.class)
                .where()
                .orderBy(BlockedSMSModel_Table.time, false)
                .queryList();

        return blocked;
    }

}
