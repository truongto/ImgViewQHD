package com.example.imgviewqhd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DetailActivity extends AppCompatActivity {
    String img;
    private ImageView imageView;
    private FloatingActionMenu floatingActionMenu;
    FloatingActionButton favourite, SetAS, SaveImg, Share;
    int width, height;
    private DisplayMetrics displayMetrics;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        imageView = findViewById(R.id.imgImageDetail);
        floatingActionMenu = findViewById(R.id.floatingActionMenu);
        favourite = findViewById(R.id.fbtnLike);
        Share = findViewById(R.id.fbtnShare);
        SetAS = findViewById(R.id.fbtnSetAs);
        SaveImg = findViewById(R.id.fbtnSave);

        Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, String.valueOf(imageView.getDrawable()));
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
        SetAS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetScreenWidthHeight();
                final Bitmap bitmapImg = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                final WallpaperManager wallManager = WallpaperManager.getInstance(getApplicationContext());
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        wallManager.setBitmap(bitmapImg, null, true, WallpaperManager.FLAG_SYSTEM);

                    } else {
                        wallManager.setBitmap(bitmapImg);

                    }
                    Toast.makeText(DetailActivity.this, "Đã cài làm ảnh nền", Toast.LENGTH_SHORT).show();
                } catch (IOException ex) {

                }

            }
        });

        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DetailActivity.this, "Favorites", Toast.LENGTH_SHORT).show();
            }
        });
        SaveImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DownloadFileFromURL(DetailActivity.this).execute(img);
            }
        });

        GetItem();
    }

    public void GetScreenWidthHeight() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
            displayMetrics = new DisplayMetrics();
            width = displayMetrics.widthPixels;
            height = displayMetrics.heightPixels;
        } else {
            displayMetrics = new DisplayMetrics();
            Point size = new Point();
            WindowManager windowManager = getWindowManager();
            windowManager.getDefaultDisplay().getSize(size);
            width = size.x;
            height = size.y;
        }
    }

    private void GetItem() {
        img = getIntent().getStringExtra("img");
        Picasso.get().load(img).into(imageView);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class DownloadFileFromURL extends AsyncTask<String, Integer, String> {
        public Context context;

        public DownloadFileFromURL(Context context) {
            this.context = context;
        }


        /**
         * Before starting background thread
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("Starting download");
            progressBar = new ProgressDialog(DetailActivity.this);
            progressBar.setMessage("Loading... Please wait...");
            progressBar.setIndeterminate(false);
            progressBar.setCancelable(false);
            progressBar.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            String param = f_url[0];
            try {
                String root = Environment.getExternalStorageDirectory().toString();
                URL url = new URL(param);
                URLConnection connection = url.openConnection();
                connection.connect();
                int lenghtOfFile = connection.getContentLength();
                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                // Output stream to write file
                int start = img.length() - 10;
                int end = img.length();

                OutputStream output = new FileOutputStream(root + "/" + img.substring(start, end));

                byte data[] = new byte[1024];

                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress((int) (total * 100 / lenghtOfFile));
                    // writing data to file
                    output.write(data, 0, count);
                }
                // flushing output
                output.flush();
                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        /**
         * After completing background task
         **/
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.dismiss();
            Toast.makeText(context, "Download Success", Toast.LENGTH_SHORT).show();
        }
    }
}
