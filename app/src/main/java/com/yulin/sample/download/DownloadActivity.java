package com.yulin.sample.download;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.yulin.download_upload.download.RetrofitDownloadAdapter;
import com.yulin.download_upload.download.RetrofitDownloadConfig;
import com.yulin.download_upload.download.RetrofitDownloadManager;
import com.yulin.sample.R;

import java.io.File;

/**
 * Created by YuLin on 2016/12/29 0029.
 */

public class DownloadActivity extends AppCompatActivity {

    private static final String TAG = DownloadActivity.class.getSimpleName();
    private ImageView mImg;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        mImg = (ImageView) findViewById(R.id.id_img);
    }

    private String strPic = "https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1482992522&di=ecf87c21daca666147868fc5aaeef8e3&src=http://image57.360doc.com/DownloadImg/2012/12/1116/28840930_20.jpg";
    private String strApk = "http://oss-ccclubs.oss-cn-hangzhou.aliyuncs.com/app/20161208174314986.apk";

    public void downloadPic(View v) {
        startDownloadPic(strPic);
    }

    public void downloadApk(View v) {
        startDownloadApk(strApk);
    }

    private void startDownloadPic(String url) {
        RetrofitDownloadConfig retrofitDownloadConfig = new RetrofitDownloadConfig.Builder(this)
                .setSavingFile(getImgFile())
                .setNotification(getNotification())
                .setRemoteView(getRemoteView())
                .setDownloadingTip("正在下载...")
                .setDownloadSuccessTip("下载成功")
                .setDownloadFailureTip("下载失败")
                .setDownloadCompleteTxtId(R.id.tv_txt)
                .setDownloadingProgressTxtId(R.id.tv_progress)
                .setDownloadingProgressBarId(R.id.progress)
                .setRetrofitDownloadAdapter(new RetrofitDownloadAdapter() {
                    @Override public void onDownloading(int code, String message, long fileTotalSize, long fileSizeDownloaded) {
                        Log.d(TAG, "onDownloading file download: " + fileSizeDownloaded + " of " + fileTotalSize + "," + code + "," + message);

                    }

                    @Override public void onDownloadSuccess(int code, String message) {
                        Log.e(TAG, "onDownloadSuccess:" + code + "," + message);

                        mImg.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + File.separator + "beauty.jpg"));
                    }

                    @Override public void onDownloadFailure(int code, String message) {
                        Log.e(TAG, "onDownloadFailure:" + code + "," + message);
                    }

                    @Override public void onDownloadError(Throwable t) {
                        Log.e(TAG, "onDownloadError:" + t);
                    }
                })
                .build();
        RetrofitDownloadManager retrofitDownloadManager = new RetrofitDownloadManager(retrofitDownloadConfig);
        retrofitDownloadManager.downloadFile(url);
    }

    private void startDownloadApk(String url) {
        RetrofitDownloadConfig retrofitDownloadConfig = new RetrofitDownloadConfig.Builder(this)
                .setSavingFile(getSavingFile())
                .setNotification(getNotification())
                .setRemoteView(getRemoteView())
                .setDownloadingTip("正在下载...")
                .setDownloadSuccessTip("下载成功")
                .setDownloadFailureTip("下载失败")
                .setDownloadCompleteTxtId(R.id.tv_txt)
                .setDownloadingProgressTxtId(R.id.tv_progress)
                .setDownloadingProgressBarId(R.id.progress)
                .setAutoInstallApk(true)
                .setRetrofitDownloadAdapter(new RetrofitDownloadAdapter() {
                    @Override public void onDownloading(int code, String message, long fileTotalSize, long fileSizeDownloaded) {
                        Log.d(TAG, "onDownloading file download: " + fileSizeDownloaded + " of " + fileTotalSize + "," + code + "," + message);

                    }

                    @Override public void onDownloadSuccess(int code, String message) {
                        Log.e(TAG, "onDownloadSuccess:" + code + "," + message);
                    }

                    @Override public void onDownloadFailure(int code, String message) {
                        Log.e(TAG, "onDownloadFailure:" + code + "," + message);
                    }

                    @Override public void onDownloadError(Throwable t) {
                        Log.e(TAG, "onDownloadError:" + t);
                    }
                })
                .build();
        RetrofitDownloadManager retrofitDownloadManager = new RetrofitDownloadManager(retrofitDownloadConfig);
        retrofitDownloadManager.downloadFile(url);
    }

    private File getImgFile() {
        Log.e(TAG, "路径：" + Environment.getExternalStorageDirectory() + File.separator + "beauty.jpg");
        return new File(Environment.getExternalStorageDirectory() + File.separator + "beauty.jpg");
    }

    private File getSavingFile() {
        return new File(Environment.getExternalStorageDirectory() + File.separator + "chefenxiang.apk");
    }

    private RemoteViews getRemoteView() {
        RemoteViews remoteView = new RemoteViews(this.getApplicationContext().getPackageName(), R.layout.layout_notification_update);
        remoteView.setProgressBar(R.id.progress, 100, 0, false);
        remoteView.setTextViewText(R.id.tv_progress, "0%");
        return remoteView;
    }

    private Notification getNotification() {
        Intent updateIntent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(this.getApplicationContext(), 0,
                updateIntent, 0);
        Notification notification = new Notification(R.mipmap.ic_launcher, "正在下载...", System.currentTimeMillis());
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notification.contentIntent = pendingIntent;
        notification.contentView = getRemoteView();
        return notification;
    }

}
