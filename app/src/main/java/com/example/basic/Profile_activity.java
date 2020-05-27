package com.example.basic;

import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Profile_activity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    CircleImageView profileImage ;
    FirebaseDatabase mDatabase;
    DatabaseReference uRef;
    FirebaseUser mUser;
    TextView fullName,phoneNumber,stream,roll,dob,year,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_activity);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        profileImage = findViewById(R.id.imageview_profile);
        fullName     = findViewById(R.id.NameOfUser);
        email=findViewById(R.id.email);
        phoneNumber=findViewById(R.id.phone);
        stream=findViewById(R.id.stream);
        year=findViewById(R.id.year);
        roll=findViewById(R.id.roll);
        dob=findViewById(R.id.dob);

        if (mUser.getPhoneNumber().length()==0){
            phoneNumber.setText("Phone Number");
        }
        else{
            phoneNumber.setText(mUser.getPhoneNumber());
        }
        if (mUser.getDisplayName()==""){
            fullName.setText("Full Name");
        }
        else{
            fullName.setText(mUser.getDisplayName());
        }
        email.setText(mUser.getEmail());

        Glide.with(this).load(mUser.getPhotoUrl()).into(profileImage);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile_activity.this, editProfile.class));
            }
        });


        //Animation animation = AnimationUtils.loadAnimation(this,R.anim.rotate);

        //Animation animation1 = AnimationUtils.loadAnimation(this,R.anim.blink_anim);

        //Animation animation2 = AnimationUtils.loadAnimation(this,R.anim.lefttoright);

        //profileImage.startAnimation(animation);

        //fullName.startAnimation(animation1);
    }
}
