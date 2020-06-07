package com.example.basic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        getSupportActionBar().setTitle("Settings");

        if(findViewById(R.id.content_preference)!=null);
        {
            if(savedInstanceState!=null)
                return;

            getFragmentManager().beginTransaction().add(R.id.content_preference,new settingFragment()).commit();
        }
    }
}
