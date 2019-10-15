package com.example.imgviewqhd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.imgviewqhd.model.Photo;

import java.util.List;

public class ImgAdapter extends RecyclerView.Adapter<ImgAdapter.Holder> {
    List<Photo> photoList;
    Context context;

    public ImgAdapter(Context context, List<Photo> imgList) {
        this.context=context;
      this.photoList=imgList;
    }


    @NonNull
    @Override
    public ImgAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.img_adapter, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImgAdapter.Holder holder, int position) {
        Photo photo = photoList.get(position);
        Glide.with(context).load(photo.getUrlS()).into(holder.imageView)
                ;
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public Holder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imgadapter);
        }
    }
}
