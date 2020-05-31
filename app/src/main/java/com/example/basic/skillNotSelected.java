package com.example.basic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class skillNotSelected extends AppCompatActivity {
    Button selectSkill;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.skillnotselected);
        selectSkill=findViewById(R.id.select_skill);
        selectSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(skillNotSelected.this,Selectskills_activity.class));
            }
        });
    }
}
