package com.example.busbooking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.busbooking.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.UUID;

public class profile extends Drawerbase {
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(profile.this);
        builder.setTitle("Are you sure you want to quit ?");
        builder.setMessage("Do you want to log out");


        builder.setPositiveButton("Quit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(profile.this,profile.class));
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

    Button logout,update;
    ImageButton profile;
    ActivityProfileBinding activityProfileBinding;
    private FirebaseUser user;
    FirebaseDatabase rootNode;
    private DatabaseReference reference;

    ImageView imageView;
    Uri videoUri;
    private  String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProfileBinding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(activityProfileBinding.getRoot());
        allocateActivityTitle("Profile");

        logout=findViewById(R.id.logoutid);
        update=findViewById(R.id.updateid);
        profile=findViewById(R.id.updateP);
        imageView=findViewById(R.id.imageViewP);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,100);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(profile.this);
                builder.setTitle("Are you sure you want to quit ?");
                builder.setMessage("Do you want to log out");

                builder.setPositiveButton("Quit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(profile.this,Login.class));
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();

            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog dialog=new dialog();
                dialog.show(getSupportFragmentManager(),"My Fragment");
                uploadImage();
            }
        });
        user=FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("users");
        userid=user.getUid();


        final TextView emailTextview=(TextView) findViewById(R.id.emailid);
        final TextView fullTextview=(TextView) findViewById(R.id.fullnameid);
        final TextView phoneTextview=(TextView) findViewById(R.id.phoneid);
        final TextView greetTextview=(TextView) findViewById(R.id.greetingid);
        final TextView idTextview=(TextView) findViewById(R.id.id);
        final ImageView imageView=(ImageView)findViewById(R.id.imageViewP);


        reference.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userhelper userprofile=snapshot.getValue(userhelper.class);
                if(userprofile!=null){
                    String email=userprofile.email;
                    String full=userprofile.fullname;
                    String phone=userprofile.phone;
                    String id=userprofile.id;
                    String link=userprofile.uri;


                    try {
                        Picasso.get()
                                .load(link)
                                .into(imageView);
                    }
                    catch (Exception e){

                    }

                    fullTextview.setText(full);
                    idTextview.setText(id);
                    emailTextview.setText(email);
                    phoneTextview.setText(phone);
                    greetTextview.setText("welcome,"+full+"!");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(profile.this, "something wrong happened", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100 && data !=null && data.getData() !=null)
        {
            videoUri=data.getData();
            getImageInImageView();

        }
    }
    private void getImageInImageView() {
        Bitmap bitmap= null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),videoUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageView.setImageBitmap(bitmap);
    }
    private void uploadImage() {
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("updating");
        progressDialog.show();

        FirebaseStorage.getInstance().getReference("images/"+ UUID.randomUUID().toString()).putFile(videoUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful())  {
                                updatecomplain(task.getResult().toString());
                            }
                        }
                    });

                    Toast.makeText(profile.this,"Image uploaded",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(profile.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }


        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress =100.0 * snapshot.getBytesTransferred()/snapshot.getTotalByteCount();
                progressDialog.setMessage("updating profile"+(int)progress+"%");
            }
        });
    }

    private  void updatecomplain(String url) {
        //FirebaseDatabase.getInstance().getReference("users/").setValue(url);
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("uri").setValue(url);
    }
}