package com.toryx.vrestodemaergodoti;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static android.view.View.INVISIBLE;

public class Stats extends AppCompatActivity {
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter2;
    private ListView dataListView;
    private ListView dataListView2;
    private TextView cond;
    private TextView par;
    private TextView apo;
    private ImageView backst;
    private FirebaseAuth firebaseAuth;
    ArrayList<String> listid;
    ArrayList<String>  nameid;
    ArrayList<String>  ggid;
    DatabaseReference dbRef;
    DatabaseReference dbRef2;
    Button b;
    ArrayList<String> onomata = new ArrayList<>();
    ArrayList<String> onomata2 = new ArrayList<>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        firebaseAuth = FirebaseAuth.getInstance();
        String Id=getIntent().getStringExtra("ID");
        String name=getIntent().getStringExtra("nam");
        listid = new ArrayList<>();
        nameid = new ArrayList<>();
        ggid = new ArrayList<>();
        dataListView = (ListView) findViewById(R.id.listrec);
        dataListView2 = (ListView) findViewById(R.id.listrec2);
        cond =  findViewById(R.id.condition);
        EditText srh= findViewById(R.id.pelatis2);
        par = findViewById(R.id.textView7);
        apo = findViewById(R.id.textView8);
        backst= findViewById(R.id.back2);
        dbRef = database.getReference().child("Pelates").child(Id).child("Send");
        dbRef2 = database.getReference().child("Pelates").child(Id).child("Received");

        backst.setOnClickListener(new View.OnClickListener() { public void onClick(View v) {
            finish();
        }});
        adapter = new ArrayAdapter<String>(this,R.layout.demata, onomata);
        adapter2 = new ArrayAdapter<String>(this,R.layout.demata, onomata2);
        dataListView.setAdapter(adapter);
        dataListView2.setAdapter(adapter2);
        addChildEventListener();
        addChildEventListener2();
        backst.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        finish();
    }
});
        srh.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                (Stats.this).adapter.getFilter().filter("Κωδικός : "+s);
                (Stats.this).adapter2.getFilter().filter("Κωδικός : "+s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cond.setText("Αποστολές/Παραλαβές απο "+name);

        par.setOnClickListener(new View.OnClickListener() {

    @SuppressLint("ResourceType")
    @Override
    public void onClick(View v) {
        par.setTextColor(Color.BLACK);
        apo.setTextColor(Color.WHITE);
        apo.setBackgroundResource(R.drawable.gradientbackground);
        par.setBackgroundResource(Color.TRANSPARENT);
        dataListView.setVisibility(View.VISIBLE);
        dataListView2.setVisibility(View.GONE);

    }
});

        apo.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                apo.setTextColor(Color.BLACK);
                par.setTextColor(Color.WHITE);
                par.setBackgroundResource(R.drawable.gradientbackground);
                apo.setBackgroundResource(Color.TRANSPARENT);
                dataListView2.setVisibility(View.VISIBLE);
                dataListView.setVisibility(View.GONE);
            }
        });




    }

    private void addChildEventListener() {
        ChildEventListener childListener = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

String data= (String) "Κωδικός : "+ dataSnapshot.child("code").getValue()+" \nΚατάσταση : "+dataSnapshot.child("cond").getValue()+"\nΑποστολέας : "+dataSnapshot.child("friend").getValue();
                adapter.add(data);
                Log.e("ggwp",data);
                nameid.add((String)  dataSnapshot.child("cond").getValue());
                ggid.add((String)  dataSnapshot.child("friend").getValue());

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
    private void addChildEventListener2() {
        ChildEventListener childListener = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String data2= (String) "Κωδικός : "+ dataSnapshot.child("code").getValue()+" \nΚατάσταση : "+dataSnapshot.child("cond").getValue()+"\nΑποστολέας : "+dataSnapshot.child("friend").getValue();
                adapter2.add(data2);
                Log.e("ggwp",data2);
                nameid.add((String)  dataSnapshot.child("cond").getValue());
                ggid.add((String)  dataSnapshot.child("friend").getValue());

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
        dbRef2.addChildEventListener(childListener);
    }
    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.mainmenu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                //noinspection SimplifiableIfStatement
                if (id == R.id.UpdateSettings) {

                    startActivity(new Intent(Stats.this, ProfileAct.class));

                }
                if (id == R.id.exitus) {
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(Stats.this, Pelates.class));


                }
                return false;
            }
        });
        popup.show();
    }
}

