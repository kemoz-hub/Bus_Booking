package com.example.busbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.busbooking.databinding.ActivityCancelticketBinding;
import com.example.busbooking.databinding.ActivityMainBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class cancelticket extends Drawerbase {

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(cancelticket.this);
        builder.setTitle("Are you sure you want to quit ?");
        builder.setMessage("Do you want to log out");


        builder.setPositiveButton("Quit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(cancelticket.this,Login.class));
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
    ActivityCancelticketBinding activityCancelticketBinding;

    ticketadapter adapter;
    RecyclerView recyclerView;
    DatabaseReference reference;
    FirebaseUser user;
    TextView text;

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
        activityCancelticketBinding = ActivityCancelticketBinding.inflate(getLayoutInflater());
        setContentView(activityCancelticketBinding.getRoot());
        allocateActivityTitle("Cancel Ticket");



        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference rootRef= FirebaseDatabase.getInstance().getReference();

        recyclerView=findViewById(R.id.recyclerT);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager( cancelticket.this));

        user=FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("users");
        String userid=user.getUid();

        final TextView fullTextview=(TextView) findViewById(R.id.textstat);

        reference.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userhelper userprofile=snapshot.getValue(userhelper.class);

                if(userprofile!=null){

                    String full=userprofile.id;
                    fullTextview.setText(full);


                    FirebaseRecyclerOptions<Bookedbuses> options=
                            new FirebaseRecyclerOptions.Builder<Bookedbuses>()
                                    .setQuery(FirebaseDatabase.getInstance().getReference("Tickets").orderByChild("idno").equalTo(full),Bookedbuses.class)
                                    .build();

                    adapter=new ticketadapter(options);
                    recyclerView.setAdapter(adapter);
                    adapter.startListening();

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(cancelticket.this, "something wrong happened", Toast.LENGTH_SHORT).show();
            }
        });

        FirebaseRecyclerOptions<Bookedbuses> options=
                new FirebaseRecyclerOptions.Builder<Bookedbuses>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Tickets"),Bookedbuses.class)
                        .build();

        adapter=new ticketadapter(options);
        recyclerView.setAdapter(adapter);

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_employer);

        bottomNavigationView.setSelectedItemId(R.id.Ticket);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.Receipt:
                        startActivity(new Intent(getApplicationContext(),print.class));
                        overridePendingTransition(0,0);
                        return  true;

                    case R.id.Bus:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return  true;
                }
                return false;
            }
        });


    }
}