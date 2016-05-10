package com.cyl.wifi_hnust.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by yonglong on 2016/5/10.
 */
public class SQLOperation {
    MyDatabaseHelper helper;
    SQLiteDatabase db;
    Context context;

    public SQLOperation(Context context) {
        this.context = context;
        helper = new MyDatabaseHelper(context);
    }

}
