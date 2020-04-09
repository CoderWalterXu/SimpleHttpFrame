package com.xlh.study.simplehttpframe;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xlh.study.httplibrary.data.bitmap.IBitmapDataListener;
import com.xlh.study.httplibrary.data.json.IJsonDataListener;
import com.xlh.study.httplibrary.WxHttp;
import com.xlh.study.httplibrary.task.HttpTask;

public class MainActivity extends AppCompatActivity {

    Button btnJsonRequest,btnImageRequest;
    TextView tvResult,tvImageResult;
    ImageView iv;

    String jsonUrl = "http://apis.juhe.cn/ip/ipNew?ip=144.34.161.97&key=aa205eeb45aa76c6afe3c52151b52160";
    // 测试错误url,会返回503
//    String jsonUrlError = "http://1234";

    String imageUrl = "https://user-gold-cdn.xitu.io/2020/4/8/1715887e6504f544?imageView2/0/w/1280/h/960/format/webp/ignore-error/1";
    // 测试错误图片url,会返回404
//    String imageUrlError = "https://user-gold-cdn.xitu.io/2020/4/8/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnJsonRequest = findViewById(R.id.btn_request);
        tvResult = findViewById(R.id.tv_result);
        btnImageRequest = findViewById(R.id.btn_iamge_request);
        tvImageResult = findViewById(R.id.tv_iamge_result);
        iv = findViewById(R.id.iv);


        btnJsonRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendJsonRequest();
            }
        });

        btnImageRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendImageRequest();
            }
        });


    }

    private void sendImageRequest() {

        tvImageResult.setText("开始请求图片...");
        iv.setImageBitmap(null);

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
    }

    private void sendJsonRequest() {

        tvResult.setText("开始请求Json...");

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

    }
}
