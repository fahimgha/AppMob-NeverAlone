package com.example.neveralone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;

import com.goodiebag.pinview.Pinview;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class JoinCircleActivity extends AppCompatActivity {

    Pinview pinview;
    DatabaseReference reference, currentReference;
    FirebaseUser user;
    FirebaseAuth auth;
    String current_user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_circle);
        pinview =(Pinview)findViewById(R.id.pinview);
        auth =FirebaseAuth.getInstance();
        user =auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        currentReference =FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());

        current_user_id =user.getUid();


    }
    public void submitButtonClick(View v){
        // check si le code est present dans la base de donnée
        // si le code est present, puis trouver l'utilisateur et ensuite creer un cercle
         Query query = reference.orderByChild("circlecode").equalTo(pinview.getValue());

         query.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 if(snapshot.exists()){
                     CreateUser createUser = null;
                     for(DataSnapshot childDss : snapshot.getChildren()){
                         createUser = childDss.getValue(CreateUser.class);
                     }
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });
    }
}