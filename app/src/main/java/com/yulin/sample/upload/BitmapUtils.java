package com.yulin.sample.upload;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BitmapUtils {

    /***
     * 根据Uri转换成Bitmap
     * @param context
     * @param uri
     * @return
     */
    public static Bitmap uriToBitmap(Context context, Uri uri){
        InputStream inputStream = null;
        try {
            inputStream = context.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
         try {
             if(inputStream != null) inputStream.close();
         }catch (IOException e) {
            e.printStackTrace();
         }
        }
    }

    /**
     * 压缩图片到指定文件
     * @param bmp
     * @param path
     * @return
     */
    public static boolean compressBitmap2file(Bitmap bmp, String path){
        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        int quality = 100;
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(path);
            return bmp.compress(format, quality, stream);
        }catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if(stream != null) stream.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
