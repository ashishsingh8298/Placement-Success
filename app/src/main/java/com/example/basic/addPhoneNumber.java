package com.example.basic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class addPhoneNumber extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private final static int RESOLVE_HINT = 1011;
    String mobNumber;
    TextView phnNo;
    Button add;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_phone_number);


        getSupportActionBar().setTitle(("Add Phone Number"));

        mAuth = FirebaseAuth.getInstance();
        if (mAuth != null) {
            mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
            mDatabase.child("firstTimeLogin").setValue("true");
        }
        phnNo = findViewById(R.id.Number);
        add = findViewById(R.id.Add);
        phnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPhone();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = phnNo.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(addPhoneNumber.this, "Please Enter your Phone Number!", Toast.LENGTH_SHORT).show();
                } else {
                    mDatabase.child("Phone Number").setValue(phone);
                    mDatabase.child("firstTimeLogin").setValue("false");
                    Toast.makeText(addPhoneNumber.this, "Phone Number added Successfully.", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(addPhoneNumber.this, Nav_Activity.class));
                }
            }
        });
    }

    private void getPhone() {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.CREDENTIALS_API)
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) addPhoneNumber.this)
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) addPhoneNumber.this)
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
                    phnNo.setText(newString);
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
        Toast.makeText(addPhoneNumber.this, "Connection Failed!", Toast.LENGTH_LONG).show();
    }
}
