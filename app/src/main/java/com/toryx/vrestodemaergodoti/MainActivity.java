package com.toryx.vrestodemaergodoti;


import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private TextView username;
    private TextView password;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    String meros="";
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = database.getReference().child("Pelates");
    ArrayList<String> phoneNumbers = new ArrayList<>();
    ArrayList<String> Emails = new ArrayList<>();
    ArrayList<String> Id = new ArrayList<>();
    ArrayList<String> Til = new ArrayList<>();
    RelativeLayout rellay1, rellay2;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rellay1 = (RelativeLayout) findViewById(R.id.rellay1);
        rellay2 = (RelativeLayout) findViewById(R.id.rellay2);

        handler.postDelayed(runnable, 2000); //2000 is the timeout for the splash
        Button eisodos = (Button) findViewById(R.id.buttoneisodoy);
        username = (TextView) findViewById(R.id.UsernameLogin);
        password = (TextView) findViewById(R.id.PasswordID);
        Button register = (Button) findViewById(R.id.register);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        addChildEventListener();





        eisodos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.setText("ilias12tory@gmail.com");
                password.setText("qqqqqq");
                meros="t";
                if(username.getText().toString().matches("") || password.getText().toString().matches("") ){
                    Toast.makeText(MainActivity.this, "Παρακαλώ συμπληρώστε Ονομα Χρήστη και Κωδικο", Toast.LENGTH_LONG).show();}
                else{validate(username.getText().toString(), password.getText().toString());
                    password.setText("");}
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegister();
            }
        });

        Spinner mySpinner = (Spinner) findViewById(R.id.spinner1);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(MainActivity.this,
                R.layout.customspinner, getResources().getStringArray(R.array.names));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 1) {
                    meros = "b";
                } else if (i == 2) {
                    meros = "a";
                } else if (i == 3) {
                    meros = "n";
                } else if (i == 4) {
                    meros = "t";
                } else if (i == 0) {


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public void openRegister() {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);


    }

    private void validate(String Usern, String Userp) {

       // progressDialog.setMessage("Αυθεντικοποιηση...");

        progressDialog.show();
        progressDialog.setContentView(R.layout.pd);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        firebaseAuth.signInWithEmailAndPassword(Usern, Userp).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override

            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    checkE();
                } else {
                    Toast.makeText(MainActivity.this, "Εισοδος Απετυχε", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private void checkE() {


        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        Boolean emailflag = firebaseUser.isEmailVerified();
        if(emailflag) {

            Intent intent = new Intent(getBaseContext(), Main2Activity.class);
            intent.putExtra("EXTRA_SESSION_ID", phoneNumbers);
            intent.putExtra("EXTRA_SESSION_ID3", Emails);
            intent.putExtra("EXTRA_SESSION_ID2", Id);
            intent.putExtra("EXTRA_SESSION_ID4", Til);
            intent.putExtra("WORKSPACE", meros);
            if(meros.equals("")){Toast.makeText(MainActivity.this,"Διάλεξε μέρος",Toast.LENGTH_SHORT).show(); }
            else { Toast.makeText(MainActivity.this, "Εισοδος Πετυχε,Καλως Ηρθες", Toast.LENGTH_SHORT).show();
            startActivity(intent);}
        }
        else{Toast.makeText(MainActivity.this, "Εισοδος Aπέτυχε.Δεν εχεις κανει αυθεντικοποιηση του E-mail", Toast.LENGTH_SHORT).show();
             firebaseAuth.signOut();
        }

    }

    private void addChildEventListener() {
        ChildEventListener childListener = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Id.add(dataSnapshot.getKey());
                phoneNumbers.add((String)  dataSnapshot.child("userN").getValue());
                Emails.add((String)  dataSnapshot.child("userE").getValue());
                Til.add((String)  dataSnapshot.child("userT").getValue());


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
        dbRef.addChildEventListener(childListener);
    }
}
