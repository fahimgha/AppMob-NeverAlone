package com.example.neveralone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class NameActivity extends AppCompatActivity {
    String email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        Intent myIntent = getIntent();
        if(myIntent!=null)
        {
            email =myIntent.getStringExtra("email");
            password =myIntent.getStringExtra("mot de passe");
        }
    }
}