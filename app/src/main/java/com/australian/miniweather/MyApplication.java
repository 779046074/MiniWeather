package com.australian.miniweather;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.australian.miniweather.model.DaoMaster;
import com.australian.miniweather.model.DaoSession;

public class MyApplication extends Application {

    public static DaoSession mSession;


    @Override
    public void onCreate() {
        super.onCreate();
        initDb();
    }

    /**
     * 链接数据库并创建会话
     */
    public void initDb(){
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this,"weather.db");
        SQLiteDatabase db = devOpenHelper.getReadableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        mSession = daoMaster.newSession();
    }
}
