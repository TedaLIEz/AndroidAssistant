package com.hustunique.androidassistant.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hustunique.androidassistant.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunpe on 2017/6/14.
 */

public class BlockedCallSaver {
    private static final String TAG = "BlockedCallSaver";
    private BlockedCallDbHelper mDbHelper;

    public BlockedCallSaver(Context context) {
        mDbHelper = new BlockedCallDbHelper(context);
    }

    public boolean addBlockedCall(String number) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.BlockedCallEntry.COLUMN_NUMBER, number);
        values.put(DatabaseContract.BlockedCallEntry.COLUMN_TIME, System.currentTimeMillis());
        long newRowId = db.insert(DatabaseContract.BlockedCallEntry.TABLE_NAME, null, values);

        db.close();

        return newRowId != -1;
    }

    public boolean deleteBlockedCall(String id) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String selection = DatabaseContract.BlockedCallEntry._ID + " LIKE ?";
        String[] selectionArgs = { id };

        db.delete(DatabaseContract.BlockedCallEntry.TABLE_NAME, selection, selectionArgs);
        db.close();

        return true;
    }

    public List<BlockedCallPack> getAllBlockedCall(){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                DatabaseContract.BlockedCallEntry._ID,
                DatabaseContract.BlockedCallEntry.COLUMN_NUMBER,
                DatabaseContract.BlockedCallEntry.COLUMN_TIME
        };
        String sortOrder = DatabaseContract.BlockedCallEntry.COLUMN_TIME + " DESC";

        Cursor c = db.query(
                DatabaseContract.BlockedCallEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder);

        List<BlockedCallPack> ret = new ArrayList<BlockedCallPack>();
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                BlockedCallPack callpack = new BlockedCallPack();

                callpack.id = c.getLong(c.getColumnIndex(DatabaseContract.BlockedCallEntry._ID));
                callpack.number = c.getString(c.getColumnIndex(DatabaseContract.BlockedCallEntry.COLUMN_NUMBER));
                callpack.timestamp = c.getLong(c.getColumnIndex(DatabaseContract.BlockedCallEntry.COLUMN_TIME));
                ret.add(callpack);

                LogUtil.d(TAG, "id: " + callpack.id);
                LogUtil.d(TAG, "number: " + callpack.number);
                c.moveToNext();
            }
        }
        db.close();
        return ret;
    }

    public class BlockedCallPack{
        public long id;
        public String number;
        public long timestamp;
    }
}
