package com.example.imgviewqhd;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.imgviewqhd.interfaceOnClick.OnClickListener;
import com.example.imgviewqhd.model.Photo;

import java.util.List;

public class ImgAdapter extends RecyclerView.Adapter<ImgAdapter.Holder> {
    List<Photo> photoList;
    Context context;
    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public ImgAdapter(Context context, List<Photo> imgList) {
        this.context = context;
        this.photoList = imgList;
    }


    @NonNull
    @Override
    public ImgAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.img_adapter, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImgAdapter.Holder holder, final int position) {
        Photo photo = photoList.get(position);
        Glide.with(context).load(photo.getUrlZ()).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onClickListener(photoList.get(position));

            }
        });
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public Holder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgadapter);
        }
    }
}
