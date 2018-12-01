package com.sizov.vitaly.marsphotos;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotoGalleryFragment extends Fragment {

    public static final String TAG = PhotoGalleryFragment.class.getSimpleName();

    public static PhotoGalleryFragment newInstance() {
        return new PhotoGalleryFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_gallery, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ApiUtils.getApi().getLatestPhotos().enqueue(new Callback<Photo>() {

            Handler mHandler = new Handler(getActivity().getMainLooper());

            @Override
            public void onResponse(Call<Photo> call, final Response<Photo> response) {
                mHandler.post(() -> {
                    if (!response.isSuccessful()) {
                        showMessage(R.string.connect_error);
                    } else {
                        Photo photosBean = response.body();
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

    private void showMessage(@StringRes int string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
    }
}
