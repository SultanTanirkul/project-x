package com.project.group18.limberup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.GridLayout;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;


public class ActivityCategory extends AppCompatActivity {
    private ArrayList<Activity> activities = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Button Functionality
        setContentView(R.layout.activity_category);
        SharedPreferences sharedPref = ActivityCategory.this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", null);
        HashMap params = new HashMap<>();
        params.put("token", token);
        ServerOp serverOp = ServerOp.getInstance(getApplicationContext());
        serverOp.addToRequestQueue(serverOp.postRequest("https://limberup.herokuapp.com/api/activity/read", params, (s) ->{
            try {
                JSONArray response = new JSONArray(s);
                setActivities(response);
                setupActivityGrid();
            } catch(JSONException e){
                Log.i("---->", "setActivities: " + e.toString());
            }
        }));
    }


    public void setActivities(JSONArray activities) throws JSONException{
            for (int i = 0; i < activities.length(); i++) {
                this.activities.add(new Activity(activities.getJSONObject(i)));
            }
            Log.i("---->", "setActivities: " + this.activities.size());
    }




    // Functions to definition of the category button
    private void setupActivityGrid() {
        GridLayout gridLayout = (GridLayout) findViewById(R.id.activityGrid);

        gridLayout.removeAllViews();

        int total = activities.size();
        int column = 2;
        if(column > total && total != 0){
            column = total;
        }
        int row = total / column;
        gridLayout.setColumnCount(column);
        gridLayout.setRowCount(row + 1);
        for (int i = 0, c = 0, r = 0; i < total; i++, c++) {
            if (c == column) {
                c = 0;
                r++;
            }
            final Activity currentActivity = activities.get(i);
            Button button = new Button(this);
            button.setWidth(gridLayout.getWidth()/2);
            button.setText(currentActivity.name);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ActivityCategory.this, EventListActivity.class);
                    intent.putExtra("id", currentActivity.id);
                    intent.putExtra("activity", currentActivity.name);
                    startActivity(intent);
                }
            });

            button.setLayoutParams(new GridLayout.LayoutParams());

            GridLayout.Spec rowSpan = GridLayout.spec(GridLayout.UNDEFINED, 1);
            GridLayout.Spec colspan = GridLayout.spec(GridLayout.UNDEFINED, 1);
            if (r == 0 && c == 0) {
                Log.e("", "spec");
                colspan = GridLayout.spec(GridLayout.UNDEFINED, 2);
                rowSpan = GridLayout.spec(GridLayout.UNDEFINED, 2);
            }
            GridLayout.LayoutParams gridParam = new GridLayout.LayoutParams(
                    rowSpan, colspan);
            gridLayout.addView(button, gridParam);


        }
    }


}
