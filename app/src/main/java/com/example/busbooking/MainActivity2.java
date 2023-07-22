package com.example.busbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.busbooking.databinding.ActivityMain2Binding;
import com.example.busbooking.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class MainActivity2 extends Drawerbase {

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity2.this);
        builder.setTitle("Are you sure you want to quit ?");
        builder.setMessage("Do you want to log out");


        builder.setPositiveButton("Quit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity2.this,Login.class));
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

    ActivityMain2Binding activityMain2Binding;
    AutoCompleteTextView from,to;
    Button Add;
    EditText datepick,cost,busno;
    DatePickerDialog.OnDateSetListener setListener;
    FirebaseDatabase rootNode;
    DatabaseReference reference,databaseReference;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMain2Binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(activityMain2Binding.getRoot());
        allocateActivityTitle("ADD buses");



        from=findViewById(R.id.FromA);
        to=findViewById(R.id.DestinationA);
        datepick = findViewById(R.id.date_pickerA);
        Add=findViewById(R.id.Add);
        cost=findViewById(R.id.cost);
        busno=findViewById(R.id.busno);

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_employer);

        bottomNavigationView.setSelectedItemId(R.id.Add_Buses);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.Musers:
                        startActivity(new Intent(getApplicationContext(),Manageusers.class));
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

        String[] location= {"BUSIA","KITALE","ELDORET","NAKURU","NAIROBI","LIMURU","MACHAKOS","EMBU","THIKA","MERU","MAUA"};

        ArrayAdapter<String> ss = new ArrayAdapter<String>(this,R.layout.dropdownlocation,location);
        ss.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        to.setAdapter(ss);

        to.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String location2=adapterView.getItemAtPosition(i).toString();

            }
        });

        String[] location1= {"BUSIA","KITALE","ELDORET","NAKURU","NAIROBI","LIMURU","MACHAKOS","EMBU","THIKA","MERU","MAUA"};

        ArrayAdapter<String> dd = new ArrayAdapter<String>(this,R.layout.dropdownlocation,location);
        ss.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        from.setAdapter(ss);

        from.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String location2=adapterView.getItemAtPosition(i).toString();

            }
        });

        Calendar calender=Calendar.getInstance();
        final int Year=calender.get(Calendar.YEAR);
        final int month=calender.get(Calendar.MONTH);
        final int Day=calender.get(Calendar.DAY_OF_MONTH);

        //date picker initializing current date,year and month
        datepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(
                        MainActivity2.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        setListener,Year,month,Day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-10000);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        setListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                String date=year+"/"+month+"/"+dayOfMonth;
                datepick.setText(date);
            }
        };
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("Buses");
                databaseReference = rootNode.getReference("users");
                String Uid =FirebaseAuth.getInstance().getCurrentUser().getUid();


                String From=from.getText().toString().trim();
                String TO=to.getText().toString().trim();
                String Cost=cost.getText().toString().trim();
                String Busno=busno.getText().toString().trim();
                String Departure=datepick.getText().toString().trim();


                addbuses addbuses = new addbuses("","",Busno,Uid,From,TO,Departure, Cost,"");
                reference.child(Busno).setValue(addbuses);
                Toast.makeText(MainActivity2.this, "Bus Added successfully", Toast.LENGTH_SHORT).show();

            }
        });
    }
}