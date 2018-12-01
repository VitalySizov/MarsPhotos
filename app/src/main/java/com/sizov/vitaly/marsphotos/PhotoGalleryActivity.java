package com.sizov.vitaly.marsphotos;

import android.support.v4.app.Fragment;

public class PhotoGalleryActivity extends SingleFragmentActivity {

    public static final String TAG = PhotoGalleryActivity.class.getSimpleName();

    @Override
    protected Fragment createFragment() {
        return PhotoGalleryFragment.newInstance();
    }
}
