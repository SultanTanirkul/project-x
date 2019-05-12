package com.project.group18.limberup;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class FeedRecyclerAdapter extends RecyclerView.Adapter<FeedRecyclerAdapter.FeedViewHolder> {
    private ArrayList<Feed> mFeedList;
    private Context context;


    public static class FeedViewHolder extends RecyclerView.ViewHolder {
        public ImageView mFeedImage;
        public TextView mFeedTitle, mFeedText, mDateTime;
        public Button mFeedMoreButton;
        public FeedViewHolder(View itemView) {
            super(itemView);
            mFeedImage = itemView.findViewById(R.id.feed_activity_image);
            mFeedTitle = itemView.findViewById(R.id.feed_activity_title);
            mFeedText = itemView.findViewById(R.id.feed_activity_text);
            mFeedMoreButton = itemView.findViewById(R.id.feed_activity_more_button);
            mDateTime = itemView.findViewById(R.id.feed_activity_date);
        }
    }

    public FeedRecyclerAdapter(ArrayList<Feed> feedList, Context context){
        this.mFeedList = feedList;
        this.context = context;
    }

    @NonNull
    @Override
    public FeedRecyclerAdapter.FeedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_view_feed_layout, viewGroup, false);
        FeedViewHolder fvh  = new FeedViewHolder(view);

        return fvh;
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder feedViewHolder, int i) {
        Feed currentItem = mFeedList.get(i);
        Picasso.get().load(currentItem.getImage()).resize(1000, 400).centerCrop().into(feedViewHolder.mFeedImage);
        feedViewHolder.mFeedTitle.setText(currentItem.getTitle());
        feedViewHolder.mFeedText.setText(currentItem.getDescription());
        String[] full = currentItem.getDateTime().split(" ");
        String dayOfTheWeek = full[0];
        String dayOfTheMonth = full[1];
        String Month = full[2];
        String Year = full[3];
        String date = dayOfTheWeek + " " + dayOfTheMonth + " " + Month + " " + Year;
        String time = full[4] + " " + full[5];
        feedViewHolder.mDateTime.setText("Date: " + date + "\n" + "Time: " + time);

        feedViewHolder.mFeedMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("url", currentItem.getLink());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mFeedList.size();
    }


}
