package com.toryx.vrestodemaergodoti;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;


public class Paralaves extends AppCompatActivity {
    private ListView dataListView;
    private EditText itemText;
    private LinearLayout co;
    private Button deleteButton;
    private Button deleteButton2;
    private Button resetbut;
    private ImageView pisw;
    String sendname;
    boolean newsort2=true;
    LinearLayout filtrakoumpi;
    LinearLayout taxikoumpi2;
    DatabaseReference dbRef3;
    DatabaseReference dbRef4;
    private FirebaseAuth firebaseAuth;
    private LinearLayout filt;

    private int selectedPosition = 0;
    String[] dat = new String[5];
    ArrayList<String> gg = new ArrayList();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = database.getReference().child("Codes");
    private DatabaseReference dbRef2 = database.getReference().child("Pelates");

    ArrayList<String> listItems = new ArrayList<String>();
    ArrayList<String> listKeys = new ArrayList<String>();
    ArrayList<String[]> alld = new ArrayList<String[]>();
    ArrayAdapter<String> adapter;
    CheckedTextView prosapos;
    ArrayList<String> Id = new ArrayList<>();
    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> codessend = new ArrayList<>();
    ArrayList<String> codessendid = new ArrayList<>();
    ArrayList<String> codesrec = new ArrayList<>();
    ArrayList<String> codesrecid = new ArrayList<>();

    //Ολες οι Πολεις απο τα φιλτρα
    CheckedTextView city1;
    CheckedTextView city2;
    CheckedTextView city3;
    CheckedTextView city4;


