package com.example.busbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.busbooking.databinding.ActivityManageusersBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Context;

import java.util.ArrayList;

public class Manageusers extends Drawerbase {
    RecyclerView recyclerView;
    ArrayList<userhelper> list;
    DatabaseReference databaseReference;
    myadapter adapter;
    Context mcontext;
    Activity mactivity;
    ActivityManageusersBinding activityManageusersBinding;
    //when the back button is clicked it goes back to MainAdmin activity
    @Override

        public void onBackPressed() {
            AlertDialog.Builder builder=new AlertDialog.Builder(Manageusers.this);
            builder.setTitle("Are you sure you want to quit ?");
            builder.setMessage("Do you want to log out");


            builder.setPositiveButton("Quit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(Manageusers.this,Login.class));
                }
            });
            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getApplicationContext(), "cancelled", Toast.LENGTH_SHORT).show();
                }
            });
            builder.show();
            return;
        }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityManageusersBinding = ActivityManageusersBinding.inflate(getLayoutInflater());
        setContentView(activityManageusersBinding.getRoot());
        allocateActivityTitle("Manage Users");


        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_employer);

        bottomNavigationView.setSelectedItemId(R.id.Musers);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.Add_Buses:
                        startActivity(new Intent(getApplicationContext(),MainActivity2.class));
                        overridePendingTransition(0,0);
                        return  true;

                    case R.id.Mreports:
                        startActivity(new Intent(getApplicationContext(),printadmin.class));
                        overridePendingTransition(0,0);
                        return  true;
                }
                return false;
            }
        });

        recyclerView=findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager( Manageusers.this));

        //it gets all data from userhelper and fills the data to the recyclervier
        FirebaseRecyclerOptions<userhelper> options=
                new FirebaseRecyclerOptions.Builder<userhelper>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("users"),userhelper.class)
                        .build();
        adapter=new myadapter(options);
        recyclerView.setAdapter(adapter);
    }
}