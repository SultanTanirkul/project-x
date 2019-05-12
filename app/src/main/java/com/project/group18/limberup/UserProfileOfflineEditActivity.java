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
    private Button m_BuddyUpButton = null;
    private Button m_FollowButton = null;
    private Button m_MessageButton = null;
    private ImageView m_ProfilePictureBorder = null;
    private CircleImageView m_ProfilePicture = null;
    private TextView m_username = null;
    private Uri filePath = null;
    private User currentUser;
    private String imageUrl;

    private TextView m_name = null;
    private EditText m_nameParam = null;
    private TextView m_bio = null;
    private EditText m_bioPram = null;
    private Button m_submitBtn = null;
    private Button m_cancelBtn = null;

    private static final int PICK_IMAGE_REQUEST = 71;

    Bitmap bitmap;

    StorageReference storageReference = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        storageReference = FirebaseStorage.getInstance().getReference();

        m_ProfilePictureBorder = findViewById(R.id.profile_pic_border);
        m_ProfilePicture = findViewById(R.id.profile_picture);

        m_name = findViewById(R.id.name);
        m_nameParam = findViewById(R.id.name_param);
        m_bio = findViewById(R.id.bio);
        m_bioPram = findViewById(R.id.bio_param);
        m_submitBtn = findViewById(R.id.update_button);
        m_cancelBtn = findViewById(R.id.cancel_button);

        SharedPreferences sharedPref = UserProfileOfflineEditActivity.this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", null);

        HashMap params = new HashMap<>();
        params.put("token", token);
        Log.i("---->", "setActivities: " + token);
        ServerOp serverOp = ServerOp.getInstance(getApplicationContext());
        serverOp.addToRequestQueue(serverOp.postRequest("https://limberup.herokuapp.com/api/user/read", params, (s) -> {
            try {
                setUser(new JSONObject(s));
            } catch (JSONException e) {
                Log.i("---->", "setActivities: " + e.toString());
            }
        }));

        m_submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap params = new HashMap<>();
                params.put("token", token);
                params.put("username", m_nameParam.getText().toString());
                params.put("bio", m_bioPram.getText().toString());
                params.put("profileImgUrl", imageUrl);
                ServerOp serverOp = ServerOp.getInstance(getApplicationContext());
                serverOp.addToRequestQueue(serverOp.postRequest("https://limberup.herokuapp.com/api/user/update", params, (s) -> { }));
                finish();
            }
        });

        m_cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        m_ProfilePictureBorder.setOnClickListener((View v) -> {
            chooseImage();
        });
    }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                uploadImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setUser(JSONObject userJson) throws JSONException {
        currentUser = new User(userJson);
        m_nameParam.setText(currentUser.getUsername());
        m_bioPram.setText(currentUser.getBio());
        currentUser.setImage(m_ProfilePicture);
    }

    private void uploadImage() {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            final ProgressBar progressBar = findViewById(R.id.progressBar);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
            UploadTask uploadTask = ref.putFile(filePath);
            uploadTask
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(UserProfileOfflineEditActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw task.getException();
                                    }
                                    return ref.getDownloadUrl();
                                }

                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Uri downloadUri = task.getResult();
                                        progressBar.setVisibility(View.VISIBLE);
                                        Picasso.get().load(downloadUri).into(m_ProfilePicture, new com.squareup.picasso.Callback() {
                                            @Override
                                            public void onSuccess() {
                                                if (progressBar != null) {
                                                    progressBar.setVisibility(View.GONE);
                                                }
                                            }

                                            @Override
                                            public void onError(Exception e) {
                                                Toast.makeText(UserProfileOfflineEditActivity.this, "Error: Could not download an image.", Toast.LENGTH_SHORT);
                                            }
                                        });

                                        imageUrl = downloadUri.toString();
                                        Log.v("Firebase", imageUrl);

                                    } else {
                                        // Handle failures
                                        // ...
                                    }
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(UserProfileOfflineEditActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }

}

