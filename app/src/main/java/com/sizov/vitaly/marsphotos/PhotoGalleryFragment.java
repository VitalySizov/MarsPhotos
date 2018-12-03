package com.sizov.vitaly.marsphotos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sizov.vitaly.marsphotos.api.ApiUtils;
import com.sizov.vitaly.marsphotos.database.PhotoDbHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotoGalleryFragment extends Fragment {

    public static final String TAG = PhotoGalleryFragment.class.getSimpleName();
    private static final String KEY_OBJECTS = "photo";
    private ArrayList<Photo.LatestPhotosBean> photos;
    private PhotoDbHelper mPhotoDbHelper;
    private Photo photo;
    private Photo.LatestPhotosBean mLatestPhotosBean;
    private List<Photo.LatestPhotosBean> mLatestPhotosBeanList;
    private RecyclerView mRecyclerView;
    private TextView mErrorTextView;
    private ImageView mErrorImageView;
    private Button mButtonRetry;
    private ProgressBar mProgressBar;

    private View.OnClickListener mOnRetryListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isOnline()) {
                mButtonRetry.setVisibility(View.INVISIBLE);
                mErrorImageView.setVisibility(View.GONE);
                mErrorTextView.setVisibility(View.INVISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);
                getPhotos();
            } else {
                mErrorTextView.setVisibility(View.VISIBLE);
                mButtonRetry.setVisibility(View.VISIBLE);
                mErrorTextView.setText(R.string.no_connect_to_internet);
                mErrorImageView.setVisibility(View.VISIBLE);
                Snackbar snackbar = Snackbar
                        .make(getView(), "Загрузить сохраненные фото ?", Snackbar.LENGTH_LONG)
                        .setAction("Да", view1 -> exportDataFromDb());

                snackbar.setActionTextColor(Color.GREEN);
                snackbar.show();
            }
        }
    };

    public static PhotoGalleryFragment newInstance() {
        return new PhotoGalleryFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: ");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        getPhotos();

        setRetainInstance(true);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(KEY_OBJECTS, photos);
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");

        if (photos != null) {
            setupAdapter();
        } else {
            Log.d(TAG, "No items in List");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_photo_gallery, container, false);

        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_download);

        mErrorImageView = (ImageView) view.findViewById(R.id.image_error);
        mErrorTextView = (TextView) view.findViewById(R.id.tv_error);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mButtonRetry = (Button) view.findViewById(R.id.button_retry);
        mButtonRetry.setOnClickListener(mOnRetryListener);

        if (isOnline()) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mErrorImageView.setVisibility(View.VISIBLE);
            mButtonRetry.setVisibility(View.VISIBLE);
            mErrorTextView.setVisibility(View.VISIBLE);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        Log.d(TAG, "onCreateView: ");

        return view;
    }

    public void setupAdapter() {
        PhotoAdapter photoAdapter = new PhotoAdapter(photos, getContext());
        mRecyclerView.setAdapter(photoAdapter);
        photoAdapter.notifyDataSetChanged();

        mErrorImageView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mButtonRetry.setVisibility(View.INVISIBLE);
        mErrorTextView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: ");

        getPhotos();
    }

    private void getPhotos() {

        if (isOnline() && photos == null) {
            ApiUtils.getApi().getLatestPhotos().enqueue(new Callback<Photo>() {

                Handler mHandler = new Handler(getActivity().getMainLooper());

                @Override
                public void onResponse(Call<Photo> call, final Response<Photo> response) {
                    mHandler.post(() -> {
                        if (!response.isSuccessful()) {
                            showMessage(R.string.connect_error);
                        } else {

                            mErrorImageView.setVisibility(View.INVISIBLE);
                            mErrorTextView.setVisibility(View.INVISIBLE);
                            mButtonRetry.setVisibility(View.INVISIBLE);

                            photo = response.body();
                            photos = photo.getLastTwentyPhotos();
                            setupAdapter();
                            mProgressBar.setVisibility(View.INVISIBLE);
                            saveDbData();
                        }
                    });
                }

                @Override
                public void onFailure(Call<Photo> call, Throwable t) {
                    mHandler.post(() -> {
                        showMessage(R.string.request_error);
                    });
                }
            });
        }
    }

    private void saveDbData() {

        mPhotoDbHelper = new PhotoDbHelper(getContext());
        SQLiteDatabase database = mPhotoDbHelper.getWritableDatabase();

        database.delete(mPhotoDbHelper.TABLE_OBJECTS, null, null);

        ContentValues contentValues = new ContentValues();

        for (int i = 0; photos.size() > i; i++) {
            contentValues.put(mPhotoDbHelper.KEY_ID, i);
            contentValues.put(mPhotoDbHelper.KEY_ID_PHOTO, String.valueOf(photos.get(i).getIdPhoto()));
            contentValues.put(mPhotoDbHelper.KEY_SOL, String.valueOf(photos.get(i).getSol()));
            contentValues.put(mPhotoDbHelper.KEY_IMG_SRC, String.valueOf(photos.get(i).getImgSrc()));
            contentValues.put(mPhotoDbHelper.KEY_EARTH_DATE, String.valueOf(photos.get(i).getEarthDate()));

            database.insert(mPhotoDbHelper.TABLE_OBJECTS, null, contentValues);
        }

        viewElementsDb();
    }

    private void exportDataFromDb() {

        mPhotoDbHelper = new PhotoDbHelper(getContext());
        Cursor cursor = mPhotoDbHelper.getAll();

        if (!cursor.moveToNext()) {
            mErrorTextView.setText(R.string.first_start);
        } else {
            mErrorImageView.setVisibility(View.INVISIBLE);
            mErrorTextView.setVisibility(View.INVISIBLE);
            mButtonRetry.setVisibility(View.INVISIBLE);
        }

        if (cursor.moveToFirst()) {

            int idIndex = cursor.getColumnIndex(mPhotoDbHelper.KEY_ID);
            int idPhotoIndex = cursor.getColumnIndex(mPhotoDbHelper.KEY_ID_PHOTO);
            int idSolIndex = cursor.getColumnIndex(mPhotoDbHelper.KEY_SOL);
            int idImgSrcIndex = cursor.getColumnIndex(mPhotoDbHelper.KEY_IMG_SRC);
            int idEarthDateIndex = cursor.getColumnIndex(mPhotoDbHelper.KEY_EARTH_DATE);

            photos = new ArrayList<>();

            do {
                Photo.LatestPhotosBean photosBean = new Photo.LatestPhotosBean(
                        cursor.getInt(idIndex),
                        cursor.getInt(idPhotoIndex),
                        cursor.getInt(idSolIndex),
                        cursor.getString(idImgSrcIndex),
                        cursor.getString(idEarthDateIndex)
                );
                photos.add(photosBean);

            } while (cursor.moveToNext());
        }

        cursor.close();

        if (photos != null) {
            setupAdapter();
        }
    }

    private void viewElementsDb() {
        mPhotoDbHelper = new PhotoDbHelper(getContext());
        Cursor cursor = mPhotoDbHelper.getAll();

        if (!cursor.moveToNext()) {
            Toast.makeText(getContext(), R.string.first_start, Toast.LENGTH_LONG).show();
        }

        if (cursor.moveToFirst()) {

            int idIndex = cursor.getColumnIndex(mPhotoDbHelper.KEY_ID);
            int idPhoto = cursor.getColumnIndex(mPhotoDbHelper.KEY_ID_PHOTO);
            int solIndex = cursor.getColumnIndex(mPhotoDbHelper.KEY_SOL);
            int imgSrcIndex = cursor.getColumnIndex(mPhotoDbHelper.KEY_IMG_SRC);
            int earthDateIndex = cursor.getColumnIndex(mPhotoDbHelper.KEY_EARTH_DATE);

            do {
                Log.d(TAG, "ID = " + cursor.getInt(idIndex)
                        + " | ID PHOTO = " + cursor.getInt(idPhoto)
                        + " | SOL = " + cursor.getInt(solIndex)
                        + " | IMG SRC = " + cursor.getString(imgSrcIndex)
                        + " | EARTH DATE = " + cursor.getString(earthDateIndex)
                );
            } while (cursor.moveToNext());
        } else
            Log.d(TAG, "0 rows");

        cursor.close();
    }

    private void showMessage(@StringRes int string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
    }

    // Checking Internet connection
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
