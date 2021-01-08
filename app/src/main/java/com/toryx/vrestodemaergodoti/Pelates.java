package com.toryx.vrestodemaergodoti;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

public class Pelates extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private ListView dataListView;
    private FirebaseDatabase firebaseDatabase;
    ArrayList<String> codes = new ArrayList<>();
    ArrayList<String> tils = new ArrayList<>();
    ArrayList<String> emai = new ArrayList<>();
    ArrayList<String> onomata = new ArrayList<>();
    ArrayList<String> IDs = new ArrayList<>();
    ArrayList<String> Id = new ArrayList<>();
    private EditText selectpelati;
    private TextView pa;
    private TextView ap;
    private ImageView pisw;
    ArrayAdapter<String> adapter;
    String gg;
    int x=0;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = database.getReference().child("Pelates");
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deck);

        pa= findViewById(R.id.textView7);
        ap= findViewById(R.id.textView8);


        dataListView = (ListView) findViewById(R.id.lists);
        firebaseAuth = FirebaseAuth.getInstance();
        pisw = findViewById(R.id.back2) ;
        pisw.setOnClickListener(new View.OnClickListener() { public void onClick(View v) {
            finish();
        }});
        // onomata = getIntent().getStringArrayListExtra("EXTRA_SESSION_ID");
            IDs = getIntent().getStringArrayListExtra("EXTRA_SESSION_ID2");
            emai = getIntent().getStringArrayListExtra("EXTRA_SESSION_ID3");
            tils = getIntent().getStringArrayListExtra("EXTRA_SESSION_ID4");
        selectpelati = findViewById(R.id.pelatis);
        firebaseDatabase = FirebaseDatabase.getInstance();




    adapter = new ArrayAdapter<String>(this, R.layout.names, onomata);
    dataListView.setAdapter(adapter);
        dataListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,  int position, long id) {


                       gg =adapter.getItem(position);

                        for(int i=0;i<codes.size();i++){
                        if(gg.equals(codes.get(i))){x=i;}

                        }
                        for(int i=0;i<adapter.getCount();i++){
                            parent.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                        }
                        parent.getChildAt(position).setBackgroundColor(Color.LTGRAY);

                        Log.e("P",gg);
                        Log.e("P",Id.get(x));

                        Intent intent = new Intent(Pelates.this, Stats.class);
                        intent.putExtra("ID", Id.get(x));
                        intent.putExtra("nam",gg);
                        startActivity(intent);


                    }
                });


        selectpelati.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                (Pelates.this).adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        addChildEventListener();




    }

    private void addChildEventListener() {
        ChildEventListener childListener = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


           adapter.add((String)  dataSnapshot.child("userN").getValue());
                Id.add((String)  dataSnapshot.getKey());
                codes.add((String)  dataSnapshot.child("userN").getValue());



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

                    startActivity(new Intent(Pelates.this, ProfileAct.class));

                }
                if (id == R.id.exitus) {
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(Pelates.this, MainActivity.class));


                }
                return false;
            }
        });
        popup.show();
    }


}

