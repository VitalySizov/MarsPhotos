package com.sizov.vitaly.marsphotos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Photo implements Parcelable {

    @SerializedName("latest_photos")
    private ArrayList<LatestPhotosBean> mLatestPhotos;

    private ArrayList<Photo> mPhotos;

    protected Photo(Parcel in) {
        mPhotos = in.createTypedArrayList(Photo.CREATOR);
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    public ArrayList<LatestPhotosBean> getLatestPhotos() {
        return mLatestPhotos;
    }

    public void setLatestPhotos(ArrayList<LatestPhotosBean> latestPhotos) {
        mLatestPhotos = latestPhotos;
    }

    public ArrayList<LatestPhotosBean> getLastTwentyPhotos() {

        ArrayList<LatestPhotosBean> latestPhotos = getLatestPhotos();
        Collections.reverse(latestPhotos);

        if (latestPhotos.size() >= 20) {
            List<LatestPhotosBean> twentyElements = latestPhotos.subList(0,20);
            ArrayList<LatestPhotosBean> finalTwentyElements = new ArrayList<>(twentyElements.size());
            finalTwentyElements.addAll(twentyElements);
            return finalTwentyElements;
        }

        return latestPhotos;
    }

    public List<Photo> getPhotos() {
        return mPhotos;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(mPhotos);
    }

    public static class LatestPhotosBean implements Parcelable {

        private int mId;

        @SerializedName("id")
        private int mIdPhoto;

        @SerializedName("sol")
        private int mSol;

        @SerializedName("img_src")
        private String mImgSrc;

        @SerializedName("earth_date")
        private String mEarthDate;

        public LatestPhotosBean(int mId, int idPhoto, int sol, String imgSrc, String earthDate) {
            mId = mId;
            mIdPhoto = idPhoto;
            mSol = sol;
            mImgSrc = imgSrc;
            mEarthDate = earthDate;
        }

        protected LatestPhotosBean(Parcel in) {
            mId = in.readInt();
            mIdPhoto = in.readInt();
            mSol = in.readInt();
            mImgSrc = in.readString();
            mEarthDate = in.readString();
        }

        public static final Creator<LatestPhotosBean> CREATOR = new Creator<LatestPhotosBean>() {
            @Override
            public LatestPhotosBean createFromParcel(Parcel in) {
                return new LatestPhotosBean(in);
            }

            @Override
            public LatestPhotosBean[] newArray(int size) {
                return new LatestPhotosBean[size];
            }
        };

        public int getIdPhoto() {
            return mIdPhoto;
        }

        public void setIdPhoto(int idPhoto) {
            mIdPhoto = idPhoto;
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(mId);
            parcel.writeInt(mIdPhoto);
            parcel.writeInt(mSol);
            parcel.writeString(mImgSrc);
            parcel.writeString(mEarthDate);
        }
    }
}


