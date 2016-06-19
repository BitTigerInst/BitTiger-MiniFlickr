package com.android.bittiger.janescookies.tinyflickr;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by xicheng on 16/6/16.
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private Context mContext;   // get resource of this component

//    private List<GalleryItem> mList;
    private List<Contact> mList;

    public GalleryAdapter(Context mContext, List<Contact> mList) {
//    public GalleryAdapter(Context mContext, List<GalleryItem> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;

        public TextView nameTextView;


        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.gallery_item);

            nameTextView = (TextView) itemView.findViewById(R.id.contact_item);

            // for test, use me.jpg from drawable/
            mImageView.setImageResource(R.drawable.me);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_gallery,
                parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        Contact item = mList.get(position);
        TextView nameTextView = holder.nameTextView;
        nameTextView.setText(item.getName());

        ImageView nameImageView = holder.mImageView;

        nameImageView.setImageResource(R.drawable.dog);



    }
    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void addAll(List<Contact> newList) {
        mList.addAll(newList);
    }

    public void clear() {
        mList.clear();
    }

}
