package com.project.group18.limberup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileOfflineEditActivity extends AppCompatActivity {

    // Declaration of Button type Elements
    private ImageView m_coverImage = null;
    private ImageView m_ProfilePictureBorder = null;
    private CircleImageView m_ProfilePicture = null;

    private TextView m_name = null;
    private EditText m_nameParam = null;
    private TextView m_bio = null;
    private EditText m_bioPram = null;
    private Button m_submitBtn = null;
    private Button m_cancelBtn = null;

    StorageReference storageReference = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        storageReference = FirebaseStorage.getInstance().getReference();

        m_coverImage = findViewById(R.id.cover_pic);
        m_coverImage.setVisibility(View.GONE);
        m_ProfilePictureBorder = findViewById(R.id.profile_pic_border);
        m_ProfilePictureBorder.setVisibility(View.GONE);
        m_ProfilePicture = findViewById(R.id.profile_picture);
        m_ProfilePicture.setVisibility(View.GONE);

        m_name = findViewById(R.id.name);
        m_nameParam = findViewById(R.id.name_param);
        m_bio = findViewById(R.id.bio);
        m_bioPram = findViewById(R.id.bio_param);
        m_submitBtn = findViewById(R.id.update_button);
        m_cancelBtn = findViewById(R.id.cancel_button);

        SharedPreferences sharedPref = UserProfileOfflineEditActivity.this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", null);

        m_nameParam.setText(sharedPref.getString("name", null));
        m_bioPram.setText(sharedPref.getString("bio", null));


        m_submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("offline", "1");
                editor.putString("name", m_nameParam.getText().toString());
                editor.putString("bio", m_bioPram.getText().toString());
                editor.apply();
            }
        });
    }


}


