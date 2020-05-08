package com.example.basic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPaasword extends AppCompatActivity {

    EditText emailId;
    Button Signin,Signup;
    Button ForgotPassword;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_paasword);

        emailId=findViewById(R.id.Email);
        mAuth=FirebaseAuth.getInstance();
        Signin=findViewById(R.id.SignIn);
        Signup=findViewById(R.id.SignUp);
        ForgotPassword=findViewById(R.id.Reset);
        ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
}