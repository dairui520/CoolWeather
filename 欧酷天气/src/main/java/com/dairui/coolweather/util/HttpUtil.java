package com.dairui.coolweather.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 戴瑞Mic
 * Date: 2016/11/17 14:37
 */

public class HttpUtil {

    /**
     * 经常要使用这个类，所以把他写成静态的类
     */

    public static void sendHttpRequest(final String address, final HttpCallbackListener listener) {
        new Thread() {

            private HttpURLConnection connection=null;

            @Override
            public void run() {
                try {
                    URL url =new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bfr = new BufferedReader(new InputStreamReader(inputStream));
                    String line = null;
                    StringBuilder response = new StringBuilder();
                    while ((line = bfr.readLine()) != null) {
                        response.append(line);
                    }
                    if (listener != null) {
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    // 如果出错
                    if (listener != null) {
                        listener.onError(e);
                    }
                }finally {
                    // 释放资源
                    if (connection!=null)
                    {
                        connection.disconnect();
                    }
                }
            }
        }.start();
    }
}
