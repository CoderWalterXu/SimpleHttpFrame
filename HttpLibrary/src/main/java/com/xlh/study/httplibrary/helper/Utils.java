package com.xlh.study.httplibrary.helper;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author: Watler Xu
 * time:2020/4/8
 * description:
 * version:0.0.1
 */
public final class Utils {

    /**
     * 将InputStream转换成一个String对象
     *
     * @param inputStream
     * @return
     */
    public static String InputStreamConvertString(InputStream inputStream) throws IOException {
        String content = null;

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        StringBuilder sb = new StringBuilder();

        String line = null;

        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } finally {
            try {
                inputStream.close();
                return sb.toString();
            } catch (IOException e) {
            }
        }
        return content;
    }

}
