package com.example.basic;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Company_Description extends AppCompatActivity {
    private FirebaseAuth mAuth;
    TextView c_name,c_desc,c_res,c_ctc,c_eligibility,c_skills,c_criteria,start_date,end_date,feed;
    Button apply,postComment;
    FirebaseDatabase mDatabase;
    DatabaseReference mRef,cRef;
    FirebaseUser mUser;
    ImageView c_logo;
    String temp,user_id,date;
    Calendar calendar;
    SimpleDateFormat df;
    LinearLayoutManager mLinerLayoutManager;
    private RecyclerView mRecyclerView;
    FirebaseRecyclerAdapter<Comment,viewHolder> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Comment> options;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company__description);
        Intent intent=getIntent();
        String temp=intent.getStringExtra("str");
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        user_id=mUser.getUid();
        calendar=Calendar.getInstance();
        SimpleDateFormat df=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        date=df.format(calendar.getTime());
        mDatabase= FirebaseDatabase.getInstance();
        mRef=mDatabase.getReference("Company").child(temp);
        cRef=FirebaseDatabase.getInstance().getReference("Company").child(temp).child("Comments");
        mLinerLayoutManager=new LinearLayoutManager(this);
        mLinerLayoutManager.setReverseLayout(true);
        mLinerLayoutManager.setStackFromEnd(true);
        mRecyclerView= findViewById(R.id.list);
        mRecyclerView.setHasFixedSize(true);
        showData();
        feed=findViewById(R.id.Feedback);
        c_logo=findViewById(R.id.Company_logo);
        postComment=findViewById(R.id.post);
        c_name=findViewById(R.id.company_name);
        c_desc=findViewById(R.id.Company_desc_content);
        c_res=findViewById(R.id.Company_res_content);
        c_ctc=findViewById(R.id.Company_ctc_content);
        c_eligibility=findViewById(R.id.Company_branch_content);
        c_skills=findViewById(R.id.Company_skills_content);
        c_criteria=findViewById(R.id.Company_critera_content);
        start_date=findViewById(R.id.Company_start_content);
        end_date=findViewById(R.id.Company_end_content);
        apply=findViewById(R.id.apply);

       mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String logo=dataSnapshot.child("linkLogo").getValue(String.class);
                String name = dataSnapshot.child("jobTitle").getValue(String.class);
                String desc = dataSnapshot.child("jobDescription").getValue(String.class);
                String res =dataSnapshot.child("jobResponsibility").getValue(String.class);
                String ctc =dataSnapshot.child("jobCtc").getValue(String.class);
                String branch =dataSnapshot.child("elegibleBranch").getValue(String.class);
                String skills =dataSnapshot.child("requiredSkills").getValue(String.class);
                String criteria =dataSnapshot.child("elegibilityCriteria").getValue(String.class);
                String s_date =dataSnapshot.child("applyDateFrom").getValue(String.class);
                String e_date =dataSnapshot.child("applyDateTo").getValue(String.class);
                Picasso.get().load(logo).fit().centerCrop().placeholder(R.drawable.images).error(R.drawable.error).into(c_logo);
                    c_name.setText(name);
                    c_desc.setText(desc);
                    c_res.setText(res);
                    c_ctc.setText(ctc);
                    c_eligibility.setText(branch);
                    c_skills.setText(skills);
                    c_criteria.setText(criteria);
                    start_date.setText(s_date);
                    end_date.setText(e_date);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



       postComment.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               final String comment=feed.getText().toString().trim();
               if(TextUtils.isEmpty(comment))
               {
                   Toast.makeText(Company_Description.this,"Write your Feedback",Toast.LENGTH_LONG).show();
               }
               else
               {
                   Toast.makeText(Company_Description.this, "Feedback received.", Toast.LENGTH_LONG).show();
                   String user_id = mAuth.getCurrentUser().getUid();
                    DatabaseReference ref=cRef.push();
                   ref.child("userId").setValue(user_id);
                   ref.child("Comment").setValue(comment);
                   ref.child("Date").setValue(date);
                   feed.setText("");
               }
           }
       });
       showData();

    }
    private  void  showData()
    {
        options=new FirebaseRecyclerOptions.Builder<Comment>().setQuery(cRef,Comment.class).build();
        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Comment, viewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull viewHolder holder, int position, @NonNull Comment model) {
                holder.setComments(getApplicationContext(),model.getUserId(),model.getComment(),model.getDate());
            }

            @NonNull
            @Override
            public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_row,parent,false);
                viewHolder vHolder=new viewHolder(itemView);
                vHolder.setOnClickListener(new viewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

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
