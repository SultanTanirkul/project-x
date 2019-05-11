package com.project.group18.limberup;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ExploreArrayAdapter extends ArrayAdapter<User> {
    private Context context = null;
    private List<User> users = null;

    public ExploreArrayAdapter(Context context, int resource, ArrayList<User> objects){
        super(context, resource, objects);
        this.context = context;
        this.users = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        User user = users.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_explore, null);

        CircleImageView profilePic = view.findViewById(R.id.explore_profile_picture);
        TextView userName_Age = view.findViewById(R.id.explore_user_name_age);
        TextView userInterests = view.findViewById(R.id.explore_user_interests);

        profilePic.setImageURI(Uri.parse(user.getImage()));
        userName_Age.setText(user.getUsername() + ", "+ user.getAge());
        userInterests.setText(user.getInterests());
        return view;
    }
}
