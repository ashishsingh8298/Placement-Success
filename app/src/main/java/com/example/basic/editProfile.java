package com.example.basic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class editProfile extends AppCompatActivity {
    private FirebaseAuth mAuth;
    TextInputEditText fullName,phoneNumber,dob;
    FirebaseUser mUser;
    Button update;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        mAuth= FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users");
        fullName=findViewById(R.id.Name);
        phoneNumber=findViewById(R.id.Phone);
        dob=findViewById(R.id.dob);

        fullName.setText(mUser.getDisplayName());
        phoneNumber.setText(mUser.getPhoneNumber());
        update=findViewById(R.id.Update);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startUpdate();
            }
        });



    }
    private void startUpdate()
    {
        String user_id = mAuth.getCurrentUser().getUid();
        DatabaseReference current_user = mDatabase.child(user_id);

        final String sname=fullName.getText().toString().trim();
        final String sdob=dob.getText().toString().trim();
        final String sphone=phoneNumber.getText().toString().trim();
        if (sname==""){

        }
        else {
            current_user.child("Name").setValue(sname);
        }
        if (sdob==""){

        }
        else{
            current_user.child("dob").setValue(sdob);
        }

        if (sphone==""){

        }else{
            current_user.child("Phone Number").setValue(sphone);
        }
        Toast.makeText(this,"Information Updated Succesfully...",Toast.LENGTH_LONG);
        startActivity(new Intent(editProfile.this,Profile_activity.class));
        finish();
    }
}
