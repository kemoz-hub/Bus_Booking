package com.example.busbooking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.busbooking.databinding.ActivitySearchedBusesBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class SearchedBuses extends Drawerbase {
    availableadapter adapter;
    RecyclerView recyclerView;
    DatabaseReference reference;
    FirebaseUser user;
    TextView fromp,Top;
    String from,to;
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
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(SearchedBuses.this);
        builder.setTitle("Are you sure you want to quit ?");
        builder.setMessage("Do you want to log out");


        builder.setPositiveButton("Quit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(SearchedBuses.this,Login.class));
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
    protected void onCreate(Bundle savedInstanceState) {
        ActivitySearchedBusesBinding activitySearchedBusesBinding;
        super.onCreate(savedInstanceState);
        activitySearchedBusesBinding = ActivitySearchedBusesBinding.inflate(getLayoutInflater());
        setContentView(activitySearchedBusesBinding.getRoot());
        allocateActivityTitle("Available Buses");

        fromp=findViewById(R.id.frompass);
        Top=findViewById(R.id.Topass);
        text=findViewById(R.id.textmessage);

        Bundle bundle=getIntent().getExtras();

        if (bundle!=null ){
             from =bundle.getString("To");
             to =bundle.getString("from");
            fromp.setText(to);
            Top.setText(from);
        }


        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference rootRef= FirebaseDatabase.getInstance().getReference();

        recyclerView=findViewById(R.id.recycleviewS);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager( SearchedBuses.this));

        /*FirebaseRecyclerOptions<addbuses> options=
                new FirebaseRecyclerOptions.Builder<addbuses>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Buses").orderByChild("to").equalTo(to),addbuses.class)
                        .build();

        adapter=new availableadapter(options);
        recyclerView.setAdapter(adapter);*/


       FirebaseRecyclerOptions<addbuses> options1=
                new FirebaseRecyclerOptions.Builder<addbuses>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Buses").orderByChild("from").equalTo(to),addbuses.class)
                        .build();

        adapter=new availableadapter(options1);
        recyclerView.setAdapter(adapter);
        adapter.startListening();


        FirebaseRecyclerOptions<addbuses> options2=
                new FirebaseRecyclerOptions.Builder<addbuses>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Buses").orderByChild("to").equalTo(from),addbuses.class)
                        .build();

        adapter=new availableadapter(options2);
        recyclerView.setAdapter(adapter);
        adapter.startListening();


    if(adapter.getItemCount()==0){
        text.setVisibility(View.GONE);
    }
    else{
        text.setVisibility(View.VISIBLE);
    }


    }
}