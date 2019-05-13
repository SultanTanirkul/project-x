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
import android.widget.ListView;
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

public class DashboardEventAdapter extends ArrayAdapter<Event>{
    private Context context = null;
    private List<Event> events = null;


    public DashboardEventAdapter(Context context, int resource, ArrayList<Event> events){
        super(context, resource, events);
        this.context = context;
        this.events = events;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        Event event = events.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_dashboard_event, null);

        TextView eventTitle = view.findViewById(R.id.event_title);
        TextView eventTime = view.findViewById(R.id.event_date);
        eventTitle.setText(event.getName());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EventActivity.class);
                context.startActivity(intent);
            }
        });

        return view;
    }
}
