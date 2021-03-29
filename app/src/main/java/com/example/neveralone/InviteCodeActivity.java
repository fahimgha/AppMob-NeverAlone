package com.example.neveralone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class InviteCodeActivity extends AppCompatActivity {

    String name,email,password,date,issharing,code;
    //Uri imageUri;
    ProgressDialog progressDialog;
    TextView t1;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    //StorageReference storageReference;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_code);
        t1 =(TextView)findViewById(R.id.textView222);
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        Intent myIntent = getIntent();

        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        //storageReference = FirebaseStorage.getInstance().getReference().child("User_images");
        if (myIntent != null) {
            name = myIntent.getStringExtra("name");
            email = myIntent.getStringExtra("email");
            password = myIntent.getStringExtra("password");
            code = myIntent.getStringExtra("code");
            issharing = myIntent.getStringExtra("isSharing");
            //imageUri= myIntent.getParcelableExtra("imageUri");
        }
        t1.setText(code);

    }
    public void registerUser(View v){

        progressDialog.setMessage("Please wait while we are creating an account for you.");
        progressDialog.show();

        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(InviteCodeActivity.this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            user = auth.getCurrentUser();
                            CreateUser createUser = new CreateUser(name,email,password,code,"false","na","na",user.getUid());
                            user = auth.getCurrentUser();
                            userId = user.getUid();

                            reference.child(userId).setValue(createUser)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {

                                            /*StorageReference sr = storageReference.child(user.getUid() + ".jpg");
                                            sr.putFile(imageUri)
                                                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task1) {
                                                            if (task1.isSuccessful()) {
                                                                String download_image_path = task1.getResult().getMetadata().getReference().getDownloadUrl().toString();
                                                                reference.child(user.getUid()).child("imageUrl").setValue(download_image_path)
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task1) {
                                                                                if (task1.isSuccessful()) {*/
                                                                                    progressDialog.dismiss();
                                                                                    Toast.makeText(getApplicationContext(), "User Registered successfully", Toast.LENGTH_SHORT).show();
                                                                                    Intent myIntent = new Intent(InviteCodeActivity.this, MapsActivity.class);
                                                                                    startActivity(myIntent);
                                                                                /*} else {
                                                                                    progressDialog.dismiss();
                                                                                    Toast.makeText(getApplicationContext(), "erreur lors de la creation d'un compte", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }
                                                                        });
                                                            }
                                                        }
                                                    });*/

                                        }else {
                                                progressDialog.dismiss();
                                                Toast.makeText(getApplicationContext(), "Could not register user.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                        }
                    }

                });
    }
}