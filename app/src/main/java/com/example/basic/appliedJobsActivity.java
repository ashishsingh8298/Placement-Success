package com.example.basic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class appliedJobsActivity extends AppCompatActivity implements AdapterClass.OnNoteListener {
    private static final String TAG="appliedJobsActivity";
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    DatabaseReference ref,uRef;
    ArrayList<Company> list;
    RecyclerView recyclerView;
    ArrayList<String> keyList;
    ArrayList<String> userKeyList;
    String userId;
    ArrayList<String> finalkeyList=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applied_jobs);

        mAuth = FirebaseAuth.getInstance();
        ref= FirebaseDatabase.getInstance().getReference().child("Company");
        recyclerView=findViewById(R.id.rv);
        userKeyList=new ArrayList<>();

        mUser=mAuth.getCurrentUser();
        userId=mUser.getUid();
        uRef=FirebaseDatabase.getInstance().getReference("Users").child(userId).child("appliedJobs");

        uRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    for (DataSnapshot ds:dataSnapshot.getChildren()){
                        userKeyList.add(ds.getKey());
                    }

                }


                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            list=new ArrayList<>();
                            keyList=new ArrayList<>();
                            for (String id:userKeyList){
                                for (DataSnapshot ds:dataSnapshot.getChildren()){
                                    if(id.equals(ds.getKey())){
                                        keyList.add(ds.getKey());
                                        list.add(ds.getValue(Company.class));
                                    }
                                }
                            }


                            AdapterClass adapterClass=new AdapterClass(list,appliedJobsActivity.this::OnNoteClick);
                            recyclerView.setAdapter(adapterClass);
                            finalkeyList=keyList;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(appliedJobsActivity.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void OnNoteClick(int position) {
        String temp=finalkeyList.get(position);
        Intent intent = new Intent(appliedJobsActivity.this, Company_Description.class);
        intent.putExtra("str",temp);
        startActivity(intent);
        finish();
    }
}