    //antikatavoli filtro
    CheckedTextView filtroanti;
    CheckedTextView filtoparadosi;
    String workplace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paralaves);
        co = findViewById(R.id.filtranotp);
        firebaseAuth = FirebaseAuth.getInstance();
        dataListView = findViewById(R.id.listaapostolesp);
        itemText = findViewById(R.id.editTextp);
        deleteButton = findViewById(R.id.sendbutp);
        resetbut = findViewById(R.id.resetp);
        deleteButton.setEnabled(false);
        final Animation fadeIn = AnimationUtils.loadAnimation(Paralaves.this, android.R.anim.fade_in);
        final Animation fadeOut = AnimationUtils.loadAnimation(Paralaves.this, android.R.anim.fade_out);
        prosapos = findViewById(R.id.prosapostp);
        taxikoumpi2=findViewById(R.id.taxip);
        workplace=getIntent().getStringExtra("meros");
        //Ολες οι Πολεις απο τα φιλτρα
        city1 = findViewById(R.id.city1p);
        city2 = findViewById(R.id.city2p);
        city3 = findViewById(R.id.city3p);
        city4 = findViewById(R.id.city4p);


        filtroanti = findViewById(R.id.filtroantikatavolip);
        filtrakoumpi = findViewById(R.id.filtrp);
        pisw = findViewById(R.id.backp);
        pisw.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        filt = findViewById(R.id.filtersp);
        filtoparadosi = findViewById(R.id.filtroparadosikatoikonp);
        deleteButton2 = findViewById(R.id.sendbutp2);
        co.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filt.setVisibility(View.GONE);
                co.setVisibility(View.GONE);
                dataListView.setEnabled(true);
            }
        });

        //  ΔΗΜΙΟΥΡΓΙΑ ΤΗΣ ΛΙΣΤΑΣ
        adapter = new ArrayAdapter<String>(this, R.layout.simplechoice, listItems);
        dataListView.setAdapter(adapter);
        dataListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        dataListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selectedPosition = position;
                        deleteButton.setEnabled(true);
                        String data = dataListView.getItemAtPosition(position).toString();
                        Toast.makeText(Paralaves.this, data, Toast.LENGTH_LONG).show();


                    }
                });

        if(workplace.equals("t")){city1.setVisibility(View.GONE);}
        if(workplace.equals("b")){city3.setVisibility(View.GONE);}
        if(workplace.equals("a")){city2.setVisibility(View.GONE);}
        if(workplace.equals("n")){city4.setVisibility(View.GONE);}

        addChildEventListener();
        addChildEventListener2();

        taxikoumpi2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> temp=new ArrayList<>();
                for(int i=0;adapter.getCount()>i;i++){temp.add(adapter.getItem(adapter.getCount()-i-1));}
                adapter.clear();
                if(newsort2){ newsort2=false;
                    Toast.makeText(Paralaves.this, "Ταξινόμηση βάση του πιο παλιού", Toast.LENGTH_LONG).show(); }
                else{newsort2=true;
                    Toast.makeText(Paralaves.this, "Ταξινόμηση βάση του πιο πρόσφατου", Toast.LENGTH_LONG).show(); }

                for(int i=0;temp.size()>i;i++){adapter.add(temp.get(i));}
            }
        });


        // ΦΙΛΤΡΟ ΓΙΑ ΤΟΝ ΚΩΔΙΚΟ - ΑΜΕΣΗ ΑΝΑΝΕΩΣΗ ΤΗΣ ΛΙΣΤΑΣ
        itemText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                (Paralaves.this).adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        // OnClickListener για τα ΦΙΛΤΡΑ
        filtrakoumpi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filt.getVisibility() == View.VISIBLE) {

                    fadeOut.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            filt.setVisibility(View.GONE);
                            dataListView.setEnabled(true);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    filt.startAnimation(fadeOut);
                } else {
                    filt.setVisibility(View.VISIBLE);
                    co.setVisibility(View.VISIBLE);
                    filt.startAnimation(fadeIn);
                    dataListView.setEnabled(false);
                }

            }
        });
        // OnClickListener για τα ΠΡΟΣ ΑΠΟΣΤΟΛΗ
        prosapos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (prosapos.isChecked()) {
                    prosapos.setChecked(false);
                    Toast.makeText(Paralaves.this, "Εμφανίζονται όλα τα δέματα", Toast.LENGTH_LONG).show();
                    adapter.clear();
                    addChildEventListener();


                } else {
                    prosapos.setChecked(true);

                    Toast.makeText(Paralaves.this, "Εμφανίζονται μόνο τα προς παραλαβή ή τα καθοδόν", Toast.LENGTH_LONG).show();
                    adapter.clear();
                    for (String[] s : alld) {
                        if ((s[3].equals("Καθοδόν") || s[3].equals("Προς Παραλαβή")) && s[0].charAt(9) == workplace.charAt(0) ) {
                            adapter.add(s[0] + " | " + s[3]);

                        }

                    }

                }
            }
        });

//OnClickListener για κάθε πόλη
//ΘΕΣΣΑΛΟΝΙΚΗ ΑΠΟΣΤΟΛΗ
        city1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (city1.isChecked()) {

                } else {

                    city1.setChecked(true);
                    city2.setChecked(false);
                    city3.setChecked(false);
                    city4.setChecked(false);
                    Toast.makeText(Paralaves.this, "Αποστολή μόνο από Θεσσαλονίκη", Toast.LENGTH_LONG).show();
                    adapter.clear();
                    for (String[] s : alld) {
                        if (s[0].charAt(8) == 't') {
                            adapter.add(s[0] + " | " + s[3]);
                        }
                    }


                }
            }
        });
//ΑΛΕΞΑΝΔΡΕΙΑ ΑΠΟΣΤΟΛΗ
        city2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (city2.isChecked()) {

                } else {

                    city2.setChecked(true);
                    city1.setChecked(false);
                    city3.setChecked(false);
                    city4.setChecked(false);
                    Toast.makeText(Paralaves.this, "Αποστολή μόνο από Αλεξάνδρεια", Toast.LENGTH_LONG).show();
                    adapter.clear();
                    for (String[] s : alld) {
                        if (s[0].charAt(8) == 'a') {
                            adapter.add(s[0] + " | " + s[3]);
                        }
                    }


                }
            }
        });
