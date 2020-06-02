package com.example.basic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.internal.FlowLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class Selectskills_activity extends AppCompatActivity {
    LinearLayout linearLayoutButton;
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    FirebaseUser mUser;
    LinearLayoutManager mLinerLayoutManager;
    private RecyclerView mRecyclerView;
    FirebaseRecyclerAdapter<Skills,viewHolder> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Skills> options;
    CardView cardView;
    FirebaseAuth mAuth;
    String addSkill;
    TextView sk;
    private ChipGroup mchipGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectskills_activity);

        mAuth=FirebaseAuth.getInstance();
        mchipGroup=findViewById(R.id.chipGroup);
        cardView=findViewById(R.id.card);
        mDatabase= FirebaseDatabase.getInstance();
        mRef=mDatabase.getReference("Skills");
        DatabaseReference uRef=FirebaseDatabase.getInstance().getReference("Users");
        mLinerLayoutManager=new LinearLayoutManager(this);
        mLinerLayoutManager.setReverseLayout(true);
        mLinerLayoutManager.setStackFromEnd(true);
        mRecyclerView= findViewById(R.id.list);
        FloatingActionButton fab = findViewById(R.id.fab);
        sk=findViewById(R.id.skillName);
        uRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String skill=dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("skills").getValue(String.class);
                if(skill!=null) {
                    if(!(skill.equals(""))) {
                        sk.setText("Your Skills : " + skill);
                        String str[] = skill.split(",");
                        List<String> slist = new ArrayList<String>();
                        slist = Arrays.asList(str);
                        for (String s : slist) {
                            Chip chip = new Chip(Selectskills_activity.this);
                            chip.setText(s);
                            chip.setCloseIconVisible(true);
                            chip.setCheckable(false);
                            chip.setClickable(false);
                            mchipGroup.addView(chip);
                            mchipGroup.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Snackbar.make(view,addSkill, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            uRef.child(mAuth.getCurrentUser().getUid()).child("skills").setValue(addSkill);
           // startActivity(new Intent(Selectskills_activity.this,recommendedJobs.class));

            }
        });

        showData();


    }
    private  void  showData()
    {

        addSkill="";
        options=new FirebaseRecyclerOptions.Builder<Skills>().setQuery(mRef,Skills.class).build();
        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Skills, viewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull viewHolder holder, int position, @NonNull Skills model) {

                holder.cardView.setCardBackgroundColor(holder.cardView.isSelected()? Color.GREEN: Color.WHITE);
                holder.setSkills(getApplicationContext(),model.getName());
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.cardView.setSelected(!holder.cardView.isSelected());
                        holder.cardView.setCardBackgroundColor(holder.cardView.isSelected()? Color.GREEN: Color.WHITE);
                        if(holder.cardView.isSelected())
                        {
                            addSkill=addSkill+model.getName()+",";
                            Chip chip=new Chip(Selectskills_activity.this);
                            chip.setText(model.getName());
                            chip.setCloseIconVisible(true);
                            chip.setCheckable(false);
                            chip.setClickable(false);
                            mchipGroup.addView(chip);
                            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mchipGroup.removeView(chip);
                                    addSkill=addSkill.replace(model.getName()+",","");
                                }
                            });
                            mchipGroup.setVisibility(View.VISIBLE);
                        }
                        /*else if(!holder.cardView.isSelected())
                        {
                            //Chip chip=(Chip)view;
                            //mchipGroup.removeView(chip);
                            addSkill=addSkill.replace(model.getName()+",","");
                        }*/
                    }
                });
            }
            @NonNull
            @Override
            public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.skills_holder,parent,false);
                viewHolder vHolder=new viewHolder(itemView);

                vHolder.setOnClickListener(new viewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemLongClick(View view, int position) {


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
