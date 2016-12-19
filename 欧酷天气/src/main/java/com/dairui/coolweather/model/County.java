package com.dairui.coolweather.model;

/**
 * Created by 戴瑞Mic
 * Date: 2016/11/15 21:38
 */

public class County {
    private int id;
    private String county_Name;
    private String county_Code;
    private int City_id;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCounty_Name() {
        return county_Name;
    }

    public void setCounty_Name(String county_Name) {
        this.county_Name = county_Name;
    }

    public String getCounty_Code() {
        return county_Code;
    }

    public void setCounty_Code(String county_Code) {
        this.county_Code = county_Code;
    }

    public int getCity_id() {
        return City_id;
    }

    public void setCity_id(int city_id) {
        City_id = city_id;
    }


}
