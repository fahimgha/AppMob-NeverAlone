package com.example.neveralone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;



import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class NameActivity extends AppCompatActivity {
    String email, password;
    EditText e_PersonName;
    //CircleImageView circleImageView;

    //Uri resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        e_PersonName = (EditText) findViewById(R.id.editTextTextPersonName);
        //circleImageView = (CircleImageView) findViewById(R.id.circleImageView);
        Intent myIntent = getIntent();
        if (myIntent != null) {
            email = myIntent.getStringExtra("email");
            password = myIntent.getStringExtra("password");
        }
    }

    public void generateCode(View v) {
        Date myDate = new Date();
        SimpleDateFormat format1 = new SimpleDateFormat("aaaa-MM-dd HH:mm:ss a", Locale.getDefault());
        String date = format1.format(myDate);
        Random r = new Random();

        int n = 10000 + r.nextInt(900000);
        String code = String.valueOf(n);
        // a la place de e_PersonName remplacer par resultUri
        if (e_PersonName.getText().toString().length()!=0) {
            Intent myIntent = new Intent(NameActivity.this, InviteCodeActivity.class);
            myIntent.putExtra("name", e_PersonName.getText().toString());
            myIntent.putExtra("email", email);
            myIntent.putExtra("password", password);
            myIntent.putExtra("date", date);
            myIntent.putExtra("isSharing", "false");
            myIntent.putExtra("code", code);
            //myIntent.putExtra("ImageUri", resultUri);

            startActivity(myIntent);
            finish();

        } else {
            Toast.makeText(getApplicationContext(), "Entrez votre nom s'il vous plait", Toast.LENGTH_SHORT).show();
        }
    }
}