//ΒΕΡΟΙΑ ΑΠΟΣΤΟΛΗ
        city3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (city3.isChecked()) {

                } else {
                    city3.setChecked(true);
                    city1.setChecked(false);
                    city2.setChecked(false);
                    city4.setChecked(false);
                    Toast.makeText(Paralaves.this, "Αποστολή μόνο από Βέροια", Toast.LENGTH_LONG).show();
                    adapter.clear();
                    for (String[] s : alld) {
                        if (s[0].charAt(8) == 'b') {
                            adapter.add(s[0] + " | " + s[3]);
                        }
                    }


                }
            }
        });
//ΝΑΟΥΣΑ ΑΠΟΣΤΟΛΗ
        city4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (city4.isChecked()) {

                } else {
                    city4.setChecked(true);
                    city1.setChecked(false);
                    city2.setChecked(false);
                    city3.setChecked(false);
                    adapter.clear();
                    Toast.makeText(Paralaves.this, "Αποστολή μόνο από Νάουσα", Toast.LENGTH_LONG).show();
                    for (String[] s : alld) {
                        if (s[0].charAt(8) == 'n') {
                            adapter.add(s[0] + " | " + s[3]);
                        }
                    }


                }
            }
        });

        //φιλτρο αντικαταβολή
        filtroanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filtroanti.isChecked()) {

                } else {
                    filtroanti.setChecked(true);
                    Toast.makeText(Paralaves.this, "Με αντικαταβολή", Toast.LENGTH_LONG).show();
                    int x = adapter.getCount();
                    for (int i = 0; i < x; i++) {
                        String s = adapter.getItem(i);
                        if (s.charAt(10) == 'Y') {
                            Log.e("qq", s);
                            gg.add(s);
                        }
                    }
                    adapter.clear();
                    for (int i = 0; i < gg.size(); i++) {
                        adapter.add(gg.get(i));
                    }
                    gg.clear();


                }
            }
        });
        //φιλτρο αντικαταβολή
        filtoparadosi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filtoparadosi.isChecked()) {

                } else {
                    filtoparadosi.setChecked(true);
                    Toast.makeText(Paralaves.this, "Παράδοση Κατ΄οίκον", Toast.LENGTH_LONG).show();
                    int x = adapter.getCount();
                    for (int i = 0; i < x; i++) {
                        String s = adapter.getItem(i);
                        if (s.charAt(11) == 'Y') {
                            Log.e("qq", s);
                            gg.add(s);
                        }
                    }
                    adapter.clear();
                    for (int i = 0; i < gg.size(); i++) {
                        adapter.add(gg.get(i));
                    }
                    gg.clear();
                }
            }
        });


        resetbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.clear();

                addChildEventListener();
                city1.setChecked(false);
                city2.setChecked(false);
                city3.setChecked(false);
                city4.setChecked(false);
                filtroanti.setChecked(false);
                filtoparadosi.setChecked(false);


            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = dataListView.getItemAtPosition(selectedPosition).toString();
                    Toast.makeText(Paralaves.this, "To δέμα παραληφθηκε στο κατάστημα", Toast.LENGTH_LONG).show();
                    deleteItem(v,true);
            }
        });
        deleteButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = dataListView.getItemAtPosition(selectedPosition).toString();
                    Toast.makeText(Paralaves.this, "To δέμα παραλήφθηκε από τον πελάτη", Toast.LENGTH_LONG).show();
                    deleteItem(v,false);
            }
        });


    }

    private void addChildEventListener() {
        alld.clear();
        ChildEventListener childListener = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String ve = (String) dataSnapshot.child("code").getValue();
                String ant = "Χωρίς Αντικαταβολή";


                if(ve.charAt(9) == workplace.charAt(0)){
                    adapter.add((String) dataSnapshot.child("code").getValue() + " | " + dataSnapshot.child("cond").getValue());

                    if (ve.charAt(10) == 'Y') {
                        ant = "Με Αντικαταβολή";
                    }}
                    dat = new String[]{dataSnapshot.child("code").getValue().toString(), dataSnapshot.child("apostoleas").getValue().toString(), dataSnapshot.child("paraliptis").getValue().toString(), dataSnapshot.child("cond").getValue().toString(), ant};
                    alld.add(dat);


                listKeys.add(dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                int index = listKeys.indexOf(key);

                if (index != -1) {
                    listItems.remove(index);
                    listKeys.remove(index);
                    adapter.notifyDataSetChanged();
                }
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
                Id.add(dataSnapshot.getKey());
                names.add((String)  dataSnapshot.child("userN").getValue());

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
    private void addChildEventListener3() {
        ChildEventListener childListener = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                codessendid.add(dataSnapshot.getKey());
                codessend.add((String)  dataSnapshot.child("code").getValue());


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
        dbRef3.addChildEventListener(childListener);
    }
    private void addChildEventListener4() {
        ChildEventListener childListener = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                codesrecid.add(dataSnapshot.getKey());
                codesrec.add((String)  dataSnapshot.child("code").getValue());
                Log.e("paok2",String.valueOf(dataSnapshot.child("code").getValue()));

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
        dbRef4.addChildEventListener(childListener);
    }


    public void deleteItem(View view,boolean para) {
        dataListView.setItemChecked(selectedPosition, false);


        int x = 0;
        for (String[] s : alld) {
            if (s[0].equals(adapter.getItem(selectedPosition).substring(0, 12))) {
                break;

            }
            x++;
        }
        selectedPosition = x;
        if(para){ dbRef.child(listKeys.get(selectedPosition)).child("cond").setValue("Προς Παραλαβή");}
        else {dbRef.child(listKeys.get(selectedPosition)).child("cond").setValue("Το Δέμα έχει παραληφθεί");}


        ChangeDemaCond(alld.get(selectedPosition),para);
        adapter.clear();
        city1.setChecked(false);
        city2.setChecked(false);
        city3.setChecked(false);
        city4.setChecked(false);
        filtroanti.setChecked(false);
        filtoparadosi.setChecked(false);
        prosapos.setChecked(false);
        addChildEventListener();
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

                    startActivity(new Intent(Paralaves.this, ProfileAct.class));

                }
                if (id == R.id.exitus) {
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(Paralaves.this, MainActivity.class));


                }
                return false;
            }
        });
        popup.show();
    }

    public void ChangeDemaCond(String code[],boolean para) {
        String apost = code[1];
        String par = code[2];
        String kod = code[0];

        for (int i = 0; i < names.size(); i++) {
            if (names.get(i).equals(apost)) {
                sendname=Id.get(i);

                dbRef3 = database.getReference().child("Pelates").child(Id.get(i)).child("Send");
                addChildEventListener3();
                Log.e("paok1",Id.get(i));
            }

            if (names.get(i).equals(par)) {
                dbRef4 = database.getReference().child("Pelates").child(Id.get(i)).child("Received");
                addChildEventListener4();
                Log.e("paok2",Id.get(i));
            }

        }
        sendconf(kod,para);
    }

    public void sendconf(final String kod, final boolean par){
        final AlertDialog.Builder builder = new AlertDialog.Builder(Paralaves.this);
        builder.setCancelable(true);
        builder.setTitle("Επιβεβαίωση Αποστολής");
        builder.setMessage("Θα σταλθεί είδοποίηση");

        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        for (int i = 0; i < codessend.size(); i++) {
                            if (codessend.get(i).equals(kod)) {
                                if(par){dbRef3.child(codessendid.get(i)).child("cond").setValue("Προς Παραλαβή");}
                                else{dbRef3.child(codessendid.get(i)).child("cond").setValue("Το Δέμα έχει παραληφθεί");}

                            } }


                        for (int i = 0; i < codesrec.size(); i++) {
                            if(codesrec.get(i)!=null){
                                if (codesrec.get(i).equals(kod)) {
                                    Log.e("paok3", codesrecid.get(i));
                                    if(par){ dbRef4.child(codesrecid.get(i)).child("cond").setValue("Προς Παραλαβή");}
                                        else{ dbRef4.child(codesrecid.get(i)).child("cond").setValue("Το Δέμα έχει παραληφθεί");}
                                } }}





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
