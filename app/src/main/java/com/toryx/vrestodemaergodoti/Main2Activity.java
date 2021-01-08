package com.toryx.vrestodemaergodoti;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.jakewharton.processphoenix.ProcessPhoenix;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.IdentityScope;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Main2Activity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference refe;
    private DatabaseReference mDatabase;
    private FirebaseStorage firebaseStorage;
    private Button exit;
    private LinearLayout apstles;
    private LinearLayout prlvs;
    private LinearLayout pela;
    public Intent intentforcity;
    private Button re;
    private static final String FILE_NAME = "example.txt";
    EditText nameap;
    EditText emailap;
    EditText tilap;
    EditText namepa;
    EditText emailpa;
    EditText tilpa;
    String pros = "";
    String antikatavoli = "N";
    String paralavi = "N";
    String code = "";
    int apostoleas = -1;
    int paraliptis = -1;
    int Nouser;
    Code element;
    ArrayAdapter<String> myAdapter;
    ArrayAdapter<String> temp1;
    ArrayAdapter<String> myAdapter2;
    CodeForCustomer elemcus;
    ArrayList<String> tils = new ArrayList<>();
    ArrayList<String> emai = new ArrayList<>();
    ArrayList<String> sessionId = new ArrayList<>();
    ArrayList<String> IDs = new ArrayList<>();
    CheckedTextView anti;
    boolean filtered = false;
    CheckedTextView para;
    TextView kalosorisma;
    String workplace = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        exit = findViewById(R.id.testaki);
        apstles = findViewById(R.id.apo);
        prlvs = findViewById(R.id.par);
        pela = findViewById(R.id.pel);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        anti = findViewById(R.id.antikatavoli);
        para = findViewById(R.id.paradosi);
        nameap = findViewById(R.id.nameapostolea);
        emailap = findViewById(R.id.emailapostolea);
        tilap = findViewById(R.id.tilapostolea);
        final EditText srh = findViewById(R.id.searchpara);
        final EditText srh2 = findViewById(R.id.searchpara2);
        kalosorisma = findViewById(R.id.textView);
        namepa = findViewById(R.id.nameparalipti);
        emailpa = findViewById(R.id.emailparalipti);
        tilpa = findViewById(R.id.tilparalipti);
        refe = FirebaseDatabase.getInstance().getReference().child("Codes");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Pelates");
        element = new Code();
        elemcus = new CodeForCustomer();



        if (getIntent().getExtras() != null) {

            sessionId = getIntent().getStringArrayListExtra("EXTRA_SESSION_ID");
            IDs = getIntent().getStringArrayListExtra("EXTRA_SESSION_ID2");
            emai = getIntent().getStringArrayListExtra("EXTRA_SESSION_ID3");
            tils = getIntent().getStringArrayListExtra("EXTRA_SESSION_ID4");
            workplace = getIntent().getStringExtra("WORKSPACE");
            if (!emai.get(0).equals("Ήδη Μέλος;")) {
                emai.add(0, "Ήδη Μέλος;");
                IDs.add(0, "");
                sessionId.add(0, "");
                tils.add(0, "");
                emailap.getText().clear();
                emailpa.getText().clear();
            }
        } else {
            sessionId.add("");
            emai.add("");
            tils.add("");
            IDs.add("");

        }

        Spinner mySpinner = (Spinner) findViewById(R.id.listaapostolea);

        myAdapter = new ArrayAdapter<String>(Main2Activity.this,
                R.layout.customspinner, emai);

        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int pos = 0;
                for (int j = 0; j < emai.size(); j++) {
                    if (myAdapter.getItem(i).equals(emai.get(j))) {
                        pos = j;
                    }
                }
                Log.e("true", myAdapter.getItem(i));
                Log.e("true", emai.get(pos));
                Log.e("true", String.valueOf(pos));

                if (srh.getText().equals("")) {
                    nameap.setText(sessionId.get(pos));
                    tilap.setText(tils.get(pos));
                    if (i == 0) {
                        emailap.setText("");
                        Nouser = 1;
                        apostoleas = -1;
                    } else {
                        emailap.setText(emai.get(pos));
                        Nouser = 0;
                        apostoleas = pos;
                    }

                } else {
                    nameap.setText(sessionId.get(pos));
                    tilap.setText(tils.get(pos));
                    emailap.setText(emai.get(pos));
                    Nouser = 0;
                    apostoleas = pos;
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
        srh.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                (Main2Activity.this).myAdapter.getFilter().filter(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        srh2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                (Main2Activity.this).myAdapter2.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Spinner mySpinner2 = (Spinner) findViewById(R.id.listaparalipti);

        myAdapter2 = new ArrayAdapter<String>(Main2Activity.this,
                R.layout.customspinner, emai);

        myAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner2.setAdapter(myAdapter2);

        mySpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int pos = 0;
                for (int j = 0; j < emai.size(); j++) {
                    if (myAdapter2.getItem(i).equals(emai.get(j))) {
                        pos = j;
                    }
                }
                Log.e("true", myAdapter2.getItem(i));
                Log.e("true", emai.get(pos));
                Log.e("true", String.valueOf(pos));

                if (srh2.getText().equals("")) {
                    namepa.setText(sessionId.get(pos));
                    tilpa.setText(tils.get(pos));
                    if (i == 0) {
                        emailpa.setText("");
                        Nouser = 1;
                        paraliptis = -1;
                    } else {
                        emailpa.setText(emai.get(pos));
                        Nouser = 0;
                        paraliptis = pos;
                    }

                } else {
                    namepa.setText(sessionId.get(pos));
                    tilpa.setText(tils.get(pos));
                    emailpa.setText(emai.get(pos));
                    Nouser = 0;
                    paraliptis = pos;
                }


            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        DatabaseReference dbRef = firebaseDatabase.getReference().child("Pelates").child(firebaseAuth.getUid());
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                kalosorisma.setText("Συνδεδεμένος Χρήστης : " + userProfile.getUserN());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Main2Activity.this, databaseError.getCode(), Toast.LENGTH_LONG).show();
            }
        });


        Spinner mySpinner3 = (Spinner) findViewById(R.id.eidosdematos);

        ArrayAdapter<String> myAdapter3 = new ArrayAdapter<String>(Main2Activity.this,
                R.layout.customspinner, getResources().getStringArray(R.array.send));

        myAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner3.setAdapter(myAdapter3);

        mySpinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 1) {
                    pros = "b";
                } else if (i == 2) {
                    pros = "a";
                } else if (i == 3) {
                    pros = "n";
                } else if (i == 4) {
                    pros = "t";
                } else if (i == 0) {
                    pros = "";

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        anti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (anti.isChecked()) {
                    anti.setChecked(false);
                    antikatavoli = "N";
                } else {
                    anti.setChecked(true);
                    antikatavoli = "Y";
                }
            }
        });

        para.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (para.isChecked()) {
                    para.setChecked(false);
                    paralavi = "N";
                } else {
                    para.setChecked(true);
                    paralavi = "Y";
                }
            }
        });


        exit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (pros.equals("")) {
                    Toast.makeText(Main2Activity.this, "Διάλεξε Προορισμό", Toast.LENGTH_SHORT).show();
                } else {
                    Calendar cal = Calendar.getInstance();
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmm");//
                    code = dateFormat.format(cal.getTime());
                    code = code + workplace + pros + antikatavoli + paralavi;
                    final AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
                    builder.setCancelable(true);
                    builder.setTitle("Επιβεβαίωση Αποστολής");
                    builder.setMessage("Θα σταλθεί είδοποίηση");
                    builder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    element.setApostoleas(nameap.getText().toString().trim());
                                    element.setParaliptis(namepa.getText().toString().trim());
                                    element.setCond("Στην αναμονή για αποστολή");
                                    element.setCode(code);
                                    refe.push().setValue(element);
                                    if (apostoleas > -1) {
                                        elemcus.setFriend(namepa.getText().toString().trim());
                                        elemcus.setCond("Στην αναμονή για αποστολή");
                                        elemcus.setCode(code);
                                        mDatabase.child(IDs.get(apostoleas)).child("Send").push().setValue(elemcus);
                                    }
                                    if (paraliptis > -1) {
                                        elemcus.setFriend(nameap.getText().toString().trim());
                                        elemcus.setCond("Στην αναμονή για αποστολή");
                                        elemcus.setCode(code);
                                        mDatabase.child(IDs.get(paraliptis)).child("Received").push().setValue(elemcus);

                                        finish();
                                        overridePendingTransition(0, 0);
                                        startActivity(getIntent());
                                        overridePendingTransition(0, 0);

                                    }
                                }
                            });
                    builder.setNegativeButton("Ακύρωση", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });


                    AlertDialog dialog = builder.create();
                    dialog.show();


                }
            }
        });


        apstles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Apostoles.class);
                startActivity(intent);
            }
        });
        prlvs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Paralaves.class);
                startActivity(intent);
            }
        });
        pela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Pelates.class);

                intent.putExtra("EXTRA_SESSION_ID", sessionId);
                intent.putExtra("EXTRA_SESSION_ID3", emai);
                intent.putExtra("EXTRA_SESSION_ID2", IDs);
                intent.putExtra("EXTRA_SESSION_ID4", tils);
                intent.putExtra("WORKSPACE", workplace);
                startActivity(intent);
            }
        });

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

                    startActivity(new Intent(Main2Activity.this, ProfileAct.class));

                }
                if (id == R.id.exitus) {
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(Main2Activity.this, MainActivity.class));


                }
                return false;
            }
        });
        popup.show();
    }
}