package com.project.group18.limberup;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EventArrayAdapter extends ArrayAdapter<Event> {
    private Context context;
    private List<Event> events;


    EventArrayAdapter(Context context, int resource, ArrayList<Event> objects)
    {
        super(context, resource, objects);

        this.context = context;
        this.events = objects;
    }


    public View getView(int position, View convertView, ViewGroup parent)
    {
        Event event = events.get(position);


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_view_events_layout, null);

        TextView title = view.findViewById(R.id.event_title);
        TextView playerNumber =  view.findViewById(R.id.event_player_number);
        TextView location =  view.findViewById(R.id.event_location);
        TextView playerLimit =  view.findViewById(R.id.event_player_limit);
        TextView date =  view.findViewById(R.id.event_date);


        title.setText(event.getTitle());
        playerLimit.setText(String.valueOf(event.getPlayerLimit()));
        playerNumber.setText(String.valueOf(event.getPlayerNumber()));
        location.setText(event.getLocation()[0] + ", " + event.getLocation()[1]);
        date.setText(event.getDate());

        return view;
    }

}
