package com.sampleapp.samplearticleapp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.sampleapp.samplearticleapp.R;

public class DetailActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        NetworkImageView imageView = (NetworkImageView) findViewById(R.id.image_view);
        TextView textViewDes = (TextView) findViewById(R.id.textViewDesc);

        String imageUrl = getIntent().getStringExtra("largeImage");
        String caption = getIntent().getStringExtra("description");

        if(!TextUtils.isEmpty(caption) && !"null".equals(caption))
            textViewDes.setText(Html.fromHtml(caption));
        ImageLoader.ImageCache imageCache = new BitmapLruCache();
        ImageLoader imageLoader = new ImageLoader(Volley.newRequestQueue(this), imageCache);
        imageView.setImageUrl(imageUrl, imageLoader);
    }
}
