package com.example.basic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class editProfile extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private FirebaseAuth mAuth;
    private final static int RESOLVE_HINT = 1011;
    String mobNumber;
    TextInputEditText fullName,phoneNumber,dob;
    FirebaseUser mUser;
    String name,dob1,phone;
    Button update;
    DatabaseReference uRef;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        checkConnection();
        mAuth= FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users");
        fullName=findViewById(R.id.Name);
        phoneNumber=findViewById(R.id.Phone);
        dob=findViewById(R.id.dob);
        String user_id = mAuth.getCurrentUser().getUid();
        DatabaseReference current_user = mDatabase.child(user_id);

        uRef=FirebaseDatabase.getInstance().getReference().child("Users").child(mUser.getUid());

        uRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name=dataSnapshot.child("Name").getValue(String.class);
                phone=dataSnapshot.child("Phone Number").getValue(String.class);
                dob1=dataSnapshot.child("dob").getValue(String.class);
                fullName.setText(name);
                phoneNumber.setText(phone);
                dob.setText(dob1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        update=findViewById(R.id.Update);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startUpdate();
            }
        });

        phoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPhone();
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



private void getPhone() {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
        .addApi(Auth.CREDENTIALS_API)
        .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) editProfile.this)
        .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) editProfile.this)
        .build();
        googleApiClient.connect();
        HintRequest hintRequest = new HintRequest.Builder()
        .setPhoneNumberIdentifierSupported(true)
        .build();
        PendingIntent intent = Auth.CredentialsApi.getHintPickerIntent(googleApiClient, hintRequest);
        try {
        startIntentSenderForResult(intent.getIntentSender(),
        RESOLVE_HINT, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
        e.printStackTrace();
        }
        }

@Override
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESOLVE_HINT) {
        if (resultCode == RESULT_OK) {
        com.google.android.gms.auth.api.credentials.Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
        if (credential != null) {
        mobNumber = credential.getId();
        String newString = mobNumber.replace("+91", "");
        //Toast.makeText(addPhoneNumber.this,""+newString,Toast.LENGTH_SHORT).show();
        phoneNumber.setText(newString);
        } else {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
        }
        }
        }

@Override
public void onConnected(@Nullable Bundle bundle) {

        }

@Override
public void onConnectionSuspended(int i) {

        }

@Override
public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(editProfile.this, "Connection Failed!", Toast.LENGTH_LONG).show();
        }
    public void checkConnection()
    {
        ConnectivityManager manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork=manager.getActiveNetworkInfo();
        if(null==activeNetwork)
        {
            startActivity(new Intent(editProfile.this,noInternet.class));
        }
    }
}
