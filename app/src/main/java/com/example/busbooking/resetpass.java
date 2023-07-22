package com.example.busbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class resetpass extends AppCompatActivity {
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    EditText email;
    Button reset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpass);
        email=findViewById(R.id.REmail);
        reset=findViewById(R.id.Rbutn);
        progressBar=findViewById(R.id.progressBarR);

        fAuth=FirebaseAuth.getInstance();
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetpassword();
            }
        });

    }

    private void resetpassword() {
        String Email=email.getText().toString().trim();

        if(Email.isEmpty()){
            email.setError("email is required");
            email.requestFocus();
            return;
        }

       /* if(Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            email.setError("please provide valid email");
            email.requestFocus();
            return;
        }*/
        progressBar.setVisibility(View.VISIBLE);
        fAuth.sendPasswordResetEmail(Email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(resetpass.this);
                    builder.setTitle("check a LINK has been sent to your email");
                    builder.setMessage("click and change password");
                    builder.show();
                    Toast.makeText(resetpass.this, "check your email to reset your password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }else{
                    AlertDialog.Builder builder=new AlertDialog.Builder(resetpass.this);
                    builder.setTitle("This email address  does not exist");
                    builder.setMessage("contact administrator ");
                    builder.show();
                    Toast.makeText(resetpass.this, "Try again something wrong happened", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}