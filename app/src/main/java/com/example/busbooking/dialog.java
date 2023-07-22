package com.example.busbooking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class dialog extends DialogFragment {
    Button save;
    EditText newp;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
      return view=inflater.inflate(R.layout.dialogfragment,container,false);
        //return inflater.inflate(R.layout.dialogfragment,container,false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        save=view.findViewById(R.id.save);
        newp=view.findViewById(R.id.newpass);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String pass=newp.getText().toString();
                FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                user.updatePassword(pass).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(getActivity(), "profile activated successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getActivity(), "failed to activate profile", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    /*@NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        LayoutInflater inflater=getLayoutInflater();
         view=inflater.inflate(R.layout.dialogfragment,null);
        save=view.findViewById(R.id.save);
        newp=view.findViewById(R.id.newpass);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String pass=newp.getText().toString();
                FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                user.updatePassword(pass).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });

        return builder.create();
    }*/



}
