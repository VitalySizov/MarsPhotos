package com.sizov.vitaly.marsphotos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

public class PhotoDetailsFragment extends Fragment {

    private String mImgSrc;
    private PhotoView mImageView;

    public static PhotoDetailsFragment newInstance() {
        return new PhotoDetailsFragment();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getActivity().getIntent();
        mImgSrc = intent.getStringExtra("EXTRA_IMG_SRC");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_details, container, false);

        mImageView = (PhotoView) view.findViewById(R.id.photo_details);

        Uri uri = Uri.parse(mImgSrc);
        Picasso.get().load(uri).into(mImageView);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
