package com.example.busbooking;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {
Button login;
    FirebaseDatabase rootNode;
    DatabaseReference reference,databaseReference;
    EditText mFullName, mEmail, mPassword, mPhone,mconfirm2,mid;
    AutoCompleteTextView mconfirm;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    Button button,btnRegLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        login
                =findViewById(R.id.btnRegLogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });

        mFullName = findViewById(R.id.mFullName);
        mEmail = findViewById(R.id.mEmail);
        mPassword = findViewById(R.id.password);
        mconfirm = findViewById(R.id.confirmpass);
        mid = findViewById(R.id.id_no);
        mPhone = findViewById(R.id.mPhone);
        button = findViewById(R.id.buttonl1);
        btnRegLogin=findViewById(R.id.btnRegLogin);



        String[] location= {"BUSIA","KITALE","ELDORET","NAKURU","NAIROBI","LIMURU","MACHAKOS","EMBU","THIKA","MERU","MAUA"};

        ArrayAdapter<String> ss = new ArrayAdapter<String>(this,R.layout.dropdownlocation,location);
        ss.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mconfirm.setAdapter(ss);

        mconfirm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String location2=adapterView.getItemAtPosition(i).toString();

            }
        });

        btnRegLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login.class));

            }
        });

        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.INVISIBLE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("users");
                databaseReference = rootNode.getReference("users");


                String fullname=mFullName.getText().toString().trim();
                String email=mEmail.getText().toString().trim();
                String password=mPassword.getText().toString().trim();
                String phone=mPhone.getText().toString().trim();
                String confirm=mconfirm.getText().toString().trim();
                String id=mid.getText().toString().trim();



                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("password is required");
                    return;
                }
                if (mPassword.length() < 6) {
                    mPassword.setError(("password must be>=6 characters"));
                    return;
                }
                if(TextUtils.isEmpty(id)) {
                    mid.setError("please enter id number");

                    return;
                }
                if(mid.length()!=8){
                    mid.setError("please enter valid id number");
                    return;
                }
                /*if(mid.length()==8 && mPhone.length()==10){
                    databaseReference.orderByChild("id").equalTo(id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                Toast.makeText(register.this, "id number already exist", Toast.LENGTH_SHORT).show();
                                mid.setError("id number already exist");

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    return;
                }*/
                if(TextUtils.isEmpty(phone)) {
                    mPhone.setError("please enter phone number");
                    return;
                }
                if(mPhone.length()!=10){
                    mPhone.setError("please enter valid phone number");
                    return;
                }



                if(TextUtils.isEmpty(fullname)) {
                    mFullName.setError("please enter your full names");
                }


                else {

                    progressBar.setVisibility(View.VISIBLE);
                    fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                if (task.isSuccessful()) {
                                    databaseReference.orderByChild("id").equalTo(id).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                Toast.makeText(Register.this, "id number already exist", Toast.LENGTH_SHORT).show();
                                                mid.setError("id number already exist");
                                                progressBar.setVisibility(View.GONE);

                                            }
                                            else{
                                                progressBar.setVisibility(View.VISIBLE);
                                                String Uid = task.getResult().getUser().getUid();
                                                userhelper userhelper = new userhelper(Uid, fullname, email, password, phone, confirm, id, 0, "");
                                                reference.child(Uid).setValue(userhelper);
                                                Toast.makeText(Register.this, "user created", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(), Login.class));
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                        }

                                    });
                                    return;
                                }

                            }
                            else {
                                Toast.makeText(Register.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);

                            }

                        }
                    });
                }
            }
        });
    }
}