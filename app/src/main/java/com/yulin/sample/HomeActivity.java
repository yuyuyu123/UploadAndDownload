package com.yulin.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yulin.sample.download.DownloadActivity;
import com.yulin.sample.upload.UploadActivity;

/**
 * Created by YuLin on 2016/12/29 0029.
 */
public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void upload(View v) {
        Intent intent = new Intent(this, UploadActivity.class);
        startActivity(intent);

    }

    public void download(View v) {
        Intent intent = new Intent(this, DownloadActivity.class);
        startActivity(intent);
    }
}
