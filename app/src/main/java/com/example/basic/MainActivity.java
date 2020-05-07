package com.example.basic;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static int SPASH_SCREEN=5000;

    //variables
    Animation topAnim,bottomAnim;
    ImageView image;
    TextView logo,slogon;
    FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //Animations
        topAnim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        //Hooks
        image=findViewById(R.id.imageView);
        logo=findViewById(R.id.textView);
        slogon=findViewById(R.id.textView);

        image.setAnimation(topAnim);
        logo.setAnimation(bottomAnim);
        slogon.setAnimation(bottomAnim);


        new Handler().postDelayed(new Runnable(){

            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, Nav_Activity.class);
                startActivity(intent);
                finish();
                        /*FirebaseUser mUser = mAuth.getCurrentUser();
                        if ((mUser != null && mAuth.getCurrentUser().isEmailVerified())) {
                            Intent i = new Intent(MainActivity.this, Nav_Activity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }
                        else
                        {
                            startActivity(new Intent(MainActivity.this,Login_Activity.class));
                            Toast.makeText(MainActivity.this, "Please Login!", Toast.LENGTH_SHORT).show();
                        }*/
            }
        },SPASH_SCREEN);


    }



}
