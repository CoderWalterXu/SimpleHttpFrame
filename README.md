# SimpleHttpFrame
简单的Http请求框架，支持请求json和图片数据

## 使用
### 请求Json
```
        WxHttp.sendJsonRequest(jsonUrl, null, ResponseClass.class, new IJsonDataListener<ResponseClass>() {
            @Override
            public void onJsonDataSuccess(ResponseClass responseClass) {
                tvResult.setText("请求Json成功\r\n"+responseClass.toString());
            }

            @Override
            public void onJsonDataFailure(Exception e) {
                tvResult.setText("请求Json失败\r\n"+e.toString());
            }

        });
```
### 请求图片
```
        WxHttp.sendBitmapRequest(imageUrl, new IBitmapDataListener() {
            @Override
            public void onBitmapDataSuccess(Bitmap bitmap) {
                tvImageResult.setText("请求图片成功");
                iv.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapDataFailure(Exception e) {
                tvImageResult.setText("请求图片失败\r\n"+e.toString());
            }
        });         
```

![image](https://note.youdao.com/yws/api/personal/file/36C0D246BBC24345B8AF80FCCD603B0E?method=download&shareKey=ab68d046b2bd40798b3d27251525d0a4)


## 要点
* 构建者模式设置请求相关（待实现：由调用层设置）
* 线程池管理请求线程，单例模式实现
* 请求任务实现Delayed接口，来支持请求失败重试
* 使用HttpURLConnection来发起网络请求
