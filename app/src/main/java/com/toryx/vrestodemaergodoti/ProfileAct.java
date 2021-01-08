package com.toryx.vrestodemaergodoti;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ProfileAct extends AppCompatActivity {
    private TextView profname,profemail,retupr,proftel,arithpel,arapo,arpar;
    private Button editpr;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference alldata = database.getReference().child("Pelates");
    private DatabaseReference allcodes = database.getReference().child("Codes");

    private int pel=0;
    private int apo=0;
    private int par=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        profname= findViewById(R.id.nameli);
        profemail=findViewById(R.id.emailli);

        editpr = findViewById(R.id.editli);

        retupr= findViewById(R.id.returnli);
        proftel = findViewById(R.id.teli);

        arithpel= findViewById(R.id.arithmospelatwn);
        arapo= findViewById(R.id.arithmosapostolwn);
        arpar= findViewById(R.id.arithmosparal);

        firebaseAuth= FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();



        //DatabaseReference databaseReference = firebaseDatabase.getReference();
        DatabaseReference dbRef = firebaseDatabase.getReference().child("Pelates").child(firebaseAuth.getUid());

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                proftel.setText(dataSnapshot.child("userT").getValue().toString());
                profname.setText(dataSnapshot.child("userN").getValue().toString());
                profemail.setText(dataSnapshot.child("userE").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileAct.this,databaseError.getCode(),Toast.LENGTH_LONG).show();
            }
        });




        editpr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startActivity(new Intent(ProfileAct.this,Update_P.class)); }});
        retupr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }

        });

        addChildEventListener();
        addChildEventListener2();

    }
    private void addChildEventListener() {

        ChildEventListener childListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                pel++;
                arithpel.setText(String.valueOf(pel));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        alldata.addChildEventListener(childListener);
    }
    private void addChildEventListener2() {

        ChildEventListener childListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String ve=(String) dataSnapshot.child("code").getValue();
                if (ve.charAt(8)=='t') {
                    apo++;

                }
                arapo.setText((String.valueOf(apo)));
                if (ve.charAt(9)=='t') {
                    par++;
                }
                arpar.setText((String.valueOf(par)));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        allcodes.addChildEventListener(childListener);
    }





}
