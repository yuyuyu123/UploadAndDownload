package com.yulin.sample;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 上传图片返回信息
 *
 * Created by YuLin on 2016/12/6 0006.
 */
public class PhotoBean implements Parcelable {
    String code;
    String data;
    List<String> list;
    String message;
    boolean state;
    boolean success;
    String totalSize;
    String url;

    @Override
    public String toString() {
        return "PhotoBean{" +
                "code='" + code + '\'' +
                ", data='" + data + '\'' +
                ", list=" + list +
                ", message='" + message + '\'' +
                ", state=" + state +
                ", success=" + success +
                ", totalSize='" + totalSize + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(String totalSize) {
        this.totalSize = totalSize;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeString(this.data);
        dest.writeStringList(this.list);
        dest.writeString(this.message);
        dest.writeByte(this.state ? (byte) 1 : (byte) 0);
        dest.writeByte(this.success ? (byte) 1 : (byte) 0);
        dest.writeString(this.totalSize);
        dest.writeString(this.url);
    }

    public PhotoBean() {
    }

    protected PhotoBean(Parcel in) {
        this.code = in.readString();
        this.data = in.readString();
        this.list = in.createStringArrayList();
        this.message = in.readString();
        this.state = in.readByte() != 0;
        this.success = in.readByte() != 0;
        this.totalSize = in.readString();
        this.url = in.readString();
    }

    public static final Creator<PhotoBean> CREATOR = new Creator<PhotoBean>() {
        @Override
        public PhotoBean createFromParcel(Parcel source) {
            return new PhotoBean(source);
        }

        @Override
        public PhotoBean[] newArray(int size) {
            return new PhotoBean[size];
        }
    };
}
