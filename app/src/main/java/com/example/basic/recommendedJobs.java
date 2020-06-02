package com.example.basic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class recommendedJobs extends AppCompatActivity implements AdapterClass.OnNoteListener{
    private static final String TAG="recommendedJobsActivity";
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    DatabaseReference ref,uRef;
    ArrayList<Company> list;
    RecyclerView recyclerView;
    ArrayList<String> keyList;
    ArrayList<Company> myList;
    ArrayList<String> myKeyList;
    ArrayList<String> finalkeyList=new ArrayList<>();
    String userId,skill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommended_jobs);

        mAuth = FirebaseAuth.getInstance();
        ref= FirebaseDatabase.getInstance().getReference().child("Company");
        recyclerView=findViewById(R.id.rv);

        mUser=mAuth.getCurrentUser();
        userId=mUser.getUid();
        uRef=FirebaseDatabase.getInstance().getReference("Users").child(userId);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    list=new ArrayList<>();
                    keyList=new ArrayList<>();
                    for (DataSnapshot ds:dataSnapshot.getChildren()){
                        keyList.add(ds.getKey());
                        list.add(ds.getValue(Company.class));
                    }
                    //AdapterClass adapterClass=new AdapterClass(list,recommendedJobs.this::OnNoteClick);
                    //recyclerView.setAdapter(adapterClass);
                    finalkeyList=keyList;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        uRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myList=new ArrayList<>();
                myKeyList=new ArrayList<>();
                skill=dataSnapshot.child("skills").getValue(String.class);
                    String str[] = skill.split(",");
                    List<String> slist = new ArrayList<String>();
                    slist = Arrays.asList(str);
                    for (String s : slist) {
                        for (Company object : list) {
                            if (object.getRequiredSkills().toLowerCase().contains(s.toLowerCase())) {
                                myList.add(object);
                            }
                        }
                    }
                    HashSet<Company> listToSet = new HashSet<Company>(myList);
                    ArrayList<Company> listWithoutDuplicate = new ArrayList<Company>(listToSet);
                    for (Company object : listWithoutDuplicate) {
                        myKeyList.add(keyList.get(list.indexOf(object)));
                    }
                    finalkeyList = myKeyList;
                    AdapterClass adapterClass = new AdapterClass(listWithoutDuplicate, recommendedJobs.this::OnNoteClick);
                    recyclerView.setAdapter(adapterClass);
                }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //recommend(skill);



    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    /*private void recommend(String skill)
    {
        myList=new ArrayList<>();
        myKeyList=new ArrayList<>();
        for (Company object:list){
            if (object.getRequiredSkills().toLowerCase().contains(skill.toLowerCase())){
                myList.add(object);

            }
    }
        for (Company object:myList){
            myKeyList.add(keyList.get(list.indexOf(object)));
        }
        finalkeyList=myKeyList;
        AdapterClass adapterClass=new AdapterClass(myList,this::OnNoteClick);
        recyclerView.setAdapter(adapterClass);
    }*/

    @Override
    public void OnNoteClick(int position) {
        String temp=finalkeyList.get(position);
        Intent intent = new Intent(recommendedJobs.this, Company_Description.class);
        intent.putExtra("str",temp);
        startActivity(intent);
    }
}
