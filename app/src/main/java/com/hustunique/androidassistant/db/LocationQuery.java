package com.hustunique.androidassistant.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hustunique.androidassistant.R;

/**
 * Created by sunpe on 2017/6/19.
 */

public class LocationQuery {
    LocationDbHelper mDbHelper;
    String unknown;

    public LocationQuery(Context context) {
        mDbHelper = new LocationDbHelper(context);
        unknown = context.getString(R.string.unknown_area);
        mDbHelper.importIfNotExist();
    }

    public String getLocationInfo(String number) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String subNumber = number.substring(0, 7);
        String[] projection = {
                "_id",
                "area"
        };

        String selection = "_id = ?";
        String[] selectionArgs = {subNumber};
        Cursor c = null;
        try {
            c = db.query(
                "phone_location",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
            );
            if (c.getCount() == 0) {
                return this.unknown;
            }
            c.moveToFirst();
            String area = c.getString(c.getColumnIndex("area"));
            return area.replaceAll("\\[.*\\]", "");
        } finally {
            if (c != null) {
                c.close();
            }
        }

    }
}
