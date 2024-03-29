package com.project.group18.limberup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
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

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PendingFriendListAdapter extends ArrayAdapter<User>{
    private Context context = null;
    private List<User> users = null;

    public PendingFriendListAdapter(Context context, int resource, ArrayList<User> users){
        super(context, resource, users);
        this.context = context;
        this.users = users;
        //Log.v("Client", users.get(1).getUsername());
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        User user = users.get(position);
        
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_explore_person, null);

        CircleImageView profilePic = view.findViewById(R.id.explore_profile_picture);
        TextView userName_Age = view.findViewById(R.id.explore_user_name_age);
        TextView userInterests = view.findViewById(R.id.explore_user_interests);

        user.setImageView(profilePic);
        userName_Age.setText(user.getUsername() + ", "+ user.getAge());
        //userInterests.setText(user.getInterests());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                String token = sharedPref.getString("token", null);
                HashMap params = new HashMap<>();
                params.put("token", token);
                params.put("usertoconfirm", user.getId());
                ServerOp serverOp = ServerOp.getInstance(context);
                serverOp.addToRequestQueue(serverOp.postRequest("https://limberup.herokuapp.com/api/user/friend/confirm", params, (s) ->{
                    Log.i("FRIEND LISTT ADAPTER", "onClick: ");
                }));

                Toast.makeText(getContext(), "USER HAS BEEN ADDED", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
}
