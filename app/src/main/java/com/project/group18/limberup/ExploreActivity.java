package com.project.group18.limberup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ExploreActivity extends AppCompatActivity {

    private ArrayList<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        User user = new User("Dummy User", "Dummy Password", "DummyUser@dummymail.my", "I'm Dummy User", "Being Dummy",
                "https://cdn.igromania.ru/mnt/articles/9/8/4/5/9/7/27170/html/img/59ab0985c3701a36.jpg", 12);

        users.add(user);
        users.add(user);
        users.add(user);
        users.add(user);

        ArrayAdapter<User> adapter =  new ExploreArrayAdapter(this, 0, users);

        ListView exploreListView = findViewById(R.id.explore_list_view);

        exploreListView.setAdapter(adapter);

    }
}
