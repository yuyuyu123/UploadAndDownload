# UploadAndDownload
An upload and download library based on retrofit2 and okhttp3.

#Effects:
1.Download a file:

   ![image](https://github.com/yuyuyu123/UploadAndDownload/blob/master/screenshot1.gif)

2.Upload a file:

   ![image](https://github.com/yuyuyu123/UploadAndDownload/blob/master/screenshot2.gif)


How to download?For example:download an apk file:

    private String strApk = "http://oss-ccclubs.oss-cn-hangzhou.aliyuncs.com/app/20161208174314986.apk";
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
    
    
How to upload a file?For example:upload an image file:

    private String url = "http://pic.test.com/";//your file server address
    private String nameKey = "file";
    private void uploadFile(File file) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("key", "5fcfe94a91b1d2ae08867a4f3c55455c");
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
    
    
