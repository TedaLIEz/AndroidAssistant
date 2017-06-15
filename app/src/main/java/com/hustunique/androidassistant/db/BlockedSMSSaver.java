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

public class BlockedSMSSaver {
    private static final String TAG = "BlockedSMSSaver";
    private BlockedSMSDbHelper mDbHelper;

    public BlockedSMSSaver(Context context) {
        mDbHelper = new BlockedSMSDbHelper(context);
    }

    public boolean addBlockedSMS(String sourceNumber, String text) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.BlockedSMSEntry.COLUMN_NUMBER, sourceNumber);
        values.put(DatabaseContract.BlockedSMSEntry.COLUMN_TEXT, text);
        values.put(DatabaseContract.BlockedSMSEntry.COLUMN_TIME, System.currentTimeMillis());
        long newRowId = db.insert(DatabaseContract.BlockedSMSEntry.TABLE_NAME, null, values);

        db.close();

        return newRowId != -1;
    }

    public boolean deleteBlockedSMS(String id) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String selection = DatabaseContract.BlockedSMSEntry._ID + " LIKE ?";
        String[] selectionArgs = { id };

        db.delete(DatabaseContract.BlockedSMSEntry.TABLE_NAME, selection, selectionArgs);
        db.close();

        return true;
    }

    public List<SMSPack> getAllBlockedSMS(){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                DatabaseContract.BlockedSMSEntry._ID,
                DatabaseContract.BlockedSMSEntry.COLUMN_NUMBER,
                DatabaseContract.BlockedSMSEntry.COLUMN_TEXT,
                DatabaseContract.BlockedSMSEntry.COLUMN_TIME
        };
        String sortOrder = DatabaseContract.BlockedSMSEntry.COLUMN_TIME + " DESC";

        Cursor c = db.query(
                DatabaseContract.BlockedSMSEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder);

        List<SMSPack> ret = new ArrayList<SMSPack>();
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                SMSPack sms = new SMSPack();

                sms.id = c.getLong(c.getColumnIndex(DatabaseContract.BlockedSMSEntry._ID));
                sms.number = c.getString(c.getColumnIndex(DatabaseContract.BlockedSMSEntry.COLUMN_NUMBER));
                sms.text = c.getString(c.getColumnIndex(DatabaseContract.BlockedSMSEntry.COLUMN_TEXT));
                sms.timestamp = c.getLong(c.getColumnIndex(DatabaseContract.BlockedSMSEntry.COLUMN_TIME));
                ret.add(sms);

                LogUtil.d(TAG, "id: " + sms.id);
                LogUtil.d(TAG, "number: " + sms.number);
                LogUtil.d(TAG, "text: " + sms.text);
                c.moveToNext();
            }
        }

        db.close();

        return ret;
    }

    public class SMSPack {
        public long id;
        public String number;
        public String text;
        public long timestamp;
    }
}
