package com.example.basic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Dashboard extends AppCompatActivity {
    FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard2);
        mAuth=FirebaseAuth.getInstance();


        mAuthStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mUser=mAuth.getCurrentUser();
                if(mUser==null)
                {
                    Intent i=new Intent(Dashboard.this,Login_Activity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            }
        };
    }
   public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.action_logout)
        {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(Dashboard.this,Login_Activity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onStart()
    {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }
}
