package com.project.group18.limberup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

    public ExploreArrayAdapter(Context context, int resource, ArrayList<User> users){
        super(context, resource, users);
        this.context = context;
        this.users = users;
       // Log.v("Client", users.get(1).getUsername());
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        User user = users.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_view_explore_layout, null);

        CircleImageView profilePic = view.findViewById(R.id.explore_profile_picture);
        TextView userName_Age = view.findViewById(R.id.explore_user_name_age);
        TextView userInterests = view.findViewById(R.id.explore_user_interests);

        user.setImageView(profilePic);
        userName_Age.setText(user.getUsername() + ", "+ user.getAge());
        //userInterests.setText(user.getInterests());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("---->", "onClick: created on click listener");
                Intent intent = new Intent(context, UserProfileActivity.class);
                intent.putExtra("id", user.getId());
                context.startActivity(intent);
            }
        });
        return view;
    }
}
