package com.xlh.study.httplibrary.http;

/**
 * @author: Watler Xu
 * time:2020/4/8
 * description:网络请求的抽象类
 * 基础：抽取了设置url、请求参数、回调结果、执行网络请求等4个行为
 * 新增：重试次数
 * version:0.0.1
 */
public interface IHttpRequest {

    /**
     * 设置请求url
     * @param url
     */
    void setUrl(String url);

    /**
     * 设置请求参数
     * @param data
     */
    void setRequestData(byte[] data);

    /**
     * 设置网络请求回调
     * @param httpListener
     */
    void setHttpListener(HttpCallbackListener httpListener);

    /**
     * 执行网络请求
     * 可以通过HttpURLConnection发起网络请求
     */
    void execute();

    /**
     * 设置最大重试次数
     * @param maxRetryCount
     */
    void setMaxRetryCount(int maxRetryCount);

    /**
     * 设置当前重试次数
     * @param curRetryCount
     */
    void setCurRetryCount(int curRetryCount);

}
