package com.project.group18.limberup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.UserViewHolder> {

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView mUserPic;
        public TextView mUserNameAge;

        public UserViewHolder(View itemView)
        {
            super(itemView);
            this.mUserPic = itemView.findViewById(R.id.user_item_c_image_view);
            this.mUserNameAge = itemView.findViewById(R.id.user_item_name_age_text_view);
        }
    }
    ArrayList<User> users;
    Context context;
    public UserRecyclerAdapter(Context context, ArrayList<User> users)
    {
        this.users = users;
        this.context = context;
    }
    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_recycler_view, viewGroup, false);
        UserRecyclerAdapter.UserViewHolder uvh  = new  UserRecyclerAdapter.UserViewHolder(view);
        return uvh;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder userViewHolder, int i) {
        User currentItem = users.get(i);
        Picasso.get().load(currentItem.getImage()).into(userViewHolder.mUserPic);
        userViewHolder.mUserNameAge.setText(currentItem.getUsername() + ", " +  currentItem.getAge());
    }

    @Override
    public int getItemCount() {
        return 0;
    }


}
