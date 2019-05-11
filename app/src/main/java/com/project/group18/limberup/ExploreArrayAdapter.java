package com.project.group18.limberup;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.Circle;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ExploreArrayAdapter extends ArrayAdapter<User>{
    private Context context = null;
    private List<User> users = null;

    public ExploreArrayAdapter(Context context, int resource, ArrayList<User> objects){
        super(context, resource, objects);
        this.context = context;
        this.users = objects;
        Log.v("Client", users.get(1).getUsername());
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        User user = users.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_view_explore_layout, null);

        ImageView profilePic = view.findViewById(R.id.explore_profile_picture);
        TextView userName_Age = view.findViewById(R.id.explore_user_name_age);
        TextView userInterests = view.findViewById(R.id.explore_user_interests);

        Picasso.get().load("https://cdn.igromania.ru/mnt/articles/9/8/4/5/9/7/27170/html/img/59ab0985c3701a36.jpg").fit().into(profilePic, new Callback() {
            @Override public void onSuccess() {
                Toast.makeText(getContext(), "Images are downloaded.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(getContext(), "Images are not downloaded.", Toast.LENGTH_LONG).show();

            }

        });
        Toast.makeText(getContext(), "Images are downloaded.", Toast.LENGTH_LONG).show();

        userName_Age.setText(user.getUsername() + ", "+ user.getAge());
        userInterests.setText(user.getInterests());
        return view;
    }
}
