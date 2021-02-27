package com.example.neveralone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class RegisterActivity extends AppCompatActivity {

    EditText e_mail3;
    FirebaseAuth auth;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        e_mail3 = (EditText)findViewById(R.id.editTextTextEmailAddress3);
        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this); //reprendre ici PART5 7:22

    }

    public void goToPasswordActivity(View v)
    {
        //verifier si le mail est déja enregistré ou pas
        auth.fetchSignInMethodsForEmail(e_mail3.getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if(task.isSuccessful())
                {
                    dialog.dismiss();
                    boolean check = !task.getResult().getSignInMethods().isEmpty();

                    if (!check)
                    {
                        //l'email n'existe pas donc on peut le créer
                        Intent myIntent = new Intent(RegisterActivity.this,PasswordActivity.class);
                        myIntent.putExtra("email",e_mail3.getText().toString());
                        startActivity(myIntent);
                        finish();
                    }
                    else{
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(),"This email is already registred",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }
}