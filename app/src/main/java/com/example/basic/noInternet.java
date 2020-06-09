package com.example.basic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class noInternet extends AppCompatActivity {
    TextView tryAgain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);
        tryAgain=findViewById(R.id.try_again);
        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork=manager.getActiveNetworkInfo();
                if(null!=activeNetwork)
                {
                    startActivity(new Intent(noInternet.this,Nav_Activity.class));
                }
                else
                {
                    startActivity(new Intent(noInternet.this,noInternet.class));
                }
            }
        });
    }
}
