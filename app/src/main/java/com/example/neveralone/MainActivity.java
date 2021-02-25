package com.example.neveralone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void goToLogin(View V){
        Intent myIntent  =  new Intent(MainActivity.this,LoginActivity.class);
        startActivity(myIntent);
    }
    public void goToRegister(View V){
        Intent myIntent  =  new Intent(MainActivity.this,RegisterActivity.class);
        startActivity(myIntent);
    }
}