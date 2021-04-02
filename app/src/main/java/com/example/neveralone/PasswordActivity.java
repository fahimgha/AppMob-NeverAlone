package com.example.neveralone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordActivity extends AppCompatActivity {

    String email;
    EditText e2_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        e2_password = (EditText)findViewById(R.id.editTextPassWord2);
        Intent myIntent = getIntent();
        if(myIntent!=null)
        {
            email = myIntent.getStringExtra("email");
        }
    }
    public void goToNamePicActivity(View v)
    {
        if(e2_password.getText().toString().length() >6)
        {
            Intent myIntent = new Intent(PasswordActivity.this, NameActivity.class);
            myIntent.putExtra("email",email);
            myIntent.putExtra("password", e2_password.getText().toString());
            startActivity(myIntent);
            finish();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Le mot de passe doit être composé de 6 caractères au minimum.", Toast.LENGTH_SHORT).show();
        }
    }
}