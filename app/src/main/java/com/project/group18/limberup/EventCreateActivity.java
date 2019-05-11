package com.project.group18.limberup;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventCreateActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView m_Map_Button = null;
    public static final int RESULT_OK = 1;
    private EditText m_Event_location = null;
    private ImageView m_Date_Picker_Button = null;
    private ImageView m_Date_Time_Picker = null;
    private Calendar c = Calendar.getInstance();
    private EditText m_Time_Text, m_Date_Text, m_Event_Name, m_Min_Player_Number, m_Max_Player_Number;
    private Button m_Back_button, m_Create_Button;

    private String placeId = null;
    private LatLng latLng = null;

    // Date and Time variables
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        m_Map_Button = findViewById(R.id.create_event_map_button);
        m_Event_location = findViewById(R.id.create_event_location);
        m_Time_Text = findViewById(R.id.create_event_time_text);
        m_Date_Text = findViewById(R.id.create_event_date_text);
        m_Date_Picker_Button = findViewById(R.id.create_event_date_picker);
        m_Date_Time_Picker = findViewById(R.id.create_event_time_picker);
        m_Back_button = findViewById(R.id.create_event_back_button);
        m_Create_Button = findViewById(R.id.create_event_create_button);
        m_Event_Name = findViewById(R.id.create_event_title);
        m_Min_Player_Number = findViewById(R.id.create_event_min_player_number);
        m_Max_Player_Number = findViewById(R.id.create_event_max_player_number);

        m_Map_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventCreateActivity.this, MapActivity.class);
                startActivityForResult(intent, RESULT_OK);
            }
        });

        m_Date_Picker_Button.setOnClickListener(this);
        m_Date_Time_Picker.setOnClickListener(this);
        m_Back_button.setOnClickListener(this);
        m_Create_Button.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {

        if (v == m_Date_Picker_Button) {

            // Get Current Date
            c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            Date dt = new Date();

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            m_Date_Text.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            c = Calendar.getInstance();
            c.setTime(dt);
            datePickerDialog.show();
        }
        if (v == m_Date_Time_Picker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            if(minute < 10)
                                m_Time_Text.setText(hourOfDay + ":0" + minute);
                            else
                                m_Time_Text.setText(hourOfDay + ":" + minute);

                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }

        if(v == m_Back_button)
        {
            finish();
        }

        if(v == m_Create_Button)
        {
            if(
                    m_Event_Name.getText().toString().isEmpty()
                    || m_Min_Player_Number.getText().toString().isEmpty()
                    || m_Event_location.getText().toString().isEmpty()
                    || m_Event_Name.getText().toString().isEmpty()
                    || m_Max_Player_Number.getText().toString().isEmpty()
                    || m_Date_Text.getText().toString().isEmpty()
                    || m_Time_Text.getText().toString().isEmpty())
            {
                Toast.makeText(this, "To create an activity, every single field must be filled", Toast.LENGTH_LONG).show();
            }else if(Integer.parseInt(m_Min_Player_Number.getText().toString()) > Integer.parseInt(m_Max_Player_Number.getText().toString())){
                Toast.makeText(this, "Min Number of Players cannot be greater than Player Limit", Toast.LENGTH_LONG).show();
            }else
            {
                ServerOp createEvent = ServerOp.getInstance(getApplicationContext());
                Map<String, String> eventPrams = new HashMap<>();
                eventPrams.put("name", m_Event_Name.getText().toString());
                eventPrams.put("token", m_Min_Player_Number.getText().toString());
                eventPrams.put("token", m_Event_Name.getText().toString());
                eventPrams.put("token", m_Max_Player_Number.getText().toString());
                eventPrams.put("token", m_Date_Text.getText().toString());
                eventPrams.put("token", m_Time_Text.getText().toString());

                createEvent.addToRequestQueue(createEvent.postRequest("https://limberup.herokuapp.com/api/event/create", eventPrams, (s) -> {
                    Toast.makeText(this, s, Toast.LENGTH_LONG).show();

                }));
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==1)
        {
            try {
                placeId = data.getStringExtra("place_id");

                PlacesClient placesClient = Places.createClient(this);

                List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.ADDRESS, Place.Field.LAT_LNG);

                FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();

                placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
                    Place place = response.getPlace();
                    Log.i("Client:", "Place found: " + place.getAddress());
                    m_Event_location.setText(String.valueOf(place.getAddress()));
                    latLng = place.getLatLng();
                }).addOnFailureListener((exception) -> {
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        int statusCode = apiException.getStatusCode();
                        // Handle error with given status code.
                        Log.e("Client:", "Place not found: " + exception.getMessage());
                    }
                });
            }catch(NullPointerException e)
            {
                Toast.makeText(EventCreateActivity.this, "Event cannot be created without location.", Toast.LENGTH_LONG).show();

            }
        }
    }

}
