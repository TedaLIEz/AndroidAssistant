package com.hustunique.androidassistant.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.hustunique.androidassistant.util.LogUtil;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by sunpe on 2017/6/19.
 */

public class LocationDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "location.db";
    public String db_path;
    public static final String TAG = "LocationDbHelper";
    private Context context;

    public LocationDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db_path = "/data/data/" + context.getPackageName() + "/databases/";
        this.context = context;
    }


    public boolean checkExist() {

        SQLiteDatabase checkDB = null;

        try {
            String myPath = db_path + DATABASE_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READONLY);

        } catch (SQLiteException e) {
            e.printStackTrace();
            // database does't exist yet.
            LogUtil.d(TAG, "Not exist");

        } catch (Exception ep) {
            ep.printStackTrace();
        }

        if (checkDB != null) {
            LogUtil.d(TAG, "Exist");
            checkDB.close();
        }

        return checkDB != null;
    }

    public void importIfNotExist() {

        boolean dbExist = checkExist();

        if (dbExist) {
            // do nothing - database already exist
        } else {

            // By calling this method and empty database will be created into
            // the default system path
            // of your application so we are gonna be able to overwrite that
            // database with our database.
            this.getReadableDatabase();

            try {

                copyDatabase();

            } catch (IOException e) {
                LogUtil.d(TAG, "Database copy error");
            }
        }

    }

    private void copyDatabase() throws IOException {
        InputStream is = context.getAssets().open(DATABASE_NAME);
        if (is == null) {
            LogUtil.d(TAG, "input open error");
        }

        OutputStream os = new FileOutputStream(db_path + DATABASE_NAME);
        if (os == null) {
            LogUtil.d(TAG, "output open error");
        }

        byte[] buffer = new byte[4096];
        int length;
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
        os.flush();
        os.close();
        is.close();
        this.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
