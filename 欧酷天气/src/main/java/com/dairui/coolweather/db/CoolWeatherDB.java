package com.dairui.coolweather.db;

/**
 * Created by 戴瑞Mic
 * Date: 2016/11/15 21:53
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dairui.coolweather.model.City;
import com.dairui.coolweather.model.County;
import com.dairui.coolweather.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库操作类
 * 因为经常使用，所以构造出单例模式
 */
public class CoolWeatherDB {

    // 数据库名字
    public static final String DB_NAME="cool_weather";
    // 数据库版本
    private static final int VERSION=1;

    private static CoolWeatherDB coolWeatherDB;

    private SQLiteDatabase db;

    // 构造方法私有化
    private CoolWeatherDB(Context context)
    {
        CoolWeatherOpenHelper helper=new CoolWeatherOpenHelper(context,DB_NAME,null,VERSION);
        db=helper.getWritableDatabase();
    }

    // 返回实例 加锁，防止多个线程同时调用，保证只有一个实例
    public static synchronized CoolWeatherDB getInstance(Context context)
    {
        if (coolWeatherDB==null)
        {
            coolWeatherDB=new CoolWeatherDB(context);
        }
        return coolWeatherDB;
    }

    /**
     * 把省份存入数据库中
     * @param province
     * @return
     */
    public boolean save_Province(Province province)
    {
        ContentValues values= new ContentValues();
        values.put("province_Name",province.getProvince_Name());
        values.put("province_Code",province.getProvince_Code());
        // 第二个参数 nullColumnHack 空列的默认值
        return db.insert("Province", null, values) != -1;
    }

    /**
     *  获取省份集合
     * @return
     */
    public List<Province> getProvinces()
    {
        Cursor cursor= db.query("Province",null,null,null,null,null,null);

        ArrayList<Province> provincesList=new ArrayList<>();
        while (cursor.moveToNext())
        {
            Province province=new Province();
            province.setId(cursor.getInt(cursor.getColumnIndex("id")));
            province.setProvince_Name(cursor.getString(cursor.getColumnIndex("province_Name")));
            province.setProvince_Code(cursor.getString(cursor.getColumnIndex("province_Code")));
            provincesList.add(province);
        }
        // 游标用完要关闭
        cursor.close();
        return provincesList;
    }

    /**
     * 把City 插入数据库中
     */
    public boolean save_City(City city)
    {
        ContentValues values= new ContentValues();
        values.put("province_id",city.getProvince_id());
        values.put("city_Name",city.getCity_Name());
        values.put("city_Code",city.getCity_Code());
        // 如果插入成功 返回true
        long result=db.insert("City", null, values);
        System.out.println("保存城市："+result);
        return true;

    }

    /**
     * 获取所选省份的City表的数据
     * @return City表的所有数据
     */
    public List<City> getCitys(int provicne_id)
    {
        ArrayList<City> CitysList=new ArrayList<>();
        Cursor cursor= db.query("City",null,"province_id=?",
                new String[]{String.valueOf(provicne_id)},null,null,null);
        while (cursor.moveToNext())
        {
            City city = new City();
            city.setId(cursor.getInt(cursor.getColumnIndex("id")));
            city.setCity_Name(cursor.getString(cursor.getColumnIndex("city_Name")));
            city.setCity_Code(cursor.getString(cursor.getColumnIndex("city_Name")));
            city.setProvince_id(cursor.getInt(cursor.getColumnIndex("province_id")));
            CitysList.add(city);
        }
        cursor.close();

        return CitysList;
    }

    /**
     *  把 County 存入数据库中
     * @param county
     * @return
     */
    public boolean save_County(County county)
    {
        ContentValues values=new ContentValues();
        values.put("county_Name",county.getCounty_Name());
        values.put("county_Code",county.getCounty_Code());
        values.put("city_id",county.getCity_id());

        return db.insert("County",null,values)!=-1;
    }

    /**
     * 返回所选城市的County的集合
     */
    public List<County> getCountys(int city_id)
    {
        ArrayList<County> countyList =new ArrayList<>();
        Cursor cursor= db.query("County",null,"city_id=?",new String[]{String.valueOf(city_id)},null,null,null);
        while (cursor.moveToNext())
        {
            County county=new County();
            county.setId(cursor.getInt(cursor.getColumnIndex("id")));
            county.setCounty_Name(cursor.getString(cursor.getColumnIndex("county_Name")));
            county.setCounty_Code(cursor.getString(cursor.getColumnIndex("county_Code")));
            county.setCity_id(cursor.getInt(cursor.getColumnIndex("city_id")));
            countyList.add(county);
        }
        return countyList;
    }
}
