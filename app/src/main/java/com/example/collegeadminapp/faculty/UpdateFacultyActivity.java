package com.example.collegeadminapp.faculty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.collegeadminapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UpdateFacultyActivity extends AppCompatActivity {

    FloatingActionButton fab;

    private RecyclerView csDepartment,eeDepartment,ecDepartment,meDepartment,ceDepartment,oeDepartment;
    private LinearLayout csNoData,eeNoData,ecNoData,meNoData,ceNoData,oeNoData;
    private List<TeacherData> list1, list2,list3,list4,list5,list6;
    private TeacherAdapter adapter;

    private DatabaseReference reference, dbRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty);

        csDepartment = findViewById(R.id.csDepartment);
        eeDepartment = findViewById(R.id.eeDepartment);
        ecDepartment = findViewById(R.id.ecDepartment);
        meDepartment = findViewById(R.id.meDepartment);
        ceDepartment = findViewById(R.id.ceDepartment);
        oeDepartment = findViewById(R.id.oeDepartment);

        csNoData = findViewById(R.id.csNoData);
        eeNoData = findViewById(R.id.eeNoData);
        ecNoData = findViewById(R.id.ecNoData);
        meNoData = findViewById(R.id.meNoData);
        ceNoData = findViewById(R.id.ceNoData);
        oeNoData = findViewById(R.id.oeNoData);

        reference = FirebaseDatabase.getInstance().getReference().child("faculty");

        csDepartment();
        ecDepartment();
        eeDepartment();
        meDepartment();
        ceDepartment();
        oeDepartment();

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateFacultyActivity.this, AddTeacherActivity.class));
            }
        });
    }

    private void csDepartment() {
        dbRef = reference.child("Computer Science");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list1 = new ArrayList<>();
                if (!snapshot.exists()){
                    csNoData.setVisibility(View.VISIBLE);
                    csDepartment.setVisibility(View.GONE);
                }else {
                    csNoData.setVisibility(View.GONE);
                    csDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        TeacherData data = snapshot1.getValue(TeacherData.class);
                        list1.add(data);
                    }
                    csDepartment.setHasFixedSize(true);
                    csDepartment.setLayoutManager(new LinearLayoutManager(UpdateFacultyActivity.this));
                    adapter = new TeacherAdapter(list1, UpdateFacultyActivity.this,"Computer Science");
                    csDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFacultyActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void eeDepartment() {
        dbRef = reference.child("Electrical");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list2 = new ArrayList<>();
                if (!snapshot.exists()){
                    eeNoData.setVisibility(View.VISIBLE);
                    eeDepartment.setVisibility(View.GONE);
                }else {
                    eeNoData.setVisibility(View.GONE);
                    eeDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        TeacherData data = snapshot1.getValue(TeacherData.class);
                        list2.add(data);
                    }
                    eeDepartment.setHasFixedSize(true);
                    eeDepartment.setLayoutManager(new LinearLayoutManager(UpdateFacultyActivity.this));
                    adapter = new TeacherAdapter(list2, UpdateFacultyActivity.this,"Electrical");
                    eeDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFacultyActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ecDepartment() {
        dbRef = reference.child("Electronics");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list3 = new ArrayList<>();
                if (!snapshot.exists()){
                    ecNoData.setVisibility(View.VISIBLE);
                    ecDepartment.setVisibility(View.GONE);
                }else {
                    ecNoData.setVisibility(View.GONE);
                    ecDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        TeacherData data = snapshot1.getValue(TeacherData.class);
                        list3.add(data);
                    }
                    ecDepartment.setHasFixedSize(true);
                    ecDepartment.setLayoutManager(new LinearLayoutManager(UpdateFacultyActivity.this));
                    adapter = new TeacherAdapter(list3, UpdateFacultyActivity.this,"Electronics");
                    ecDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFacultyActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void meDepartment() {
        dbRef = reference.child("Mechanical");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list4 = new ArrayList<>();
                if (!snapshot.exists()){
                    meNoData.setVisibility(View.VISIBLE);
                    meDepartment.setVisibility(View.GONE);
                }else {
                    meNoData.setVisibility(View.GONE);
                    meDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        TeacherData data = snapshot1.getValue(TeacherData.class);
                        list4.add(data);
                    }
                    meDepartment.setHasFixedSize(true);
                    meDepartment.setLayoutManager(new LinearLayoutManager(UpdateFacultyActivity.this));
                    adapter = new TeacherAdapter(list4, UpdateFacultyActivity.this,"Mechanical");
                    meDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFacultyActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ceDepartment() {
        dbRef = reference.child("Civil");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list5 = new ArrayList<>();
                if (!snapshot.exists()){
                    ceNoData.setVisibility(View.VISIBLE);
                    ceDepartment.setVisibility(View.GONE);
                }else {
                    ceNoData.setVisibility(View.GONE);
                    ceDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        TeacherData data = snapshot1.getValue(TeacherData.class);
                        list5.add(data);
                    }
                    ceDepartment.setHasFixedSize(true);
                    ceDepartment.setLayoutManager(new LinearLayoutManager(UpdateFacultyActivity.this));
                    adapter = new TeacherAdapter(list5, UpdateFacultyActivity.this,"Civil");
                    ceDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFacultyActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void oeDepartment() {
        dbRef = reference.child("Others");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list6 = new ArrayList<>();
                if (!snapshot.exists()){
                    oeNoData.setVisibility(View.VISIBLE);
                    oeDepartment.setVisibility(View.GONE);
                }else {
                    oeNoData.setVisibility(View.GONE);
                    oeDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        TeacherData data = snapshot1.getValue(TeacherData.class);
                        list6.add(data);
                    }
                    oeDepartment.setHasFixedSize(true);
                    oeDepartment.setLayoutManager(new LinearLayoutManager(UpdateFacultyActivity.this));
                    adapter = new TeacherAdapter(list6, UpdateFacultyActivity.this,"Others");
                    oeDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFacultyActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}