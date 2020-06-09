package com.example.basic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPaasword extends AppCompatActivity {

    EditText emailId;
    Button Signin,Signup;
    Button ForgotPassword;
    FirebaseAuth mAuth;
    LinearLayout mlayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_paasword);
        mlayout=findViewById(R.id.parent_layout);
        emailId=findViewById(R.id.Email);
        mAuth=FirebaseAuth.getInstance();
        Signin=findViewById(R.id.SignIn);
        Signup=findViewById(R.id.SignUp);
        ForgotPassword=findViewById(R.id.Reset);
        ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkConnection();
        String email = emailId.getText().toString();
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(ForgotPaasword.this,"Please Enter Your Email.",Toast.LENGTH_LONG).show();
        }
        else {
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ForgotPaasword.this, "Password reset email sent successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ForgotPaasword.this, "Please check your email address", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
});
        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgotPaasword.this,Login_Activity.class));
            }
        });
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgotPaasword.this,Register_Activity.class));
            }
        });
    }
    public void checkConnection()
    {
        ConnectivityManager manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork=manager.getActiveNetworkInfo();
        if(null==activeNetwork)
        {
            Snackbar.make(mlayout,"Could not connect to internet", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).setBackgroundTint(Color.RED).show();
        }
    }
}