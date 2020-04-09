package com.xlh.study.httplibrary.data.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;

import com.xlh.study.httplibrary.http.HttpCallbackListener;

import java.io.InputStream;

/**
 * @author: Watler Xu
 * time:2020/4/9
 * description:Bitmap数据的回调接口
 * 当网络请求成功后，将InputStream依次转换成Bitmap对象，然后返回，转换成功后，使用Handler切换到主线程
 * version:0.0.1
 */
public class BitmapCallbackListener implements HttpCallbackListener {

    private IBitmapDataListener iBitmapDataListener;

    // 网络请求在子线程，更新UI在主线程，使用Handler做线程切换
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public BitmapCallbackListener(IBitmapDataListener iBitmapDataListener) {
        this.iBitmapDataListener = iBitmapDataListener;
    }

    @Override
    public void onSuccess(InputStream inputStream) {

        try {
            // 将InputStream转换成Bitmap对象
            final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            // 将Bitmap对象回调给调用层
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    iBitmapDataListener.onBitmapDataSuccess(bitmap);
                }
            });

        } catch (Exception e) {
            iBitmapDataListener.onBitmapDataFailure(e);
            e.printStackTrace();
        }

    }

    @Override
    public void onFailure(final Exception e) {
        // 将异常回调给调用层
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                iBitmapDataListener.onBitmapDataFailure(e);
            }
        });
    }
}
