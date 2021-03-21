package com.example.neveralone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
;
import android.os.Build;
import android.os.Bundle;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;

import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;

public class MyCircleActivity extends AppCompatActivity {


    //@BindView(R.id.recyclerview) RecyclerView recyclerView;
    //@BindView(R.id.textViewNoFound) TextView textViewNoFound;
    //RecyclerView.LayoutManager mLayoutManager;
    MembersAdapter adapter;
    DatabaseReference reference,usersReference;
    FirebaseAuth auth;
    FirebaseUser user;
    CreateUser createUser;
    ArrayList<CreateUser> nameList;
    ArrayList<String> circleuser_idList;

    String memberUserId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_circle);
        //setHasOptionsMenu(true);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);

        nameList = new ArrayList<>();
        circleuser_idList = new ArrayList<>();
        //mLayoutManager = new LinearLayoutManager(this);
        //recyclerView.setLayoutManager(mLayoutManager);
        //recyclerView.setHasFixedSize(true);

        usersReference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("CircleMembers");

        adapter = new MembersAdapter(nameList,MyCircleActivity.this,getApplicationContext());
        recyclerView.setAdapter(adapter);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                nameList.clear();
                if(dataSnapshot.exists()) {
                    //textViewNoFound.setVisibility(View.GONE);
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
        //adapter = new MembersAdapter(nameList,MyCircleActivity.this,getApplicationContext());
        //recyclerView.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
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
            //mYIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mYIntent);

        }

    }
    @Override

    public boolean onCreateOptionsMenu(Menu menu){
        //MenuInflater inflater= getMenuInflater();
        getMenuInflater().inflate(R.menu.menu_refresh_mycircle, menu);
        //inflater.inflate(R.menu.menu_refresh_mycircle, menu);
        return super.onCreateOptionsMenu(menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.navRefresh)
        {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (Build.VERSION.SDK_INT >= 26) {
                transaction.setReorderingAllowed(false);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}