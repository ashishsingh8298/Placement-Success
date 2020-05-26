package com.example.basic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class appliedJobsActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    LinearLayoutManager mLinerLayoutManager;
    private RecyclerView mRecyclerView;
    FirebaseRecyclerAdapter<Company,viewHolder> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Company> options;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applied_jobs);

        mAuth = FirebaseAuth.getInstance();
        mLinerLayoutManager=new LinearLayoutManager(this);
        mLinerLayoutManager.setReverseLayout(true);
        mLinerLayoutManager.setStackFromEnd(true);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Company");
        mRecyclerView= findViewById(R.id.list);
        mRecyclerView.setHasFixedSize(true);

        showData();
    }
    private  void  showData()
    {
        options=new FirebaseRecyclerOptions.Builder<Company>().setQuery(mDatabase,Company.class).build();
        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Company, viewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull viewHolder holder, int position, @NonNull Company model) {
                holder.setDetails(getApplicationContext(),model.getJobTitle(),model.getJobDescription(),model.getLinkLogo());
            }

            @NonNull
            @Override
            public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.company_row,parent,false);
                viewHolder vHolder=new viewHolder(itemView);
                vHolder.setOnClickListener(new viewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String temp=firebaseRecyclerAdapter.getRef(position).getKey();
                        Intent intent=new Intent(appliedJobsActivity.this,Company_Description.class);
                        intent.putExtra("str",temp);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        Toast.makeText(appliedJobsActivity.this,"long click",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDeleteClick(View view, int position) {

                    }

                    @Override
                    public void onEditClick(View view, int position) {

                    }
                });
                return vHolder;
            }
        };
        mRecyclerView.setLayoutManager(mLinerLayoutManager);
        firebaseRecyclerAdapter.startListening();
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

}
