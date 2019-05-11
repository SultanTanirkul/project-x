package com.project.group18.limberup;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import java.util.Arrays;
import java.util.List;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
public class MapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private Button m_Place_Information_Confirm = null;
    private String m_Place_Id = null;
    private LatLng latLng = null;
    private FusedLocationProviderClient fusedLocationClient;
    private LinearLayout m_Place_Information = null;
    private ImageView m_Place_Information_Image = null;
    private TextView m_Place_Information_Address = null;
    private Button m_Place_Information_Cancel = null;
    private Button m_Place_Information_Show = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkLocationPermission();
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        m_Place_Information_Image = findViewById(R.id.place_information_image);
        m_Place_Information_Address = findViewById(R.id.place_information_address);
        m_Place_Information_Cancel = findViewById(R.id.place_information_cancel_button);
        m_Place_Information_Show = findViewById(R.id.place_information_show_on_map_button);
        m_Place_Information_Confirm = findViewById(R.id.place_information_confirm_button);
        m_Place_Information = findViewById(R.id.place_information);

        Places.initialize(getApplicationContext(),getResources().getString(R.string.google_api_key));
        // Add a marker in Sydney and move the camera
        if(checkLocationPermission()) {
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(this);
            mMap.setOnMyLocationClickListener(this);
            mMap.setOnMarkerClickListener(this);
        }else{
            Toast.makeText(this, "No permission given, go to apps setting and allow it manually", Toast.LENGTH_LONG).show();
        }

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.ADDRESS, Place.Field.LAT_LNG));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i("Client:", "Place: " + place.getName() + ", " + place.getId());
                m_Place_Id = place.getId();
                m_Place_Information.setVisibility(View.VISIBLE);
                getImage(place.getId());
                m_Place_Information_Address.setText(place.getAddress());
                latLng = place.getLatLng();
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("Client", "An error occurred: " + status);
            }
        });

        m_Place_Information_Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("place_id", m_Place_Id);
                setResult(MapActivity.RESULT_OK, intent);
                finish();
            }
        });

        m_Place_Information_Show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.addMarker(new MarkerOptions()
                        .position(latLng));
                LatLng coordinate = latLng; //Store these lat lng values somewhere. These should be constant.
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                        coordinate, 15);
                mMap.animateCamera(cameraUpdate);
                m_Place_Information.setVisibility(View.GONE);
            }
        });

        m_Place_Information_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_Place_Information.setVisibility(View.GONE);
            }
        });


        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            LatLng coordinate = new LatLng(location.getLatitude(), location.getLongitude()); //Store these lat lng values somewhere. These should be constant.
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                                    coordinate, 15);
                            mMap.animateCamera(cameraUpdate);
                        }
                    }
                });

    }


    /**The staff not Really needed. For checking if permissions are given and some
     * click events*/

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).

        return false;
    }

    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {

        m_Place_Information.setVisibility(View.VISIBLE);
        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }


    private void getImage(String place_id)
    {
        PlacesClient placesClient = Places.createClient(this);

        // Specify fields. Requests for photos must always have the PHOTO_METADATAS field.
        List<Place.Field> fields = Arrays.asList(Place.Field.PHOTO_METADATAS);

        // Get a Place object (this example uses fetchPlace(), but you can also use findCurrentPlace())
        FetchPlaceRequest placeRequest = FetchPlaceRequest.builder(place_id, fields).build();

        placesClient.fetchPlace(placeRequest).addOnSuccessListener((response) -> {
            Dialog dialog = ProgressDialog.show(MapActivity.this, "",
                    "Loading. Please wait...", true);
            dialog.show();
            // Get the photo metadata.
            try {
                Place tempPlace = response.getPlace();
                PhotoMetadata photoMetadata = null;
                photoMetadata = tempPlace.getPhotoMetadatas().get(0);
                // Get the attribution text.
                String attributions = photoMetadata.getAttributions();

                // Create a FetchPhotoRequest.
                FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)// Optional.// Optional.
                        .build();
                placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
                    Bitmap bitmap = fetchPhotoResponse.getBitmap();

                    m_Place_Information_Image.setImageBitmap(getResizedBitmap(bitmap, 300, 200));
                    dialog.hide();
                }).addOnFailureListener((exception) -> {
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        int statusCode = apiException.getStatusCode();
                        // Handle error with given status code.
                        Log.e("Client:", "Place not found: " + exception.getMessage());
                        dialog.hide();
                    }
                });
            }catch(NullPointerException e)
            {
                Toast.makeText(this, "No Image Available", Toast.LENGTH_LONG).show();
                dialog.hide();
            }


        });
    }


    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

}
