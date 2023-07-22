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

import com.example.busbooking.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class MainActivity extends Drawerbase {
ActivityMainBinding activityMainBinding;
Button choose;
EditText datepick;
AutoCompleteTextView from,to;
DatePickerDialog.OnDateSetListener setListener;
Button searc;

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Are you sure you want to quit ?");
        builder.setMessage("Do you want to log out");


        builder.setPositiveButton("Quit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,Login.class));
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


        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        allocateActivityTitle("Dashboard");

        from=findViewById(R.id.From);
        to=findViewById(R.id.Destination);
        datepick = findViewById(R.id.date_picker);
        searc=findViewById(R.id.search);

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_employer);

        bottomNavigationView.setSelectedItemId(R.id.Bus);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.Ticket:
                        startActivity(new Intent(getApplicationContext(),cancelticket.class));
                        overridePendingTransition(0,0);
                        return  true;

                    case R.id.Receipt:
                        startActivity(new Intent(getApplicationContext(),print.class));
                        overridePendingTransition(0,0);
                        return  true;
                }
                return false;
            }
        });

        searc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(),SearchedBuses.class));
                String str=to.getText().toString().trim();
                String str1=from.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("To",str);
                bundle.putString("from",str1);

                Intent intent1
                        =new Intent(MainActivity.this,SearchedBuses.class);
                intent1.putExtras(bundle);
                startActivity(intent1);
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
                        MainActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,
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
    }
}