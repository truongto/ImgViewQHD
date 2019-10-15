package com.example.imgviewqhd;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
String img;
private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        imageView=findViewById(R.id.imgImageDetail);
GetItem();
    }
    private void GetItem() {
        img = getIntent().getStringExtra("img");
        Picasso.get().load(img).into(imageView);
    }
}
