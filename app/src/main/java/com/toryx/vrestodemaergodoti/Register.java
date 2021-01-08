package com.toryx.vrestodemaergodoti;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Register extends AppCompatActivity {
    private EditText userName,userPass,userEmail;
    private Button regi;
    private TextView ret;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private DatabaseReference mDatabase;
    private ProgressDialog progressDialog;
    String email="";
    String name="";
    String tel="";
    private TextView usertel;
    String password="";
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        assign();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        progressDialog = new ProgressDialog(this);


        regi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    progressDialog.setMessage("Γινεται Εγγραφή...");
                    progressDialog.show();
                    //Ανεβάζω δεδομενα
                    String usermail = userEmail.getText().toString().trim();
                    String userpass = userPass.getText().toString().trim();
                    firebaseAuth.createUserWithEmailAndPassword(usermail,userpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                        @Override

                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if(task.isSuccessful()){
                                sendD();
                                sendE();
                                mDatabase = FirebaseDatabase.getInstance().getReference();


                            }else { Toast.makeText(Register.this,"Εγγραφή Μη Επιτυχής",Toast.LENGTH_SHORT).show();}

                        }
                    });
                }
            }
        });
        ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(Register.this,MainActivity.class));
            }
        });
    }

    private void assign(){
        userName= (EditText)findViewById(R.id.namelab);
        userPass= (EditText)findViewById(R.id.pass);
        userEmail= (EditText)findViewById(R.id.emaillab);
        usertel=(EditText)findViewById(R.id.tellab);
        regi=(Button) findViewById(R.id.editli);
        ret=(TextView) findViewById(R.id.retu);



    }

    private Boolean validate(){

        Boolean res=false;
        name=userName.getText().toString();
        password = userPass.getText().toString();
        email = userEmail.getText().toString();
        tel=usertel.getText().toString();
        if(name.isEmpty() || password.isEmpty() || email.isEmpty() ){

            showAlert();


        }
        else { res = true;
        }
        return res;
    }
    public void showAlert(){
        AlertDialog.Builder LoginFailed= new AlertDialog.Builder(this);
        LoginFailed.setMessage("Παρακαλώ συμπλήρωσε όλα τα κενα")
                .setNegativeButton("Δοκίμασε Ξανά...", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        LoginFailed.show();


    }

    private void sendE(){
        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(Register.this, "Επιτυχής Εγγραφή.Εχει σταλεί E-mail επιβεβαίωσης", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        finish();
                    } else {
                        Toast.makeText(Register.this, "Δεν θα σταλεί E-mail επιβεβαίωσης", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }
    private void sendD(){

        FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
        DatabaseReference myref= firebaseDatabase.getReference().child("Pelates").child(firebaseAuth.getUid());

        UserProfile userProfile=new UserProfile(name,email,tel);
        myref.setValue(userProfile);
    }


}


