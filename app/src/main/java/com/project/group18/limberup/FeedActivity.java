package com.project.group18.limberup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.prof.rssparser.Article;
import com.prof.rssparser.OnTaskCompleted;
import com.prof.rssparser.Parser;
import java.util.List;

public class FeedActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        String urlString = "http://ftr.fivefilters.org/makefulltextfeed.php?url=https://rss.cbssports.com/rss/headlines/&max=3&links=preserve";
        Parser parser = new Parser();
        parser.onFinish(new OnTaskCompleted() {

            //what to do when the parsing is done
            @Override
            public void onTaskCompleted(List<Article> list) {
                Log.v("Client", list.get(1).getDescription());
            }

            //what to do in case of error
            @Override
            public void onError(Exception e) {
               Log.v("Client", "Not working properly");
            }
        });
        parser.execute(urlString);
    }
}
