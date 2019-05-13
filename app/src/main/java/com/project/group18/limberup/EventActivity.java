package com.project.group18.limberup;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.prof.rssparser.Parser;

import java.util.ArrayList;

public class EventActivity extends AppCompatActivity {
    private ArrayList<User> users = new ArrayList<>();
    private RecyclerView m_RecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        User user = new User("Megan", null, null, null, null, "https://gameplay.tips/uploads/posts/2017-11/1509666440_life-is-strange-before-the-storm.jpg", 30);
        users.add(user);
        users.add(user);
        users.add(user);
        users.add(user);
        users.add(user);
        users.add(user);

        m_RecyclerView = findViewById(R.id.event_user_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new UserRecyclerAdapter(this, users);
        m_RecyclerView.setLayoutManager(mLayoutManager);
        m_RecyclerView.setAdapter(mAdapter);
    }
}
