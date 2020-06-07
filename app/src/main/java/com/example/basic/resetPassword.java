package com.example.basic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class resetPassword extends AppCompatActivity {
    TextView new_pass,confirm_pass;
    Button reset;
    FirebaseUser mUser;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);


        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        new_pass=findViewById(R.id.newPaasword);
        confirm_pass=findViewById(R.id.confirmPassword);
        reset=findViewById(R.id.Reset);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPass,newPass,confirmPass;
                newPass=new_pass.getText().toString();
                confirmPass=confirm_pass.getText().toString();
                if(newPass.equals(confirmPass) && newPass.length()>=8)
                {
                    mUser.updatePassword(newPass).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(resetPassword.this,"Password reset Successfully.",Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(resetPassword.this,"Password reset Failed.",Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else if(newPass.length()<8)
                {
                    Toast.makeText(resetPassword.this,"Password must be 8 characters long.",Toast.LENGTH_LONG).show();
                }
                else if(!(newPass.equals(confirmPass)))
                {
                    Toast.makeText(resetPassword.this,"New password and Confirm Password need to be same.",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(resetPassword.this,"Enter your password.",Toast.LENGTH_LONG).show();
                }

            }
        });


    }
}
