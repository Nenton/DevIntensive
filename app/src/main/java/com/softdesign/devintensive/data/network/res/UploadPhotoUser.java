package com.softdesign.devintensive.data.network.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadPhotoUser {
    @SerializedName("success")
    @Expose
    public Boolean success;
    @SerializedName("data")
    @Expose
    public Data data;

    public class Data {

        @SerializedName("photo")
        @Expose
        public String photo;
        @SerializedName("updated")
        @Expose
        public String updated;
    }
}
