package com.sizov.vitaly.marsphotos;

import android.support.v4.app.Fragment;

public class PhotoDetailsActivity extends SingleFragmentActivity {

    public static final String TAG = PhotoDetailsFragment.class.getSimpleName();

    @Override
    protected Fragment createFragment() {
        return PhotoDetailsFragment.newInstance();
    }
}
