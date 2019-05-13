package com.project.group18.limberup;

import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.prof.rssparser.Article;
import com.prof.rssparser.OnTaskCompleted;
import com.prof.rssparser.Parser;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity {
    private RecyclerView m_RecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Feed> feedList = new ArrayList<>();
    private boolean feedParsed = false;
    private SwipeRefreshLayout refreshLayout;
    private String urlString = "https://feeds.bbci.co.uk/news/health/rss.xml";
    private Parser parser = new Parser();
    private int counter = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);




        m_RecyclerView = findViewById(R.id.feed_activity_recycler_view_list);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new FeedRecyclerAdapter(feedList, this);
        m_RecyclerView.setLayoutManager(mLayoutManager);
        m_RecyclerView.setAdapter(mAdapter);

        parser.onFinish(new OnTaskCompleted() {
            //what to do when the parsing is done
            @Override
            public void onTaskCompleted(List<Article> list) {
                for(Article f : list) {
                    Log.i("Client", "Works properly");
                    feedList.add(new Feed(f.getTitle(), f.getDescription(), f.getImage().replace("http://", "https://"), f.getLink(), f.getPubDate()));
                    Log.v("Client", ""+feedList.size());
                }
                feedParsed = true;
            }
            //what to do in case of error
            @Override
            public void onError(Exception e) {
                Log.e("Client", "Cannot retrieve RSS feed.");
                feedParsed = true;
                Toast.makeText(FeedActivity.this, "Feed Cannot be loaded.", Toast.LENGTH_LONG).show();
            }
        });
        parser.execute(urlString);
        while(!feedParsed){
            feedParsed = false;
            mAdapter.notifyDataSetChanged();
        }
    }
}