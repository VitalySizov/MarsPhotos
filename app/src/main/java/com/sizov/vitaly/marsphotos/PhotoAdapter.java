package com.sizov.vitaly.marsphotos;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    private ArrayList<Photo.LatestPhotosBean> mLatestPhotosBeans;
    private Context mContext;

    public PhotoAdapter(ArrayList<Photo.LatestPhotosBean> latestPhotosBeans, Context context) {
        mLatestPhotosBeans = latestPhotosBeans;
        mContext = context;
    }

    @NonNull
    @Override
    public PhotoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_objects, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoAdapter.ViewHolder viewHolder, int position) {
        Photo.LatestPhotosBean photosBean = mLatestPhotosBeans.get(position);

        Uri uri = Uri.parse(photosBean.getImgSrc());
        Picasso.get().load(uri).into(viewHolder.mPhoto);
        viewHolder.mIdPhotoText.setText("id photo : " + String.valueOf(photosBean.getIdPhoto()));

        viewHolder.itemView.setOnClickListener(view -> {
            showFullPhoto(photosBean);
        });
    }

    private void showFullPhoto(Photo.LatestPhotosBean photosBean) {
        Intent intent = new Intent(mContext, PhotoDetailsActivity.class);
        intent.putExtra("EXTRA_IMG_SRC", photosBean.getImgSrc());
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if (mLatestPhotosBeans != null) {
            return mLatestPhotosBeans.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mPhoto;
        private TextView mIdPhotoText;
        private CardView mCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mIdPhotoText = itemView.findViewById(R.id.tv_id_photo);
            mPhoto = itemView.findViewById(R.id.iv_photo);
            mCardView = itemView.findViewById(R.id.card_view);

            mCardView.setOnLongClickListener(view -> {
                mLatestPhotosBeans.remove(getAdapterPosition());
                notifyDataSetChanged();
                notifyItemRangeChanged(getAdapterPosition(), mLatestPhotosBeans.size());
                return false;
            });
        }
    }
}
