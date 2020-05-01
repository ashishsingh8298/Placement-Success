package com.example.basic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register_Activity extends AppCompatActivity {
    EditText name,emailid,password;
    Button Signup;
    FirebaseAuth mAuth;
    ProgressDialog mDialog;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_);

        mDialog=new ProgressDialog(this);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users");

        mAuth=FirebaseAuth.getInstance();
        emailid=findViewById(R.id.Email);
        name=findViewById(R.id.Name);
        password=findViewById(R.id.Password);
        Signup=findViewById(R.id.Register);

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRegister();
            }
        });

    }
    private void startRegister()
    {
        final String sname=name.getText().toString().trim();
        String email=emailid.getText().toString().trim();
        String pwd=password.getText().toString().trim();
        if(TextUtils.isEmpty(sname))
        {
            name.setError("Please enter your Name");
            name.requestFocus();
        }
        else if(email.isEmpty())
        {
            emailid.setError("Please enter emailId");
            emailid.requestFocus();
        }
        else if(pwd.isEmpty())
        {
            password.setError("Please enter password");
            password.requestFocus();
        }
        else if(pwd.length()<8)
        {
            password.setError(("Password must be 8 character long"));
            password.requestFocus();
        }
        else if(!(TextUtils.isEmpty(email) && TextUtils.isEmpty(pwd) && TextUtils.isEmpty(sname))) {
            mDialog.setMessage(("Signing Up..."));
            mDialog.show();
            mAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(Register_Activity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {


                            if (task.isSuccessful()) {
                                mDialog.dismiss();
                                //to send the email verification link
                                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()) {
                                            Toast.makeText(Register_Activity.this, "Successfully registered! , Please check your email for verification.", Toast.LENGTH_LONG).show();
                                            String user_id = mAuth.getCurrentUser().getUid();
                                            DatabaseReference current_user = mDatabase.child(user_id);

                                            current_user.child("Name").setValue(sname);
                                            name.setText("");
                                            emailid.setText("");
                                            password.setText((""));
                                        }
                                        else
                                        {
                                            Toast.makeText(Register_Activity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });


                                //Intent i = new Intent(Register_Activity.this, Dashboard.class);
                                //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                               // startActivity(i);
                            }
                            else
                            {
                                mDialog.dismiss();
                                Toast.makeText(Register_Activity.this,"Email is already registered!",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
            );
        }
    }
}
