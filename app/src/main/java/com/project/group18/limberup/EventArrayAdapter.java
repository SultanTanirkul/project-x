package com.project.group18.limberup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
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
        View view = inflater.inflate(R.layout.item_events_event, null);

        TextView title = view.findViewById(R.id.event_title);
        TextView playerNumber =  view.findViewById(R.id.event_player_number);
        TextView location =  view.findViewById(R.id.event_location);
        TextView playerLimit =  view.findViewById(R.id.event_player_limit);
        TextView date =  view.findViewById(R.id.event_date);
        ConstraintLayout constraintLayout = view.findViewById(R.id.block);

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EventActivity.class);
                getContext().startActivity(intent);
            }
        });

        title.setText(event.getTitle());
        playerLimit.setText(String.valueOf(event.getPlayerLimit()));
        location.setText(event.getLocation()[0] + ", " + event.getLocation()[1]);
        date.setText(event.getDate());

        return view;
    }

}
