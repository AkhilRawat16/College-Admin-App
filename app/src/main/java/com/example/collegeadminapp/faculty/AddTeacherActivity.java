package com.example.collegeadminapp.faculty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.collegeadminapp.NoticeActivity;
import com.example.collegeadminapp.NoticeData;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddTeacherActivity extends AppCompatActivity {

    ImageView profile_image;
    EditText text_name,text_email,text_post;
    Spinner faculty_category;
    Button button_upload_faculty;
    final int REQ = 1;
    private Bitmap bitmap = null;
    String category;
    String name, email, post, downloadUrl ="";
    private StorageReference storageReference;
    private DatabaseReference reference, dbRef;

    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher);

        pd = new ProgressDialog(this);
        storageReference = FirebaseStorage.getInstance().getReference();
        reference = FirebaseDatabase.getInstance().getReference().child("faculty");

        profile_image = findViewById(R.id.profile_image);
        text_name = findViewById(R.id.text_name);
        text_email = findViewById(R.id.text_email);
        text_post = findViewById(R.id.text_post);
        faculty_category = findViewById(R.id.faculty_category);
        button_upload_faculty = findViewById(R.id.button_upload_faculty);
        
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        button_upload_faculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });

        String[] items = new String[]{"Select Category","Computer Science","Electrical","Electronics","Mechanical","Civil","Others"};
        faculty_category.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items));

        faculty_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = faculty_category.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void checkValidation() {
        name = text_name.getText().toString();
        post = text_post.getText().toString();
        email = text_email.getText().toString();

        if (name.isEmpty()){
            text_name.setError("Empty");
            text_name.requestFocus();
        }else if (email.isEmpty()){
            text_email.setError("Empty");
            text_email.requestFocus();
        }else if (post.isEmpty()) {
            text_post.setError("Empty");
            text_post.requestFocus();
        }else if (category.equals("Select Category")){
            Toast.makeText(AddTeacherActivity.this, "Select a Category", Toast.LENGTH_SHORT).show();
        }else if (bitmap == null){
            uploadData();
        }else {
            uploadImage();
        }
    }

    private void uploadData() {
       dbRef = reference.child(category);
        final String uniqueKey = dbRef.push().getKey();

        TeacherData teacherData = new TeacherData(name,email,post, downloadUrl,uniqueKey);

        dbRef.child(uniqueKey).setValue(teacherData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                pd.dismiss();
                Toast.makeText(AddTeacherActivity.this,"Faculty Added Successfully",Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(AddTeacherActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadImage() {
        pd.setMessage("Uploading");
        pd.show();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] finaling = baos.toByteArray();
        final StorageReference filepath;
        filepath = storageReference.child("faculty").child(finaling+"jpg");
        final UploadTask uploadTask = filepath.putBytes(finaling);
        uploadTask.addOnCompleteListener(AddTeacherActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                                    uploadData();
                                }
                            });
                        }
                    });
                }else {
                    pd.dismiss();
                    Toast.makeText(AddTeacherActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                }
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