package com.example.moises.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.moises.myapplication.Model.Advertisement;

import static android.R.animator.fade_in;

public class AdsDetailsActivity extends AppCompatActivity {
    public Advertisement advertisement;
    private TextView title;
    private TextView description;
    private ImageView image;


    public AdsDetailsActivity(){


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads_details);


        Intent intent = getIntent();

        advertisement = (Advertisement) intent.getSerializableExtra("ad");
        title = (TextView) findViewById(R.id.detail_title);
        description = (TextView) findViewById(R.id.detail_description);
        image = (ImageView) findViewById(R.id.detail_image);

        if(advertisement!=null){
            title.setText(advertisement.title);
            description.setText(advertisement.description);
            Log.d("DETAILS","AD URL:" +advertisement.imageURL);
            Glide.with(this)
                    .load(advertisement.imageURL)

                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .crossFade()
                    .into(image);


        }



    }
}
