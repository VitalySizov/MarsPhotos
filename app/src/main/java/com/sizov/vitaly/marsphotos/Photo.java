package com.sizov.vitaly.marsphotos;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

public class Photo {

    @SerializedName("latest_photos")
    private List<LatestPhotosBean> mLatestPhotos;

    private List<Photo> mPhotos;

    public List<LatestPhotosBean> getLatestPhotos() {
        return mLatestPhotos;
    }

    public void setLatestPhotos(List<LatestPhotosBean> latestPhotos) {
        mLatestPhotos = latestPhotos;
    }

    public List<LatestPhotosBean> getLastTwentyPhotos() {

        List<LatestPhotosBean> latestPhotos = getLatestPhotos();
        Collections.reverse(latestPhotos);

        if (latestPhotos.size() > 20) {
            List<LatestPhotosBean> finalArray = latestPhotos.subList(0, 20);
            return finalArray;
        }

        return latestPhotos;
    }

    public List<Photo> getPhotos() {
        return mPhotos;
    }

    public static class LatestPhotosBean {

        @SerializedName("id")
        private int mId;

        @SerializedName("sol")
        private int mSol;

        @SerializedName("img_src")
        private String mImgSrc;

        @SerializedName("earth_date")
        private String mEarthDate;

        public int getId() {
            return mId;
        }

        public void setId(int id) {
            mId = id;
        }

        public int getSol() {
            return mSol;
        }

        public void setSol(int sol) {
            mSol = sol;
        }

        public String getImgSrc() {
            return mImgSrc;
        }

        public void setImgSrc(String imgSrc) {
            mImgSrc = imgSrc;
        }

        public String getEarthDate() {
            return mEarthDate;
        }

        public void setEarthDate(String earthDate) {
            mEarthDate = earthDate;
        }
    }
}


