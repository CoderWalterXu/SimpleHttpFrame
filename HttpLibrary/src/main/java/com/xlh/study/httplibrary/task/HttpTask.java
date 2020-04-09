package com.xlh.study.httplibrary.task;

import com.alibaba.fastjson.JSON;
import com.xlh.study.httplibrary.http.HttpCallbackListener;
import com.xlh.study.httplibrary.http.IHttpRequest;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author: Watler Xu
 * time:2020/4/8
 * description:网络请求任务
 * HttpTask实质上是一个Runnable
 * 为了实现重试，故实现了Delayed接口
 * version:0.0.1
 */
public class HttpTask<T> implements Runnable, Delayed {

    private IHttpRequest httpRequest;

    // 延迟到什么时间执行
    private long delayTime;
    // 延迟多少时间再请求
    private long retryDelayTime;
    // 最大重试次数
    private int maxRetryCount = 3;
    // 当前重试次数
    private int curRetryCount = 0;
    // 是否重试
    private boolean isRepeat;

    private HttpTask(Builder builder){

        this.delayTime = builder.retryDelayTime;
        this.retryDelayTime = builder.retryDelayTime;
        this.maxRetryCount = builder.maxRetryCount;
        this.isRepeat = builder.isRepeat;

        this.httpRequest = builder.httpRequest;
        httpRequest.setUrl(builder.url);
        httpRequest.setHttpListener(builder.listener);

        if(isRepeat){
            httpRequest.setMaxRetryCount(builder.maxRetryCount);
            httpRequest.setCurRetryCount(curRetryCount);
        }else{
            httpRequest.setMaxRetryCount(0);
            httpRequest.setCurRetryCount(0);
        }

        String content = JSON.toJSONString(builder.requestData);
        try {
            httpRequest.setRequestData(content.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public HttpTask(String url, T requestData, IHttpRequest httpRequest, HttpCallbackListener listener){
        this.httpRequest = httpRequest;
        httpRequest.setUrl(url);
        httpRequest.setHttpListener(listener);
        String content = JSON.toJSONString(requestData);

        httpRequest.setMaxRetryCount(maxRetryCount);
        httpRequest.setCurRetryCount(curRetryCount);

        try {
            httpRequest.setRequestData(content.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int compareTo(Delayed delayed) {
        return 0;
    }

    @Override
    public void run() {
        try {
            this.httpRequest.execute();
        } catch (Exception e) {

            // 网络请求出错时，重新添加到重试队列
            ThreadPoolManager.getInstance().addDelayTask(this);
            httpRequest.setCurRetryCount(curRetryCount+1);
        }
    }

    @Override
    public long getDelay(TimeUnit timeUnit) {
        return timeUnit.convert(this.delayTime - System.currentTimeMillis(),TimeUnit.MICROSECONDS);
    }

    public void setDelayTime(long delayTime) {
        // 当前系统时间+延迟时间，再去执行
        this.delayTime = delayTime + System.currentTimeMillis();
    }

    public long getRetryDelayTime() {
        return retryDelayTime;
    }

    public int getCurRetryCount() {
        return curRetryCount;
    }

    public void setCurRetryCount(int curRetryCount) {
        this.curRetryCount = curRetryCount;
    }

    public boolean isRepeat() {
        return isRepeat;
    }

    public int getMaxRetryCount() {
        return maxRetryCount;
    }

    public void setMaxRetryCount(int maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
    }


    // 构建者模式
    public final static class Builder<T>{
        // 延迟多少时间再请求
        private long retryDelayTime;
        // 最大重试次数
        private int maxRetryCount = 3;
        // 是否重试
        private boolean isRepeat = true;

        private String url;
        private T requestData;
        private IHttpRequest httpRequest;
        private HttpCallbackListener listener;

        public Builder(){
        }

        public Builder retryDelayTime(long retryDelayTime){
            this.retryDelayTime = retryDelayTime;
            return this;
        }

        public Builder maxRetryCount(int maxRetryCount){
            this.maxRetryCount = maxRetryCount;
            return this;
        }

        public Builder isRepeat(boolean isRepeat){
            this.isRepeat = isRepeat;
            return this;
        }

        public Builder url(String url){
            this.url = url;
            return this;
        }

        public Builder requestData(T requestData){
            this.requestData = requestData;
            return this;
        }

        public Builder httpRequest(IHttpRequest httpRequest){
            this.httpRequest = httpRequest;
            return this;
        }

        public Builder httpCallbackListener(HttpCallbackListener listener){
            this.listener = listener;
            return this;
        }

        public HttpTask build(){

            return new HttpTask(this);
        }

    }

}
