package com.example.basic;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

public class searchActivity extends AppCompatActivity implements AdapterClass.OnNoteListener {
    private static final String TAG="searchActivity";
    DatabaseReference ref;
    ArrayList<Company> list;
    RecyclerView recyclerView;
    SearchView searchView;
    ArrayList<String> keyList;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        ref= FirebaseDatabase.getInstance().getReference().child("Company");
        recyclerView=findViewById(R.id.rv);
        searchView=findViewById(R.id.searchView);




    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ref!=null){
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

                        AdapterClass adapterClass=new AdapterClass(list,searchActivity.this::OnNoteClick);
                        recyclerView.setAdapter(adapterClass);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(searchActivity.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (searchView!=null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    search(s);
                    return true;
                }
            });
        }
    }

    private void search(String s) {
        ArrayList<Company> myList=new ArrayList<>();
        ArrayList<String> myKeyList=new ArrayList<>();
        for (Company object:list){
            if (
                    object.getJobDescription().toLowerCase().contains(s.toLowerCase())
//                    ||object.getJobResponsibility().toLowerCase().contains(s.toLowerCase())
//                    ||object.getJobTitle().toLowerCase().contains(s.toLowerCase())
//                    ||object.getRequiredSkills().toLowerCase().contains(s.toLowerCase())
            ){
                myList.add(object);

            }
        }
        for (Company object:myList){
            myKeyList.add(keyList.get(list.indexOf(object)));
        }
        keyList=myKeyList;
        AdapterClass adapterClass=new AdapterClass(myList,this::OnNoteClick);
        recyclerView.setAdapter(adapterClass);
    }


    @Override
    public void OnNoteClick(int position) {
        String temp=keyList.get(position);
        Intent intent = new Intent(searchActivity.this, Company_Description.class);
        intent.putExtra("str",temp);
        startActivity(intent);
        finish();
    }
}
