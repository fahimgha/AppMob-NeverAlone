package com.example.neveralone;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;



public class MyCircleActivity extends AppCompatActivity {


    DatabaseReference reference,usersReference;
    FirebaseAuth auth;
    FirebaseUser user;
    CreateUser createUser;

    ArrayList<CreateUser> nameList;
    ArrayList<String> circleuser_idList;
    MembersAdapter adapter;
    String memberUserId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_circle);
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);

        nameList = new ArrayList<>();
        circleuser_idList = new ArrayList<>();

        usersReference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("CircleMembers");

        adapter = new MembersAdapter(nameList,MyCircleActivity.this,getApplicationContext());
        recyclerView.setAdapter(adapter);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                nameList.clear();
                if(dataSnapshot.exists()) {

                    for (DataSnapshot dss : dataSnapshot.getChildren()) {
                        memberUserId = dss.child("circlememberid").getValue(String.class);

                        usersReference.child(memberUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                createUser = dataSnapshot.getValue(CreateUser.class);
                                nameList.add(createUser);
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),""+databaseError,Toast.LENGTH_LONG).show();
            }
        });
    }
    public void openLiveActivity(final int pos, final ArrayList<CreateUser> nameArrayList)
    {

        CreateUser addCircle = nameArrayList.get(pos);
        String latitude_user = addCircle.lat;
        String longitude_user = addCircle.lng;

        if(latitude_user.equals("na") && longitude_user.equals("na"))
        {
            Toast.makeText(getApplicationContext(),"This circle member is not sharing location.",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Intent mYIntent = new Intent(getApplicationContext(), MapsActivity.class);
            mYIntent.putExtra("lat",latitude_user);
            mYIntent.putExtra("lng",longitude_user);
            mYIntent.putExtra("name",addCircle.name);
            mYIntent.putExtra("userId",addCircle.userId);
            mYIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mYIntent);

        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_refresh_mycircle, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.navRefresh)
        {
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
        }
        return super.onOptionsItemSelected(item);
    }
}