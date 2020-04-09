package com.xlh.study.httplibrary.data.json;

import android.os.Handler;
import android.os.Looper;

import com.alibaba.fastjson.JSON;
import com.xlh.study.httplibrary.data.json.IJsonDataListener;
import com.xlh.study.httplibrary.helper.Utils;
import com.xlh.study.httplibrary.http.HttpCallbackListener;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author: Watler Xu
 * time:2020/4/8
 * description: Json数据的回调接口
 * 当网络请求成功后，将InputStream依次转换成String、Json对象，然后返回，转换成功后，使用Handler切换到主线程
 * version:0.0.1
 */
public class JsonCallbackListener<T> implements HttpCallbackListener {

    // 返回调用层的类
    private Class<T> responseClass;
    // 处理网络返回的InputStream成Json数据的回调接口
    private IJsonDataListener iJsonDataListener;

    // 网络请求在子线程，更新UI在主线程，使用Handler做线程切换
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public JsonCallbackListener(Class<T> responseClass, IJsonDataListener iJsonDataListener) {
        this.responseClass = responseClass;
        this.iJsonDataListener = iJsonDataListener;
    }

    @Override
    public void onSuccess(InputStream inputStream) {

        try {
            // 将InputStream转换成String对象
            String response = Utils.InputStreamConvertString(inputStream);
            // 将String对象转换成一个Json对象
            final T clazz = JSON.parseObject(response, responseClass);
            // 将class对象回调给调用层
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    iJsonDataListener.onJsonDataSuccess(clazz);
                }
            });

        } catch (IOException e) {
            iJsonDataListener.onJsonDataFailure(e);
            e.printStackTrace();
        }


    }

    @Override
    public void onFailure(final Exception e) {
        // 将异常回调给调用层
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                iJsonDataListener.onJsonDataFailure(e);
            }
        });
    }
}
