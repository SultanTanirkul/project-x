package com.project.group18.limberup;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    // Declaration of Button type Elements
    private Button m_BuddyUpButton = null;
    private Button m_MessageButton = null;
    private Button m_EditButton = null;
    private ImageView m_ProfilePictureBorder = null;
    private CircleImageView m_ProfilePicture = null;
    private TextView m_bio = null;
    private TextView m_username = null;
    private Uri filePath = null;
    private User currentUser;
    private String imageUrl;

    private static final int PICK_IMAGE_REQUEST = 71;

    Bitmap bitmap;

    StorageReference storageReference = null;

    //Declaration of TextView type elements
    private TextView m_BuddyCount = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        storageReference = FirebaseStorage.getInstance().getReference();
        //Initialization of Activity elements
        m_BuddyUpButton = findViewById(R.id.buddy_up_button);
        m_MessageButton = findViewById(R.id.message_button);
        m_EditButton = findViewById(R.id.edit_button);
        m_BuddyCount = findViewById(R.id.buddy_count);
        m_ProfilePictureBorder = findViewById(R.id.profile_pic_border);
        m_ProfilePicture = findViewById(R.id.profile_picture);
        m_username = findViewById(R.id.name);
        m_bio = findViewById(R.id.user_bio);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        if (id != null) {
            m_EditButton.setVisibility(View.GONE);
            loadOtherUser(id);
        } else {
            loadCurrentUser();
            m_MessageButton.setVisibility(View.GONE);
            m_EditButton.setVisibility(View.VISIBLE);
        }

        m_ProfilePictureBorder.setOnClickListener((View v) -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            DialogBuilder dialogBuilder = new DialogBuilder();
            dialogBuilder.show(fragmentManager, "device");
            //chooseImage();
        });
    }

    public void dispatchTakePictureIntent() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.d("Client", "Permission is granted");


            } else {
                Log.d("Client", "Permission is revoked");
                ActivityCompat.requestPermissions(UserProfileActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        } else {
        }
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void chooseImage() {
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
        }else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            m_ProfilePicture.setImageBitmap(imageBitmap);
            Uri tempUri = getImageUri(getApplicationContext(), imageBitmap);
            filePath = tempUri;
            uploadImage();
        }
    }

    public void setUser(JSONObject userJson) throws JSONException {
        currentUser = new User(userJson);
        m_username.setText(currentUser.getUsername());
        m_bio.setText(currentUser.getBio());
        currentUser.setImage(m_ProfilePicture);
        Log.i("---->", "friends count: " + currentUser.getFriends().size());
        m_BuddyCount.setText("Friends: " + currentUser.getFriends().size());
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
                            Toast.makeText(UserProfileActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
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
                                                Toast.makeText(UserProfileActivity.this, "Error: Could not download an image.", Toast.LENGTH_SHORT);
                                            }
                                        });

                                        imageUrl = downloadUri.toString();
                                        Log.v("Firebase", imageUrl);

                                        SharedPreferences sharedPref = UserProfileActivity.this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                                        String token = sharedPref.getString("token", null);
                                        HashMap params = new HashMap<>();
                                        params.put("token", token);
                                        params.put("profileImgUrl", imageUrl);
                                        ServerOp serverOp = ServerOp.getInstance(getApplicationContext());
                                        serverOp.addToRequestQueue(serverOp.postRequest("https://limberup.herokuapp.com/api/user/update", params, (s) -> { }));


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
                            Toast.makeText(UserProfileActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void loadCurrentUser() {
        SharedPreferences sharedPref = UserProfileActivity.this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", null);
        HashMap params = new HashMap<>();
        params.put("token", token);
        Log.i("---->", "setActivities: loading current user");
        ServerOp serverOp = ServerOp.getInstance(getApplicationContext());
        serverOp.addToRequestQueue(serverOp.postRequest("https://limberup.herokuapp.com/api/user/read", params, (s) -> {
            try {
                setUser(new JSONObject(s));
            } catch (JSONException e) {
                Log.i("---->", "setActivities: " + e.toString());
            }
        }));

        m_BuddyUpButton.setText("Friend List");
        m_BuddyUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TAG", "onClick: server start");
                Intent intent = new Intent(UserProfileActivity.this, FriendListActivity.class);
                startActivity(intent);
            }
        });

        m_EditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, UserProfileEditActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void loadOtherUser(String id) {
        SharedPreferences sharedPref = UserProfileActivity.this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", null);
        HashMap params = new HashMap<>();
        params.put("_id", id);
        params.put("token", token);
        Log.i("---->", "setActivities: loading other user");
        ServerOp serverOp = ServerOp.getInstance(getApplicationContext());
        serverOp.addToRequestQueue(serverOp.postRequest("https://limberup.herokuapp.com/api/user/find", params, (s) -> {
            try {
                setUser(new JSONObject(s));
            } catch (JSONException e) {
                Log.i("---->", "setActivities: " + e.toString());
            }
        }));

        m_BuddyUpButton.setText("add friend");
        m_BuddyUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                params.put("usertoadd", currentUser.getId());
                serverOp.addToRequestQueue(serverOp.postRequest("https://limberup.herokuapp.com/api/user/friend/add", params, (s) -> {
                    m_BuddyUpButton.setText("requested");
                }));
            }
        });
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }
}
