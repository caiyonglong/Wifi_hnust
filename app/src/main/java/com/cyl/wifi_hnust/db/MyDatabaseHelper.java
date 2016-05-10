package com.cyl.wifi_hnust.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yonglong on 2016/5/10.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String DatabaseName="info.db";
    public final String TableName="info";


    private static final int DatabaseVersion=1;

    public MyDatabaseHelper(Context context) {
        super(context, DatabaseName, null, DatabaseVersion);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ TableName+" (account TEXT PRIMARY KEY, password TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
