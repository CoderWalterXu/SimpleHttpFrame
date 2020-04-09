package com.xlh.study.httplibrary.http;

import android.util.Log;

import com.xlh.study.httplibrary.helper.Constants;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import static com.xlh.study.httplibrary.helper.Constants.TAG;

/**
 * @author: Watler Xu
 * time:2020/4/8
 * description:具体的网络请求类
 * 使用HttpURLConnection来发起网络请求
 * 需要传入url、参数、回调接口，网络结果通过回调返回
 * version:0.0.1
 */
public class HttpRequest implements IHttpRequest {

    String url;
    byte[] data;
    HttpCallbackListener httpCallbackListener;
    HttpURLConnection urlConnection;

    // 最大重试次数
    private int maxRetryCount;
    // 当前重试次数
    private int curRetryCount;

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void setRequestData(byte[] data) {
        this.data = data;
    }

    @Override
    public void setHttpListener(HttpCallbackListener httpListener) {
        this.httpCallbackListener = httpListener;
    }

    @Override
    public void execute() {
        URL url = null;
        try {
            url = new URL(this.url);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(6000);
            urlConnection.setUseCaches(false);
            urlConnection.setInstanceFollowRedirects(true);
            urlConnection.setReadTimeout(3000);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            // 设置token
            // urlConnection.setRequestProperty("token", "abf10d1646f4108faddc453506312f5e");
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.connect();

            OutputStream out = urlConnection.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(out);
            bos.write(data);
            bos.flush();
            out.close();
            bos.close();
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = urlConnection.getInputStream();
                httpCallbackListener.onSuccess(in);
            } else {
                Log.e(Constants.TAG, "== 请求失败 == http错误码：" + urlConnection.getResponseCode());
                httpCallbackListener.onFailure(new RuntimeException("== 请求失败 == http错误码：" + urlConnection.getResponseCode()));
            }
        }
        catch (Exception e) {
            Log.e(Constants.TAG, "== 请求失败 == Exception:" + e.toString());
            if(getCurRetryCount() >= getMaxRetryCount()){
                Log.e(TAG, "=== 重试机制 === 到达重试最大次数，不再重试");
                httpCallbackListener.onFailure(e);
                return;
            }
            throw new RuntimeException("== 请求失败 == Exception:" + e.toString());

        }
        finally {
            urlConnection.disconnect();
        }
    }


    public int getMaxRetryCount() {
        return maxRetryCount;
    }

    @Override
    public void setMaxRetryCount(int maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
    }

    public int getCurRetryCount() {
        return curRetryCount;
    }

    @Override
    public void setCurRetryCount(int curRetryCount) {
        this.curRetryCount = curRetryCount;
    }

}
