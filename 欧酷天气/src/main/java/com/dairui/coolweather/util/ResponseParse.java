package com.dairui.coolweather.util;

/**
 * Created by 戴瑞Mic
 * Date: 2016/11/17 15:56
 */

import android.text.TextUtils;

import com.dairui.coolweather.db.CoolWeatherDB;
import com.dairui.coolweather.model.City;
import com.dairui.coolweather.model.County;
import com.dairui.coolweather.model.Province;

/**
 * 解析服务器返回的数据
 */
public class ResponseParse {

    /**
     * 解析省份，并存入数据库
     * @param response
     * @return
     */
    public static boolean ParseProvince(String response, CoolWeatherDB db)
    {
        // TODO: 没有加锁，现在还不了解 2016/11/17
       // 判断response 是否有值
        if (!TextUtils.isEmpty(response))
        {
            String [] allprovince=response.split(",");
            for (String s : allprovince) {
                //   |在正则表达式里表示或者的意思，需要转义  \\
                String Provincearray[]=s.split("\\|");
                Province province=new Province();
                province.setProvince_Code(Provincearray[0]);
                province.setProvince_Name(Provincearray[1]);
                System.out.println("保存省份："+db.save_Province(province));
            }
            return true;
        }
        return false;
    }

    /**
     * 解析City,存入数据库中
     * @param response
     * @param db
     * @param ProvinceId 省份ID
     * @return
     */
    public static boolean ParseCity(String response,CoolWeatherDB db,int ProvinceId)
    {
        // TODO: 没有加锁，现在还不了解 2016/11/17
        // 判断response 是否有值
        if (!TextUtils.isEmpty(response))
        {
            String [] allCity=response.split(",");
            for (String s : allCity) {
                String Cityarray[]=s.split("\\|");
                City city=new City();
                city.setCity_Code(Cityarray[0]);
                city.setCity_Name(Cityarray[1]);
                city.setProvince_id(ProvinceId);
                boolean result= db.save_City(city);
               System.out.println("保存城市 : "+result);

            }
            return true;
        }
        return false;
    }

    public static boolean ParseCounty(String response,CoolWeatherDB db,int CityId)
    {
        // TODO: 没有加锁，现在还不了解 2016/11/17
        if (!TextUtils.isEmpty(response))
        {
            String [] allCounty=response.split(",");
            for (String s : allCounty) {
                String Countyarray[]=s.split("\\|");
                County county=new County();
                county.setCity_id(CityId);
                county.setCounty_Code(Countyarray[0]);
                county.setCounty_Name(Countyarray[1]);
                db.save_County(county);
            }
            return true;
        }
        return false;
    }
}
