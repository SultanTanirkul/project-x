package com.project.group18.limberup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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

import java.io.IOException;
import java.util.UUID;

public class UserProfileActivity extends AppCompatActivity {

    // Declaration of Button type Elements
    private Button m_BuddyUpButton           = null;
    private Button m_FollowButton            = null;
    private Button m_MessageButton           = null;
    private ImageView m_ProfilePictureBorder = null;
    private ImageView m_ProfilePicture       = null;
    private Uri filePath                     = null;

    private static final int PICK_IMAGE_REQUEST = 71;

    Bitmap bitmap;

    StorageReference storageReference = null;

    //Declaration of TextView type elements
    private TextView m_FollowerCount = null;
    private TextView m_BuddyCount  = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_profile);
        storageReference = FirebaseStorage.getInstance().getReference();
        //Initialization of Activity elements
        m_FollowButton = findViewById(R.id.follow_button);
        m_BuddyUpButton = findViewById(R.id.buddy_up_button);
        m_MessageButton = findViewById(R.id.follow_button);
        m_FollowerCount = findViewById(R.id.follower_count);
        m_BuddyCount = findViewById(R.id.buddy_count);
        m_ProfilePictureBorder = findViewById(R.id.profile_pic_border);
        m_ProfilePicture = findViewById(R.id.profile_picture);

        m_ProfilePictureBorder.setOnClickListener((View v) -> {
            chooseImage();
        });



        //Follow Button's functionality to increase the follower count if clicked
        m_FollowButton.setOnClickListener((View v) -> {
            int folCount = Integer.parseInt(m_FollowerCount.getText().toString());

            folCount++;
            m_FollowerCount.setText(String.valueOf(folCount));
        });

        //Buddy Up Button's functionality to increase the buddy count if clicked
        m_BuddyUpButton.setOnClickListener((View v) -> {
            int buddyCount = Integer.parseInt((m_BuddyCount.getText().toString()));

            buddyCount++;
            m_BuddyCount.setText(String.valueOf(buddyCount));
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
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                uploadImage();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            final ProgressBar progressBar = findViewById(R.id.progressBar);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
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
                                    if(!task.isSuccessful()){
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
                                        Log.v("Firebase", downloadUri.toString());
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
                            Toast.makeText(UserProfileActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });



        }


    }
}