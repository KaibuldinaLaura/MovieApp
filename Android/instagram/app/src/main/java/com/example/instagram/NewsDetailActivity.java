package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsDetailActivity extends AppCompatActivity {

    private TextView tvNewsDetail;
    private ImageView tvImageDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        tvNewsDetail = findViewById(R.id.tvNewsDetail);
        tvImageDetail = findViewById(R.id.imageView6);

        News news = (News) getIntent().getParcelableExtra("news");
        tvNewsDetail.setText(news.getTheme());
        tvImageDetail.setImageResource(news.getImageUrl());
    }
}
