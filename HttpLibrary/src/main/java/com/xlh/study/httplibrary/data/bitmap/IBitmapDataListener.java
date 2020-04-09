package com.xlh.study.httplibrary.data.bitmap;

import android.graphics.Bitmap;

/**
 * @author: Watler Xu
 * time:2020/4/9
 * description:处理网络返回的InputStream成Bitmap数据的回调接口
 * 定义了成功和失败两个行为
 * version:0.0.1
 */
public interface IBitmapDataListener {

    void onBitmapDataSuccess(Bitmap bitmap);

    void onBitmapDataFailure(Exception e);


}
