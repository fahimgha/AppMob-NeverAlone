package com.example.neveralone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth auth;

    private EditText e1, e2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //trouver le editText avec l'id du XML
        e1 = (EditText) findViewById(R.id.editTextEmailAddress);
        e2 = (EditText) findViewById(R.id.editTextPassWord);
        //point d'entré  di SDDK d'autentification Firebase
        auth = FirebaseAuth.getInstance();

    }
    public void login(View v){
        //methode pour se connecter
        auth.signInWithEmailAndPassword(e1.getText().toString(),e2.getText().toString())
        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(),"L'utilisateur a réussi à se connecter",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"mot de passe ou email incorrect",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}