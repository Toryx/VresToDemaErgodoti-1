package com.toryx.vrestodemaergodoti;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Update_P extends AppCompatActivity {

private EditText nameedited;
private EditText emailedited;
private EditText teledited;
private FirebaseUser firebaseUser;
private EditText passedited;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__p);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        passedited=findViewById(R.id.telpass);
        nameedited= findViewById(R.id.tellab);
        emailedited=findViewById(R.id.emaillab);
        teledited=findViewById(R.id.telab);

        Button changes = findViewById(R.id.editli);
        TextView re = findViewById(R.id.returnbu);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();



        final DatabaseReference dbRef = firebaseDatabase.getReference().child("Pelates").child(firebaseAuth.getUid());

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                assert userProfile != null;
                nameedited.setText(userProfile.getUserN());
                emailedited.setText(userProfile.getUserE());
                teledited.setText(userProfile.getUserT());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Update_P.this,databaseError.getCode(),Toast.LENGTH_LONG).show();
            }
        });

changes.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String name = nameedited.getText().toString();
        String email = emailedited.getText().toString();
        String tel = teledited.getText().toString();
        String userpass = passedited.getText().toString();
        Log.e("usp",userpass);
        if(userpass.equals("")){Toast.makeText(Update_P.this, "Παρακαλώ συμπληρώστε κωδικό πρόσβασης", Toast.LENGTH_SHORT).show();}
        else {
            UserProfile userProfile = new UserProfile(name, email, tel);
            dbRef.setValue(userProfile);
            firebaseUser.updatePassword(userpass).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(Update_P.this, "Επιτυχής Επεξεργασία Προφίλ", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(Update_P.this, "Μη Επιτυχής Επεξεργασία Προφίλ", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            // Toast.makeText(Update_P.this, "Επιτυχής Επεξέργασία Προφίλ", Toast.LENGTH_SHORT).show();


            finish();
        }
    }
});

        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }

        });



    }

}
