package com.xlh.study.httplibrary.http;

import java.io.InputStream;

/**
 * @author: Watler Xu
 * time:2020/4/8
 * description:网络请求的回调接口
 * 定义请求成功和失败两个行为
 * version:0.0.1
 */
public interface HttpCallbackListener {

    /**
     * 成功时，具体的网络执行结果，返回的就是InputStream
     * @param inputStream
     */
    void onSuccess(InputStream inputStream);

    void onFailure(Exception e);

}
