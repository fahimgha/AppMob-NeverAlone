package com.example.neveralone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    DatabaseReference reference, joinedReference, circleReference;
    FirebaseUser user;
    FirebaseAuth auth;
    String current_user_id, join_user_id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_circle);
        pinview =(Pinview)findViewById(R.id.pinview);
        auth =FirebaseAuth.getInstance();
        user =auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        joinedReference =FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());

        current_user_id =user.getUid();


    }
    public void submitButtonClick(View v){
        // check si le code est present dans la base de donnée
        // si le code est present, puis trouver l'utilisateur et ensuite creer un cercle
         Query query = reference.orderByChild("code").equalTo(pinview.getValue());

         query.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 if(snapshot.exists())
                 {

                     CreateUser createUser = null;
                     for(DataSnapshot childDss : snapshot.getChildren())
                     {
                         createUser = childDss.getValue(CreateUser.class);
                     }
                     join_user_id = createUser.userId;

                     circleReference = FirebaseDatabase.getInstance().getReference().child("Users").child(join_user_id).child("CircleMembers");
                     joinedReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("CircleMembers");


                     // get the correct values from the user



                     CircleJoin circleJoin = new CircleJoin(current_user_id);
                     final CircleJoin circleJoin1 = new CircleJoin(join_user_id);

                     circleReference.child(user.getUid()).setValue(circleJoin)
                             .addOnCompleteListener(new OnCompleteListener<Void>() {
                                 @Override
                                 public void onComplete(@NonNull Task<Void> task) {
                                     if(task.isSuccessful())
                                     {
                                         joinedReference.child(join_user_id).setValue(circleJoin1)
                                                 .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                     @Override
                                                     public void onComplete(@NonNull Task<Void> task) {
                                                         if(task.isSuccessful())
                                                         {


                                                             pinview.setValue("");
                                                             Toast.makeText(getApplicationContext(),"You joined this circle successfully",Toast.LENGTH_LONG).show();
                                                         }
                                                     }
                                                 });


                                     }
                                     else
                                     {
                                         pinview.setValue("");
                                         Toast.makeText(getApplicationContext(),"Could not join, try again",Toast.LENGTH_SHORT).show();
                                     }
                                 }
                             });


                 }
                 else
                 {
                     pinview.setValue("");
                     Toast.makeText(getApplicationContext(),"Invalid circle code entered",Toast.LENGTH_SHORT).show();
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });
    }
}