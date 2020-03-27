package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NewsListAdapter adapter;
    private NewsListAdapter.ItemClickListener listener = null;
    //private ImageButton ib2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ib2 = (ImageButton) findViewById(R.id.imageView2);

        listener = new NewsListAdapter.ItemClickListener() {
            @Override
            public void itemClick(int position, News item) {
                Intent intent = new Intent(MainActivity.this, NewsDetailActivity.class);
                intent.putExtra("news", item);
                startActivity(intent);
            }
        };

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new NewsListAdapter(newsGenerator(), listener);
        recyclerView.setAdapter(adapter);

        /*ib2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "You clicked on ImageView", Toast.LENGTH_LONG).show();
            }
        });*/
    }

    private List<News> newsGenerator() {
        List<News> items = new ArrayList<>();
        String s = "Нравится: ";
        String k;
        News news1 = new News(
                R.drawable.avatar,
                "9 января",
                k = "instagram",
                R.drawable.iv1,
                k + "  Do you wish I was different? -Euphoria",
                 s + 150, "Посмотреть все комментарии (15)");
        News news2 = new News(
                R.drawable.avatar,
                "15 февраля",
                k = "instagram",
                R.drawable.iv2,
                k + "  Square cat :)",
                s + 480200,
                "Посмотреть все комментарии (12655)");
        News news3 = new News(
                R.drawable.avatar2,
                "9 января",
                k = "loridemissy",
                R.drawable.iv3,
                k + "  Ice-cream - my love :3",
                s + 150, "Посмотреть все комментарии (15)");
        News news4 = new News(
                R.drawable.avatar2,
                "29 января",
                k = "loridemissy",
                R.drawable.iv4,
                k + "  Do you like tea?",
                s + 150, "Посмотреть все комментарии (15)");
        News news5 = new News(
                R.drawable.avatar3,
                "30 декабря",
                k = "mdkh_yo",
                R.drawable.iv5,
                k + "  Do you wish I was different? -Euphoria",
                s + 150, "Посмотреть все комментарии (15)");
        News news6 = new News(
                R.drawable.avatar3,
                "6 февраля",
                k = "mdkh_yo",
                R.drawable.iv6,
                k + "  Too sweet)",
                s + 150, "Посмотреть все комментарии (15)");
        News news7 = new News(
                R.drawable.avatar4,
                "3 января",
                k = "d_milkanova",
                R.drawable.iv7,
                k + "  Cooooookies ;)",
                s + 150, "Посмотреть все комментарии (15)");
        News news8 = new News(
                R.drawable.avatar4,
                "14 февраля",
                k = "d_milkanova",
                R.drawable.iv8,
                k + "  Coffee, coffee and only coffee",
                s + 150, "Посмотреть все комментарии (15)");
        News news9 = new News(
                R.drawable.avatar5,
                "8 марта",
                k = "kendalljenner",
                R.drawable.iv9,
                k + "  Tea or coffee?",
                s + 150, "Посмотреть все комментарии (15)");
        News news10 = new News(
                R.drawable.avatar5,
                "10 января",
                k = "kendalljenner",
                R.drawable.iv10,
                k + "  Of course coffee",
                s + 150, "Посмотреть все комментарии (15)");
        items.add(news1);
        items.add(news2);
        items.add(news3);
        items.add(news4);
        items.add(news5);
        items.add(news6);
        items.add(news7);
        items.add(news8);
        items.add(news9);
        items.add(news10);
        items.add(news1);
        items.add(news2);
        items.add(news3);
        items.add(news4);
        items.add(news5);
        items.add(news6);
        items.add(news7);
        items.add(news8);
        items.add(news9);
        items.add(news10);
        return items;
    }
}