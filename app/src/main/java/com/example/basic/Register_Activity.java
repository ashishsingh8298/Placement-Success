package com.example.basic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class Register_Activity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    TextInputEditText name,emailid,password,phoneNo;
    private final static int RESOLVE_HINT = 1011;
    String mobNumber;
    LinearLayout mlayout;
    Button Signup,SigninPage;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    ProgressDialog mDialog;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_);
        mlayout=findViewById(R.id.parent_layout);
        mDialog=new ProgressDialog(this);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users");


        mAuth=FirebaseAuth.getInstance();
        emailid=findViewById(R.id.Email);
        name=findViewById(R.id.Name);
        password=findViewById(R.id.Password);
        Signup=findViewById(R.id.Register);
        SigninPage=findViewById(R.id.loginPage);
        phoneNo=findViewById(R.id.Phone);

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkConnection();
                startRegister();
            }
        });

        SigninPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register_Activity.this,Login_Activity.class));
            }
        });
        phoneNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPhone();
            }
        });

    }
    private void startRegister()
    {
        final String sname=name.getText().toString().trim();
        final String email=emailid.getText().toString().trim();
        String pwd=password.getText().toString().trim();
        final String phone=phoneNo.getText().toString().trim();
        if(TextUtils.isEmpty(sname))
        {
            name.setError("Please enter your Name");
            name.requestFocus();
        }
        else if(TextUtils.isEmpty(email))
        {
            emailid.setError("Please enter emailId");
            emailid.requestFocus();
        }
        else if(TextUtils.isEmpty(pwd))
        {
            password.setError("Please enter password");
            password.requestFocus();
        }
        else if(pwd.length()<8)
        {
            password.setError(("Password must be 8 character long"));
            password.requestFocus();
        }
        else if(TextUtils.isEmpty(phone))
        {
            phoneNo.setError("Please Select your Phone Number");
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
                                            current_user.child("Email").setValue(email);
                                            current_user.child("Phone Number").setValue(phone);
                                            current_user.child("firstTimeLogin").setValue("false");
                                            current_user.child("block").setValue(false);
                                            name.setText("");
                                            emailid.setText("");
                                            phoneNo.setText("");
                                            password.setText((""));
                                            name.requestFocus();
                                        }
                                        else
                                        {
                                            Toast.makeText(Register_Activity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                            }
                            else
                            {
                                mDialog.dismiss();
                                Toast.makeText(Register_Activity.this,"Email is already registered!(Invalid Email)",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
            );
        }
    }
    private void getPhone() {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.CREDENTIALS_API)
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) Register_Activity.this)
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) Register_Activity.this)
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
                    phoneNo.setText(newString);
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
        Toast.makeText(Register_Activity.this, "Connection Failed!", Toast.LENGTH_LONG).show();
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
