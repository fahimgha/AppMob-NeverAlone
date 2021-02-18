package com.example.neveralone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    EditText e_mail3;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        e_mail3 = (EditText)findViewById(R.id.editTextTextEmailAddress3);
        auth = FirebaseAuth.getInstance();

    }

    public void goToPasswordActivity(View v){

        //verifier si le mail est déja enregistré ou pas



    }
}