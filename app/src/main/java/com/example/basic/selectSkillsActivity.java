package com.example.basic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class selectSkillsActivity extends AppCompatActivity {
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    private ChipGroup mChipGroup;
    FirebaseAuth mAuth;
    String addSkill;
    List<String> slist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_skills2);

        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance();
        mRef=mDatabase.getReference("Skills");
        DatabaseReference uRef=FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid());
        DatabaseReference skillRef=FirebaseDatabase.getInstance().getReference("skills");
        FloatingActionButton fab = findViewById(R.id.fab);
        mChipGroup=findViewById(R.id.chipGroup);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSkill="";
                int chipsCount=mChipGroup.getChildCount();
                int i=0;
                while(i<chipsCount)
                {
                    Chip chip=(Chip)mChipGroup.getChildAt(i);
                    if(chip.isChecked())
                    {
                        addSkill+=chip.getText().toString()+",";
                    }
                    i++;
                }
                Snackbar.make(view,addSkill, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                uRef.child("skills").setValue(addSkill);
                // startActivity(new Intent(Selectskills_activity.this,recommendedJobs.class));*/

            }
        });

        skillRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String skill=dataSnapshot.getValue(String.class);
                String str[] = skill.split(",");
                 slist = new ArrayList<String>();
                slist = Arrays.asList(str);
                for (String s : slist) {
                    Chip chip = new Chip(selectSkillsActivity.this);
                    chip.setText(s);
                    //chip.setCloseIconVisible(true);
                    chip.setCheckable(true);
                    chip.setClickable(true);
                    //chip.setFocusable(true);
                    chip.setCheckedIconVisible(false);
                    chip.setChipBackgroundColorResource(R.color.white);
                    chip.setChipStrokeColorResource(R.color.colorAccent);
                    chip.setChipStrokeWidth(1);
                    mChipGroup.addView(chip);
                    mChipGroup.setVisibility(View.VISIBLE);
                            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                    if(chip.isChecked()) {
                                        chip.setChipBackgroundColorResource(R.color.colorAccent);
                                    }
                                    else
                                    {
                                        chip.setChipBackgroundColorResource(R.color.white);
                                        chip.setChipStrokeColorResource(R.color.colorAccent);
                                    }
                                }
                            });
                        }
                uRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String getSkill=dataSnapshot.child("skills").getValue(String.class);
                        String skillstr[] = getSkill.split(",");
                        List<String> skilllist = new ArrayList<String>();
                        skilllist = Arrays.asList(skillstr);
                        int chipsCount=mChipGroup.getChildCount();
                        int i=0;
                        for (String s : slist) {
                            for (String getcheckedskill : skilllist) {
                                if(s.equals(getcheckedskill))
                                {
                                    Chip chip=(Chip)mChipGroup.getChildAt(i);
                                    chip.setChecked(true);
                                }
                            }
                            i++;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
