package com.example.imgviewqhd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.RenderScript;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.example.imgviewqhd.interfaceOnClick.OnClickListener;
import com.example.imgviewqhd.model.Filckr;
import com.example.imgviewqhd.model.Photo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Photo> imgList;
    ImgAdapter imgAdapter;
    private int page = 1;
    SwipeRefreshLayout swipeRefreshLayout;
//    private GridLayoutManager gridLayoutManager;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeRefreshLayout = findViewById(R.id.swipe);

        recyclerView = findViewById(R.id.recy);
        imgList = new ArrayList<>();
        imgAdapter = new ImgAdapter(this, imgList);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL);
        recyclerView.setAdapter(imgAdapter);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        loadImg(page);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                MainActivity.this.page = 1;
                imgList.clear();
                loadImg(MainActivity.this.page);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerViewScrolling(staggeredGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                MainActivity.this.page++;
                loadImg(MainActivity.this.page++);
            }
        });
       imgAdapter.setOnClickListener(new OnClickListener() {
           @Override
           public void onClickListener(Photo photo) {
               Intent intent=new Intent(MainActivity.this,DetailActivity.class);
               intent.putExtra("img",photo.getUrlC());
               startActivity(intent);
           }
       });
    }


    public void loadImg(int page) {
        AndroidNetworking.post("https://www.flickr.com/services/rest/?")
                .addBodyParameter("method", "flickr.favorites.getList")
                .addBodyParameter("api_key", "e86043819b51fd79fb6f169b632e14e7")
                .addBodyParameter("user_id", "184515172@N07")
                .addBodyParameter("format", "json")
                .addBodyParameter("nojsoncallback", "1")
                .addBodyParameter("extras", "views,media,path_alias,url_sq,url_t,url_s,url_q,url_m,url_n,url_z,url_c,url_l,url_o")
                .addBodyParameter("per_page", "10")
                .addBodyParameter("page", String.valueOf(page))
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(Filckr.class, new ParsedRequestListener() {
                    @Override
                    public void onResponse(Object response) {
                        swipeRefreshLayout.setRefreshing(false);
                        Filckr filckr = (Filckr) response;
                        List<Photo> photos = filckr.getPhotos().getPhoto();
                        MainActivity.this.imgList.addAll(photos);
                        imgAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

    }
}
