package com.xlh.study.httplibrary;

import com.xlh.study.httplibrary.data.bitmap.BitmapCallbackListener;
import com.xlh.study.httplibrary.data.bitmap.IBitmapDataListener;
import com.xlh.study.httplibrary.data.json.IJsonDataListener;
import com.xlh.study.httplibrary.data.json.JsonCallbackListener;
import com.xlh.study.httplibrary.http.HttpCallbackListener;
import com.xlh.study.httplibrary.http.HttpRequest;
import com.xlh.study.httplibrary.http.IHttpRequest;
import com.xlh.study.httplibrary.task.HttpTask;
import com.xlh.study.httplibrary.task.ThreadPoolManager;

/**
 * @author: Watler Xu
 * time:2020/4/8
 * description:供调用层调用
 * version:0.0.1
 */
public class WxHttp {


    /**
     * 请求json数据
     * @param url
     * @param requestData
     * @param response
     * @param listener
     * @param <T>
     * @param <R>
     */
    public static<T,R> void sendJsonRequest(String url, T requestData, Class<R> response, IJsonDataListener listener){
        IHttpRequest httpRequest = new HttpRequest();
        HttpCallbackListener httpCallbackListener = new JsonCallbackListener<>(response,listener);
//        HttpTask httpTask = new HttpTask(url,requestData,httpRequest,httpCallbackListener);
        // 使用构建者创建
        HttpTask httpTask = new HttpTask.Builder()
                .url(url)
                .requestData(requestData)
                .httpRequest(httpRequest)
                .httpCallbackListener(httpCallbackListener)
                // 是否重试
                .isRepeat(true)
                // 最大重试次数
                .maxRetryCount(2)
                // 重试延迟时间
                .retryDelayTime(6000)
                .build();
        ThreadPoolManager.getInstance().addTask(httpTask);
    }

    /**
     * 请求bitmap数据
     * @param url
     * @param listener
     */
    public static void sendBitmapRequest(String url, IBitmapDataListener listener){
        IHttpRequest httpRequest = new HttpRequest();
        HttpCallbackListener httpCallbackListener = new BitmapCallbackListener(listener);
        // 使用构建者创建
        HttpTask httpTask = new HttpTask.Builder()
                .url(url)
                .httpRequest(httpRequest)
                .httpCallbackListener(httpCallbackListener)
                // 是否重试
                .isRepeat(true)
                // 最大重试次数
                .maxRetryCount(2)
                // 重试延迟时间
                .retryDelayTime(6000)
                .build();
        ThreadPoolManager.getInstance().addTask(httpTask);
    }


}
