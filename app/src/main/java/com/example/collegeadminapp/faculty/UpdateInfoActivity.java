package com.example.collegeadminapp.faculty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.IInterface;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.collegeadminapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class UpdateInfoActivity extends AppCompatActivity {

    ImageView profile_image;
    EditText text_Fname,text_Femail, text_Fpost;
    Button update_faculty, delete_faculty;
    final int REQ = 1;
    private Bitmap bitmap = null;
    private String downloadUrl, category, uniqueKey;

    private StorageReference storageReference;
    private DatabaseReference reference, dbRef;

    private String name, email, image, post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);

        storageReference = FirebaseStorage.getInstance().getReference();
        reference = FirebaseDatabase.getInstance().getReference().child("faculty");


        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        post = getIntent().getStringExtra("post");
        image = getIntent().getStringExtra("image");

        profile_image = findViewById(R.id.profile_image);
        text_Fname = findViewById(R.id.text_Fname);
        text_Femail = findViewById(R.id.text_Femail);
        text_Fpost = findViewById(R.id.text_Fpost);
        update_faculty = findViewById(R.id.update_faculty);
        delete_faculty = findViewById(R.id.delete_faculty);

        try {
            Picasso.get().load(image).into(profile_image);
        } catch (Exception e) {
            e.printStackTrace();
        }
        text_Fname.setText(name);
        text_Fpost.setText(post);
        text_Femail.setText(email);

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();

            }
        });

        update_faculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = text_Fname.getText().toString();
                email = text_Femail.getText().toString();
                post = text_Fpost.getText().toString();
                checkValidation();
            }
        });

        delete_faculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               deleteData();
            }
        });

    }

    private void checkValidation() {

        if (name.isEmpty()){
            text_Fname.setError("Empty");
            text_Fname.requestFocus();
        }else if (email.isEmpty()){
            text_Femail.setError("Empty");
            text_Femail.requestFocus();
        }else if (post.isEmpty()) {
            text_Fpost.setError("Empty");
            text_Fpost.requestFocus();
        }else if (bitmap == null){
            updateData(image);
        }else
        {
            uploadImage();
        }
    }

    private void updateData(String s) {
        HashMap hp = new HashMap();
        hp.put("name",name);
        hp.put("email",email);
        hp.put("post",post);
        hp.put("image",s);

        uniqueKey = getIntent().getStringExtra("key");
        category = getIntent().getStringExtra("category");

        reference.child(category).child(uniqueKey).updateChildren(hp)
                .addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(UpdateInfoActivity.this, "Faculty Updated Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateInfoActivity.this, UpdateFacultyActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateInfoActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadImage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] finaling = baos.toByteArray();
        final StorageReference filepath;
        filepath = storageReference.child("faculty").child(finaling+"jpg");
        final UploadTask uploadTask = filepath.putBytes(finaling);
        uploadTask.addOnCompleteListener(UpdateInfoActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl = String.valueOf(uri);
                                    updateData(downloadUrl);
                                }
                            });
                        }
                    });
                }else {
                    Toast.makeText(UpdateInfoActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void deleteData() {

        uniqueKey = getIntent().getStringExtra("key");
        category = getIntent().getStringExtra("category");

        reference.child(category).child(uniqueKey).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid)
                        {
                            Toast.makeText(UpdateInfoActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(UpdateInfoActivity.this, UpdateFacultyActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateInfoActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openGallery() {
        Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage,REQ);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode == RESULT_OK){
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            profile_image.setImageBitmap(bitmap);
        }
    }
}