package com.example.basic;

import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class Profile_activity extends AppCompatActivity {

    CircleImageView profileImage ;
    TextView fullName,designation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_activity);
        profileImage = findViewById(R.id.imageview_profile);
        fullName     = findViewById(R.id.NameOfUser);


        //Animation animation = AnimationUtils.loadAnimation(this,R.anim.rotate);

        //Animation animation1 = AnimationUtils.loadAnimation(this,R.anim.blink_anim);

        //Animation animation2 = AnimationUtils.loadAnimation(this,R.anim.lefttoright);

        //profileImage.startAnimation(animation);

        //fullName.startAnimation(animation1);
    }
}
