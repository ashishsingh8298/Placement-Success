package com.example.basic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class jobsNotApplied extends AppCompatActivity {
    Button notify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs_not_applied);
        notify=findViewById(R.id.whats_new);
        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(jobsNotApplied.this,whatsNew.class));
            }
        });
    }
}
