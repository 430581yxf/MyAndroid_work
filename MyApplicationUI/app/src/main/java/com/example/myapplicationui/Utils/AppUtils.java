package com.example.myapplicationui.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.airbnb.lottie.L;
import com.example.myapplicationui.entity.Message;
import com.google.android.exoplayer.C;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

//这个就是功能类
public class AppUtils {
    private static OkHttpClient client=new OkHttpClient();
    public static Bitmap StringToIcon(String IconString){
        byte[] avatarBytes = Base64.decode(IconString, Base64.DEFAULT);
        Bitmap avatarBitmap = BitmapFactory.decodeByteArray(avatarBytes, 0, avatarBytes.length);
    return avatarBitmap;
    }
    //主要是发消息要设置时间,但是LocalDateTime前后端需要解析,索性就把它给拆开了,但是代码重复怎么办,static其他的就见名知义.
    public static void setTime(Message message){
        LocalDateTime localDateTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            localDateTime = LocalDateTime.now();
        message.setYear(localDateTime.getYear());
        message.setMonth(localDateTime.getMonth().ordinal());
        message.setDay(localDateTime.getDayOfMonth());
        message.setHour(localDateTime.getHour());
        message.setMinute(localDateTime.getMinute());
        message.setSec(localDateTime.getSecond());}
    }
    public static String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
    public static Bitmap fileToBitmap(File file) throws FileNotFoundException {
        FileInputStream inputStream = new FileInputStream(file);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        return bitmap;
    }
    public static byte[] bitmapToBytes(Bitmap bitmap) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] byteArray = outputStream.toByteArray();
        outputStream.close();
        return byteArray;
    }
    public static Bitmap byteToBitmap(byte[] byteArray){
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    public static String saveBase64ImageToFile(String base64String, Context context) throws IOException {
        String filePath = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime localDateTime = LocalDateTime.now();
            int year = localDateTime.getYear();
            int day = localDateTime.getDayOfYear();
            int hour=localDateTime.getHour();
            int min=localDateTime.getMinute();
            int sec=localDateTime.getSecond();
            // 保存到应用程序外部文件目录
            File externalDir = context.getExternalFilesDir(null);
            if (externalDir != null) {
                filePath = externalDir.getAbsolutePath() + File.separator +
                        String.valueOf(year + day+hour+min+sec) + ".PNG";
            }
        }
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

        if (filePath != null) {
            // 打开一个文件，如果文件目录不存在就创建目录
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                if (!file.getParentFile().mkdirs()) {
                    System.err.println("Failed to create directories for " + file);
                }
            }

            try {
                OutputStream os = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                os.flush();
                os.close();
                return filePath;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

