package com.example.basic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

public class whatsNew extends AppCompatActivity implements AdapterClass.OnNoteListener {
    private static final String TAG="whatsNew";
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    DatabaseReference ref,uRef;
    ArrayList<Company> list;
    Calendar calendar;
    SimpleDateFormat df;
    RecyclerView recyclerView;
    ArrayList<String> keyList;
    ArrayList<String> userKeyList;
    String userId,curr_date;
    ArrayList<String> finalkeyList=new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_new);
        calendar=Calendar.getInstance();
        df=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        curr_date=df.format(calendar.getTime());

        mAuth = FirebaseAuth.getInstance();
        ref= FirebaseDatabase.getInstance().getReference().child("Company");
        recyclerView=findViewById(R.id.rv);
        keyList=new ArrayList<>();
        userKeyList=new ArrayList<>();
        list=new ArrayList<>();

        mUser=mAuth.getCurrentUser();
        userId=mUser.getUid();
        uRef=FirebaseDatabase.getInstance().getReference("Users").child(userId).child("appliedJobs");

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()){
                    String postJobDate=dataSnapshot.getValue(Company.class).getDatetime();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy kk:mm:ss");
                    Date date = null;
                    Date curr_date_obj=null;
                    try {
                        date = dateFormat.parse(postJobDate);
                        curr_date_obj=dateFormat.parse(curr_date);
                        long days=Math.abs((date.getTime()-curr_date_obj.getTime())/86400000);
                        String niceDateStr = (String) DateUtils.getRelativeTimeSpanString(date.getTime() , Calendar.getInstance().getTimeInMillis(), DateUtils.MINUTE_IN_MILLIS);
                        if (days<7){
                            Log.d(TAG, "onChildAdded: "+niceDateStr);
                            keyList.add(0,dataSnapshot.getKey());
                            list.add(0,dataSnapshot.getValue(Company.class));
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    AdapterClass adapterClass=new AdapterClass(list,whatsNew.this::OnNoteClick);

                    recyclerView.setAdapter(adapterClass);
                    finalkeyList=keyList;


                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void OnNoteClick(int position) {
        String temp=finalkeyList.get(position);
        Intent intent = new Intent(whatsNew.this, Company_Description.class);
        intent.putExtra("str",temp);
        startActivity(intent);
    }
}
