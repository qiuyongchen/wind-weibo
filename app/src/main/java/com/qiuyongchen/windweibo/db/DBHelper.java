package com.qiuyongchen.windweibo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 创建数据库或升级数据库
 * Created by qiuyongchen on 2016/2/22.
 */
public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, DBinfo.DB.DB_NAME, null, DBinfo.DB.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBinfo.TABLE.CREATE_USER_INFO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DBinfo.TABLE.USER_INFO_DROP);
    }
}
