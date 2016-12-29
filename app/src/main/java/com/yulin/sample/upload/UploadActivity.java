package com.yulin.sample.upload;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yulin.download_upload.upload.RetrofitUploadAdapter;
import com.yulin.download_upload.upload.RetrofitUploadConfig;
import com.yulin.download_upload.upload.RetrofitUploadManager;
import com.yulin.sample.PhotoBean;
import com.yulin.sample.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
/**
 * Created by YuLin on 2016/12/29 0029.
 */
public class UploadActivity extends AppCompatActivity implements
        ActivityCompat.OnRequestPermissionsResultCallback {

    private TextView mTxtPath;
    private TextView mUploading;
    private ImageView mImg;
    private CheckBox mCb;

    private String mPath;
    private boolean isTakePhoto = false;
    private boolean isGetPic = false;

    private static final int PERMISSIONS_REQUEST_PHOTO = 0x01;
    private static final int PERMISSIONS_REQUEST_FILE = 0x02;

    private static final int REQUEST_CODE_TAKING_PHOTO = 0x03;
    private static final int REQUEST_CODE_SELECT_PHOTO_FROM_LOCAL = 0x04;
    private static final int REQUEST_CODE_CUT_PHOTO = 0x05;

    private static final int TARGET_HEAD_SIZE = 150;
    private static final String IMAGE_SAVE_DIR = Environment.getExternalStorageDirectory().getPath() + "/yulin/photo";
    private static final String IMAGE_SAVE_PATH = IMAGE_SAVE_DIR + "/demo.jpg";

    private Uri mUri;

    private CharSequence[] mItems = {"拍照上传", "选择图片"};

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("Path", mPath);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        init();
        if (savedInstanceState != null) mPath = savedInstanceState.getString("Path");

    }

    private void init() {
        mImg = (ImageView) this.findViewById(R.id.img);
        mTxtPath = (TextView) this.findViewById(R.id.path);
        mUploading = (TextView) this.findViewById(R.id.upload);
        mCb = (CheckBox) this.findViewById(R.id.cb);
        mImg.setOnClickListener(v -> showPhotoDialog());

        File file = new File(IMAGE_SAVE_DIR);
        if (!file.exists()) file.mkdirs();

    }

    /**
     * 弹出操作选择对话框
     */
    private void showPhotoDialog() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setItems(mItems, (dialog, which) -> {
                    if (which == 0) operTakePhoto();
                    else if (which == 1) operChoosePic();
                }).create().show();
    }

    /**
     * 拍照操作
     */
    private void operTakePhoto() {
        isTakePhoto = true;
        isGetPic = false;
        if (ContextCompat.checkSelfPermission(UploadActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(UploadActivity.this, Manifest.permission.CAMERA))
                showPhotoPerDialog();
            else
                ActivityCompat.requestPermissions(UploadActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_PHOTO);
        } else takePhoto();
    }

    /**
     * 选择图片操作
     */
    private void operChoosePic() {
        isTakePhoto = false;
        isGetPic = true;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                showFilePerDialog();
            else
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_FILE);
        } else getPictureFromLocal();
    }

    /**
     * 拍照权限提示
     */
    private void showPhotoPerDialog() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("PhotoDemo需要获取访问您的相机权限，以确保您可以正常拍照。")
                .setPositiveButton("确定", (dialog, which) -> ActivityCompat.requestPermissions(UploadActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_PHOTO)).create().show();
    }

    /**
     * 文件权限提示
     */
    private void showFilePerDialog() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("PhotoDemo需要获取存储文件权限，以确保可以正常保存拍摄或选取的图片。")
                .setPositiveButton("确定", (dialog, which) -> ActivityCompat.requestPermissions(UploadActivity.this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, PERMISSIONS_REQUEST_FILE)).create().show();
    }

    /**
     * 拍照
     */
    private void takePhoto() {
        String mUUID = UUID.randomUUID().toString();
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        mPath = FileUtils.getStorageDirectory() + mUUID;
        File file = new File(mPath + ".jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, REQUEST_CODE_TAKING_PHOTO);
    }

    /**
     * 从本地选择图片
     */
    private void getPictureFromLocal() {
        Intent innerIntent =
                new Intent(Intent.ACTION_GET_CONTENT);
        innerIntent.setType("image/*");
        Intent wrapperIntent = Intent.createChooser(innerIntent, null);
        startActivityForResult(wrapperIntent, REQUEST_CODE_SELECT_PHOTO_FROM_LOCAL);
    }

    /**
     * 剪裁图片
     */
    private void startPhotoZoom(Uri uri, int size) {
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");
            // crop为true是设置在开启的intent中设置显示的view可以剪裁
            intent.putExtra("crop", "true");
            // aspectX aspectY 是宽高的比例
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);

            // outputX,outputY 是剪裁图片的宽高
            intent.putExtra("outputX", size);
            intent.putExtra("outputY", size);
            //   intent.putExtra("return-data", true);
            mUri = Uri.parse("file:///" + IMAGE_SAVE_PATH);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            startActivityForResult(intent, REQUEST_CODE_CUT_PHOTO);
        } catch (ActivityNotFoundException e) {
            String errorMessage = "Your device doesn't support the crop action!";
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理拍照结果
     */
    private void dealTakePhotoWithoutZoom() {
        String finalPath = mPath + ".jpg";
        Bitmap bitmap = BitmapFactory.decodeFile(finalPath);
        if (bitmap != null) {
            boolean b = BitmapUtils.compressBitmap2file(bitmap, IMAGE_SAVE_PATH);
            if (b) {
                mImg.setImageBitmap(BitmapFactory.decodeFile(IMAGE_SAVE_PATH));
                mTxtPath.setText("图片路径：" + IMAGE_SAVE_PATH);
                mUploading.setText("Uploading...");
                uploadFile(new File(IMAGE_SAVE_PATH));
            }
        }
    }

    /**
     * 处理拍照并剪裁
     */
    private void dealTakePhotoThenZoom() {
        startPhotoZoom(Uri.fromFile(new File(mPath + ".jpg")), TARGET_HEAD_SIZE);
    }

    /**
     * 处理选择图片结果
     */
    private void dealChoosePhotoWithoutZoom(Intent data) {
        Uri uri = data.getData();
        if (uri != null) {
            Bitmap bitmap = BitmapUtils.uriToBitmap(this, uri);
            if (bitmap != null) {
                mImg.setImageBitmap(bitmap);
                boolean b = BitmapUtils.compressBitmap2file(bitmap, IMAGE_SAVE_PATH);
                if (b) {
                    mTxtPath.setText("图片地址：" + IMAGE_SAVE_PATH);
                    mUploading.setText("Uploading...");
                    uploadFile(new File(IMAGE_SAVE_PATH));
                }
            }
        }
    }

    /**
     * 处理选择图片并剪裁
     */
    private void dealChoosePhotoThenZoom(Intent data) {
        if (mCb.isChecked()) {
            Uri uri = data.getData();
            InputStream inputStream = null;
            try {
                inputStream = getContentResolver().openInputStream(uri);
                if (inputStream != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    if (bitmap != null) BitmapUtils.compressBitmap2file(bitmap, IMAGE_SAVE_PATH);
                }
                startPhotoZoom(Uri.fromFile(new File(IMAGE_SAVE_PATH)), TARGET_HEAD_SIZE);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (inputStream != null) inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 处理剪裁图片的结果
     */
    private void dealZoomPhoto() {
        try {
            if (mUri != null) {
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(mUri));
                if (bitmap != null) {
                    mImg.setImageBitmap(bitmap);
                    boolean b = BitmapUtils.compressBitmap2file(bitmap,IMAGE_SAVE_PATH);
                    if(b) {
                        mTxtPath.setText("图片地址:" + IMAGE_SAVE_PATH);
                        mUploading.setText("Uploading...");
                        uploadFile(new File(IMAGE_SAVE_PATH));
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 权限申请回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_PHOTO: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (isTakePhoto) takePhoto();
                    if (isGetPic) getPictureFromLocal();
                }
            }
            case PERMISSIONS_REQUEST_FILE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    dealZoomPhoto();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_TAKING_PHOTO: // 拍照的结果
                    if (mCb.isChecked()) dealTakePhotoThenZoom();
                    else dealTakePhotoWithoutZoom();
                    break;
                case REQUEST_CODE_SELECT_PHOTO_FROM_LOCAL://选择图片的结果
                    if (mCb.isChecked()) dealChoosePhotoThenZoom(data);
                    else dealChoosePhotoWithoutZoom(data);
                    break;
                case REQUEST_CODE_CUT_PHOTO: // 剪裁图片的结果
                    dealZoomPhoto();
                    break;
            }
        }
    }

    private String url = "http://static.ccclubs.com/upload/up.do";//你的文件服务器地址
    private String nameKey = "file";
    private void uploadFile(File file) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("key", "5fcfe94a91b1d2ae08867a4f3c55455c");//跟在URL后面的参数(如果有的话)
        RetrofitUploadConfig retrofitUploadConfig = new RetrofitUploadConfig.Builder(this)
                .setUploadUrl(url)
                .setParamsMap(paramMap)
                .setFileKey(nameKey)
                .setDescriptionString("this is uploading test file")
                .setRetrofitUploadAdapter(new RetrofitUploadAdapter<PhotoBean>() {

                    @Override public void onUploadSuccess(int code, PhotoBean photoBean) {
                        if (photoBean != null && !TextUtils.isEmpty(photoBean.getUrl())) {
                            mUploading.setText("Upload Success");
                        }
                        else {
                            mUploading.setText("Upload Failure");
                        }
                    }

                    @Override public void onUploadFailure(int code, String message) {
                        mUploading.setText("Upload Failure");
                    }

                    @Override public void onUploadError(Throwable t) {
                        mUploading.setText("Upload Error");
                    }
                }).build();

        RetrofitUploadManager retrofitUploadManager = new RetrofitUploadManager(retrofitUploadConfig);
        retrofitUploadManager.uploadFile(file);
    }
}
