package com.dairui.coolweather.model;

/**
 * Created by 戴瑞Mic
 * Date: 2016/11/15 21:37
 */

public class City {
    private int id;
    private String city_Name;
    private String city_Code;
    private int province_id;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity_Name() {
        return city_Name;
    }

    public void setCity_Name(String city_Name) {
        this.city_Name = city_Name;
    }

    public String getCity_Code() {
        return city_Code;
    }

    public void setCity_Code(String city_Code) {
        this.city_Code = city_Code;
    }

    public int getProvince_id() {
        return province_id;
    }

    public void setProvince_id(int province_id) {
        this.province_id = province_id;
    }


}
