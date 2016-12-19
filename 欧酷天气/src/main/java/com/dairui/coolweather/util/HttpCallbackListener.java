package com.dairui.coolweather.util;

/**
 * Created by 戴瑞Mic
 * Date: 2016/11/17 14:40
 */


/**
 * 联网回调函数接口
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
