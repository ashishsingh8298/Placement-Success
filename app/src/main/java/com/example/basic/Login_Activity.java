package com.example.basic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
//import com.google.firebase.quickstart.auth.R;
//import com.google.firebase.quickstart.auth.databinding.ActivityGoogleBinding;

import java.io.IOException;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Login_Activity extends AppCompatActivity {
    TextView SignupBtn;
    EditText emailId,password;
    Button SigninBtn,ForgotPassword;
    FirebaseAuth mAuth;
    ProgressDialog mDialog;
    SignInButton gSignin;
    private DatabaseReference mDatabase;
    private static final int RC_SIGN_IN=9001;
    private GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = "GoogleActivity";



    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();
        mDialog = new ProgressDialog(this);


        emailId = findViewById(R.id.Email);
        password = findViewById(R.id.Password);
        SigninBtn = findViewById(R.id.Login);
        SignupBtn = findViewById(R.id.SignUp);
        gSignin = findViewById(R.id.Google);
        ForgotPassword = findViewById(R.id.forgot_password);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mAuth.getCurrentUser();
                if (mFirebaseUser != null && mAuth.getCurrentUser().isEmailVerified()) {
                    Toast.makeText(Login_Activity.this, "You are logged in!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login_Activity.this, Nav_Activity.class));
                } else {
                    Toast.makeText(Login_Activity.this, "Please Login!", Toast.LENGTH_SHORT).show();
                }
            }
        };

        SigninBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString();
                String pass = password.getText().toString();
                if (email.isEmpty()) {
                    emailId.setError("Please enter email id");
                    emailId.requestFocus();
                } else if (pass.isEmpty()) {
                    password.setError("Please enter your password!");
                    password.requestFocus();
                } else if (!(TextUtils.isEmpty(email) && TextUtils.isEmpty(pass)) ){
                    mDialog.setMessage("Processing...");
                    mDialog.show();
                    mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(Login_Activity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            mDialog.dismiss();
                            if (!task.isSuccessful()) {
                                Toast.makeText(Login_Activity.this, "Incorrect EmailId/Password(or Not Registered!)", Toast.LENGTH_LONG).show();
                            } else {

                                if(mAuth.getCurrentUser().isEmailVerified())
                                {
                                    startActivity(new Intent(Login_Activity.this, Nav_Activity.class));
                                }
                                else
                                {
                                    Toast.makeText(Login_Activity.this, "Please verify your email!", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                }
            }
        });

        ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(Login_Activity.this,ForgotPaasword.class));
            }
        });




        //calling the signin facility if google button is pressed
        gSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        //for calling the signup activity
        SignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Login_Activity.this,Register_Activity.class);
                startActivity(i);
            }
        });
    }
    // [START signin]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]


    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(Login_Activity.this,"Google Sign in failed!",Toast.LENGTH_SHORT).show();
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(String idToken) {
        mDialog.setMessage("Processing...");
        mDialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(Login_Activity.this,"Authentication Successful", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            DatabaseReference current_user = mDatabase.child(user.getUid());

                            current_user.child("Name").setValue(user.getDisplayName());
                            current_user.child("Email").setValue(user.getEmail());
                            //current_user.child("Phone Number").setValue(user.getPhoneNumber());
                            //current_user.child("profilePhoto").child("imageurl").setValue(user.getPhotoUrl()).toString();


                            current_user.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String firstTime=dataSnapshot.child("firstTimeLogin").getValue(String.class);
                                    if(firstTime!=null)
                                    {
                                        if(firstTime.equals("false"))
                                        {
                                            startActivity(new Intent(Login_Activity.this,Nav_Activity.class));
                                        }
                                        else
                                        {
                                            startActivity(new Intent(Login_Activity.this,addPhoneNumber.class));
                                        }
                                    }
                                    else
                                    {
                                        startActivity(new Intent(Login_Activity.this,addPhoneNumber.class));
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });



                    startActivity(new Intent(Login_Activity.this,Nav_Activity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login_Activity.this,"Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                        mDialog.dismiss();
                    }
                });
    }
    // [END auth_with_google]


    @Override
    protected void onStart() {

        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

}
