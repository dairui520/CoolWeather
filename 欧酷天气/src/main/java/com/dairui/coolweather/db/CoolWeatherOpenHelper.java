package com.dairui.coolweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 戴瑞Mic
 * Date: 2016/11/15 21:15
 */

public class CoolWeatherOpenHelper extends SQLiteOpenHelper {

    /**
     * 创建省份表语句
     */
    private static final String CREATE_PROVINCE="Create table Province("
            +"id integer primary key autoincrement,"
            +"province_Name text,"
            +"province_Code text)";

    /**
     * 创建城市表语句
     */
    private static final String CREATE_CITY="Create table City("
            +"id Integer primary key autoincrement,"
            +"province_id integer,"
            +"city_Name text,"
            +"city_Code text)";

    /**
     * 创建县级表语句
     */
    private static final String CREATE_COUNTY="Create table County("
            +"id Integer primary key autoincrement,"
            +"city_id integer,"
            +"county_Name text,"
            +"county_Code text)";

    public CoolWeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // 创建表
        sqLiteDatabase.execSQL(CREATE_PROVINCE);
        sqLiteDatabase.execSQL(CREATE_CITY);
        sqLiteDatabase.execSQL(CREATE_COUNTY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
