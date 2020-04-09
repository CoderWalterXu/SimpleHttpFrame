package com.xlh.study.httplibrary.data.json;

/**
 * @author: Watler Xu
 * time:2020/4/8
 * description:处理网络返回的InputStream成Json数据的回调接口
 * 定义了成功和失败两个行为
 * version:0.0.1
 */
public interface IJsonDataListener<T> {

    void onJsonDataSuccess(T t);

    void onJsonDataFailure(Exception e);

}
