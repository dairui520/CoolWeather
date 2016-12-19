package com.dairui.coolweather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dairui.coolweather.R;
import com.dairui.coolweather.db.CoolWeatherDB;
import com.dairui.coolweather.model.City;
import com.dairui.coolweather.model.County;
import com.dairui.coolweather.model.Province;
import com.dairui.coolweather.util.HttpCallbackListener;
import com.dairui.coolweather.util.HttpUtil;
import com.dairui.coolweather.util.ResponseParse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 戴瑞Mic
 * Date: 2016/12/10 15:44
 */

public class ChooseAreaActivity extends Activity {

    private static final int LEVEL_PROVINCE = 0; // 省份级别
    private static final int LEVEL_CITY = 1;
    private static final int LEVEL_COUNTRY = 2;

    // 服务器地址 默认省份
//    private static final String ADDRESS = "http://www.weather.com.cn/data/list3/city.xml";


    private TextView tv_title;
    private ListView list_view;
    private CoolWeatherDB coolWeatherDB;
    private List<String> dataList = new ArrayList<>(); // ListView 数据
    private ArrayAdapter<String> adapter;

    private int currentLevel; // 当前选择的级别

    private Province selectProvince;
    private City selectCity;
    private County selectCountry;

    private List<Province> provinceList;
    private List<County> countyList;
    private List<City> cityList;
    private ProgressDialog progressDialog; // 提醒对话框


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置 去标题栏的方法
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choore_area);
        tv_title = (TextView) findViewById(R.id.tv_title);
        list_view = (ListView) findViewById(R.id.list_view);
        adapter = new ArrayAdapter<String>(this, R.layout.simple_list_item_1, dataList);
        list_view.setAdapter(adapter);
        coolWeatherDB = CoolWeatherDB.getInstance(getApplicationContext());
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (currentLevel == LEVEL_PROVINCE) {
                    selectProvince = provinceList.get(position);
                    queryCitys();

                } else if (currentLevel == LEVEL_CITY) {
                    selectCity = cityList.get(position);
                    queryCountrys();
                } else if (currentLevel == LEVEL_COUNTRY) {
                    Toast.makeText(ChooseAreaActivity.this, "天气马上加载", Toast.LENGTH_SHORT).show();
                }
            }
        });
        queryProvinces();
    }

    /**
     * 查询省份
     */
    void queryProvinces() {
        provinceList = coolWeatherDB.getProvinces();
        // 如果数据库中有数据
        if (provinceList.size() > 0) {
            // 初始化dataList 数据
            dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getProvince_Name());
            }
            // 更新列表
            adapter.notifyDataSetChanged();
            // 设置当前的级别
            currentLevel = LEVEL_PROVINCE;
            tv_title.setText("中国");
        } else {
            qureyFromServer(null, "province");
        }
    }

    /**
     * 查询城市
     */
    private void queryCitys() {
        //从数据库查询
        cityList = coolWeatherDB.getCitys(selectProvince.getId());
        if (cityList.size() > 0) {
            // 初始化dataList 数据
            dataList.clear();
            tv_title.setText(selectProvince.getProvince_Name());
            for (City city : cityList) {
                dataList.add(city.getCity_Name());
            }
            // 更新ListView
            adapter.notifyDataSetChanged();
            currentLevel = LEVEL_CITY;
        } else {
            qureyFromServer(selectProvince.getProvince_Code(), "city");
        }
    }

    /**
     * 查询乡镇
     */
    private void queryCountrys() {
        // 从数据库中查询
        countyList = coolWeatherDB.getCountys(selectCity.getId());
        if (countyList.size() > 0) {
            // 初始化dataList 数据
            dataList.clear();
            tv_title.setText(selectCity.getCity_Name());
            // 更新ListView 数据
            for (County county : countyList) {
                dataList.add(county.getCounty_Name());
                adapter.notifyDataSetChanged();
            }
            currentLevel = LEVEL_COUNTRY;
        } else {
            // 从服务器中查询
            qureyFromServer(selectCity.getCity_Code(), "country");
        }
    }


    /**
     * 从服务器里面查询
     *
     * @param code 城市URL代码
     * @param type 从服务器获取的类型
     */
    private void qureyFromServer(final String code, final String type) {
        String address;
        if (TextUtils.isEmpty(code)) {
            address = "http://www.weather.com.cn/data/list3/city.xml";
        } else {
            address = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
        }
        // 显示对话框 提示
        showProgressDialog();
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                boolean result = false;
                if (type.equals("province")) {
                    result = ResponseParse.ParseProvince(response, coolWeatherDB);
                } else if (type.equals("city")) {
                    result = ResponseParse.ParseCity(response, coolWeatherDB, selectProvince.getId());
                }

                if (result) {
                    // 回到主线程 更新UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            closeProgressDialog(); // 关闭对话框
                            if (type.equals("province")) {
                                queryProvinces();
                            } else if (type.equals("city")) {
                                queryCitys();
                            } else if (type.equals("country")) {
                                queryCountrys();
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 加载失败也要关闭对话框
                        closeProgressDialog();
                        Toast.makeText(ChooseAreaActivity.this, "连接服务器失败，请稍后再试", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    /**
     * 显示对话框
     */
    private void showProgressDialog() {
        if (progressDialog == null) {
            // 对话框要用this
            progressDialog = new ProgressDialog(ChooseAreaActivity.this);
            progressDialog.setTitle("正在加载...");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
