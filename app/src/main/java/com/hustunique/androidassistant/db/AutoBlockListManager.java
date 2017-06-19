package com.hustunique.androidassistant.db;

import com.hustunique.androidassistant.model.AutoBlockListModel;
import com.hustunique.androidassistant.model.AutoBlockListModel_Table;
import com.hustunique.androidassistant.util.LogUtil;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

import java.util.List;

/**
 * Created by sunpe on 2017/6/19.
 */

public class AutoBlockListManager {
    private static final String TAG = "AutoBlockListManager";
    private ModelAdapter<AutoBlockListModel> adapter;

    public AutoBlockListManager() {
        adapter = FlowManager.getModelAdapter(AutoBlockListModel.class);
    }

    public void updateList(List<AutoBlockListModel> numbers) {
        SQLite.delete()
                .from(AutoBlockListModel.class)
                .execute();
        adapter.insertAll(numbers);
        LogUtil.d(TAG, "update autoblock");
    }

    public boolean insertToList(String number) {
        long id = adapter.insert(new AutoBlockListModel(number));
        LogUtil.d(TAG, "id: " + id);
        return id != -1;
    }

    public boolean ifNumberInAutoBlockList(String number) {
        List<AutoBlockListModel> list = SQLite.select()
                .from(AutoBlockListModel.class)
                .where(AutoBlockListModel_Table.number.eq(number))
                .queryList();
        assert list != null;
        if (list.size() != 0) {
            return true;
        }
        return false;
    }

    public List<AutoBlockListModel> getAllAutoBlocks() {
        List<AutoBlockListModel> list = SQLite.select()
                .from(AutoBlockListModel.class)
                .queryList();
        LogUtil.d(TAG, "size: " + list.size());
        for (AutoBlockListModel b : list) {
            LogUtil.d(TAG, "autoblock number: " + b.number);
        }
        return list;
    }
}
