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

public class BlackList {
    private static final String TAG = "BlackList";
    private BlackListDbHelper mDbHelper;

    public BlackList(Context context) {
        mDbHelper = new BlackListDbHelper(context);
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

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.BlackListEntry.COLUMN_NUMBER, number);
        values.put(DatabaseContract.BlackListEntry.COLUMN_TIME, System.currentTimeMillis());
        long newRowId = db.insert(DatabaseContract.BlackListEntry.TABLE_NAME, null, values);

        db.close();

        return newRowId != -1;
    }

    public List<String> getAllBlackNumbers() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                DatabaseContract.BlackListEntry._ID,
                DatabaseContract.BlackListEntry.COLUMN_NUMBER,
                DatabaseContract.BlackListEntry.COLUMN_TIME
        };
        String sortOrder = DatabaseContract.BlackListEntry.COLUMN_TIME + " DESC";

        Cursor c = db.query(
                DatabaseContract.BlackListEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder);

        List<String> ret = new ArrayList<String>();
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                String number = c.getString(c.getColumnIndex(DatabaseContract.BlackListEntry.COLUMN_NUMBER));
                ret.add(number);
                LogUtil.d(TAG, number);
                c.moveToNext();
            }
        }

        db.close();

        return ret;
    }

    public boolean ifNumberInBlackList(String number) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                DatabaseContract.BlackListEntry.COLUMN_NUMBER
        };

        String selection = DatabaseContract.BlackListEntry.COLUMN_NUMBER + " = ?";
        String[] selectionArgs = { number };

        Cursor c = db.query(
                DatabaseContract.BlackListEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);

        boolean ret = c.getCount() != 0;
        db.close();

        return ret;
    }

    public boolean deleteNumberFromBlackList(String number) {
        if (!number.matches("[0-9]{11}")) {
            LogUtil.d(TAG, "regex not match");
            return false;
        }
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String selection = DatabaseContract.BlackListEntry.COLUMN_NUMBER + " LIKE ?";
        String[] selectionArgs = { number };

        db.delete(DatabaseContract.BlackListEntry.TABLE_NAME, selection, selectionArgs);
        db.close();

        return true;
    }
}
