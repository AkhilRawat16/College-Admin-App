package com.example.collegeadminapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaRouter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class PdfActivity extends AppCompatActivity {

    private final int REQ = 1;
    private Uri pdfData;
    EditText ebook_title;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    Button button_ebook;
    LinearLayout addImage3;
    String downloadUrl;
    TextView pdf_textview;
    private String pdfName, title;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        pd = new ProgressDialog(this);
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        ebook_title = findViewById(R.id.ebook_title);
        button_ebook = findViewById(R.id.button_ebook);
        addImage3 = findViewById(R.id.addImage3);
        pdf_textview = findViewById(R.id.pdf_textview);

        button_ebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = ebook_title.getText().toString();
                if (title.isEmpty()){
                    ebook_title.setError("Title Empty");
                    ebook_title.requestFocus();
                }else if (pdfData == null){
                    Toast.makeText(PdfActivity.this, "Please upload Pdf", Toast.LENGTH_SHORT).show();
                }else {
                    uploadPdf();
                }
            }
        });

        addImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private void uploadPdf() {
        pd.setTitle("Please wait...");
        pd.setMessage("Uploading Pdf");
        pd.show();
        StorageReference reference = storageReference.child("pdf/"+ pdfName+"-"+System.currentTimeMillis()+".pdf");
        reference.putFile(pdfData)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete());
                        Uri uri = uriTask.getResult();
                        uploadData(String.valueOf(uri));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(PdfActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadData(String downloadUrl) {
        String uniqueKey = databaseReference.child("pdf").push().getKey();

        HashMap data = new HashMap();
        data.put("pdfTitle",title);
        data.put("pdfUrl",downloadUrl);

        databaseReference.child("pdf").child(uniqueKey).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                Toast.makeText(PdfActivity.this, "Pdf uploaded Successfully", Toast.LENGTH_SHORT).show();
                ebook_title.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(PdfActivity.this, "Failed to upload Pdf", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Pdf file"), REQ);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode == RESULT_OK){
           pdfData = data.getData();

           if (pdfData.toString().startsWith("content://")){
               Cursor cursor = null;
               try {
                   cursor = PdfActivity.this.getContentResolver().query(pdfData,null,null,null);
                   if (cursor != null && cursor.moveToFirst()){
                       pdfName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                   }
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }
        }else if (pdfData.toString().startsWith("file://")){
            pdfName = new File(pdfData.toString()).getName();
        }
        pdf_textview.setText(pdfName);
}
}