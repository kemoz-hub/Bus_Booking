package com.example.busbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.busbooking.databinding.ActivityMainBinding;
import com.example.busbooking.databinding.ActivityPrintBinding;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.exception.FileNotFoundException;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class print extends Drawerbase {
    ActivityPrintBinding activityPrintBinding;

    FirebaseAuth mAuth;
    DatabaseReference userref;
    DatabaseReference payref;

    ListView mylist;
    List<messages> messagesList;
    DatabaseReference reference, reference2;

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(print.this);
        builder.setTitle("Are you sure you want to quit ?");
        builder.setMessage("Do you want to log out");


        builder.setPositiveButton("Quit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(print.this, Login.class));
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
        activityPrintBinding = ActivityPrintBinding.inflate(getLayoutInflater());
        setContentView(activityPrintBinding.getRoot());
        allocateActivityTitle("notifications");


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_employer);

        bottomNavigationView.setSelectedItemId(R.id.Receipt);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.Ticket:
                        startActivity(new Intent(getApplicationContext(), cancelticket.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.Bus:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });


        mylist = findViewById(R.id.mylistview);
        messagesList = new ArrayList<>();

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference2 = FirebaseDatabase.getInstance().getReference("users");
        String userid = user.getUid();


       final TextView idTextview = (TextView) findViewById(R.id.not);


        reference2.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userhelper applicantsprofile = snapshot.getValue(userhelper.class);
                if (applicantsprofile != null) {

                    String id = applicantsprofile.id;

                    idTextview.setText(id);
                    reference = FirebaseDatabase.getInstance().getReference("messages");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            messagesList.clear();
                            for (DataSnapshot datasnap : snapshot.getChildren()) {

                                messages r = datasnap.getValue(messages.class);
                                messagesList.add(r);

                            }
                            notifyadapter adapter = new notifyadapter(print.this, messagesList);
                            mylist.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(print.this, "something wrong happened", Toast.LENGTH_SHORT).show();
            }
        });


        notifyadapter adapter = new notifyadapter(print.this,messagesList);
        mylist.setAdapter(adapter);

      /*  mAuth = FirebaseAuth.getInstance();
        userref = FirebaseDatabase.getInstance().getReference().child("users");
        payref = FirebaseDatabase.getInstance().getReference().child("Buses");
        pdfView = findViewById(R.id.pdf_viewer);


        Button reportButton = findViewById(R.id.gen);
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previewDisabledUsersReport();

            }

        });

        //
        addbuseslist= new ArrayList<>();

        //create files in charity care folder
        payfile = new File("/storage/emulated/0/Report/");

        //check if they exist, if not create them(directory)
        if ( !payfile.exists()) {
            payfile.mkdirs();
        }
        pFile = new File(payfile, "Bus Tickt.pdf");

        //fetch payment and disabled users details;
        fetchPaymentUsers();
    }
    //function to fetch payment data from the database
    private void fetchPaymentUsers()
    {

        payref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    //creating an object and setting to displlay
                    addbuses pays = new addbuses();
                    pays.setBus_number(snapshot.child("bus_number").getValue().toString());
                    pays.setCost(snapshot.child("cost").getValue().toString());
                    pays.setDeparture(snapshot.child("departure").getValue().toString());
                    pays.setEmail(snapshot.child("email").getValue().toString());
                    pays.setFrom(snapshot.child("from").getValue().toString());
                    pays.setTo(snapshot.child("to").getValue().toString());
                    pays.setIdno(snapshot.child("idno").getValue().toString());



                    //this just log details fetched from db(you can use Timber for logging
                    Log.d("Buses", "Bus no: " + pays.getBus_number());
                    Log.d("Buses", "Amount: " + pays.getCost());
                    Log.d("Buses", "Departure: " + pays.getFrom());
                    Log.d("Buses","phone:"+pays.getEmail());
                    Log.d("Buses","from:"+pays.getFrom());
                    Log.d("Buses","To:"+pays.getTo());
                    Log.d("Buses","Id number:"+pays.getIdno());


                    /* The error before was cause by giving incorrect data type
                    You were adding an object of type PaymentUsers yet the arraylist expects obejct of type DisabledUsers
                     */
                    /*addbuseslist.add(pays);


                }
                //create a pdf file and catch exception beacause file may not be created
                try {
                    createPaymentReport(addbuseslist);
                } catch (DocumentException | FileNotFoundException | java.io.FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void createPaymentReport(List<addbuses> paymentUsersList) throws DocumentException, FileNotFoundException, java.io.FileNotFoundException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            BaseColor colorWhite = WebColors.getRGBColor("#ffffff");
            BaseColor colorBlue = WebColors.getRGBColor("#056FAA");
            BaseColor grayColor = WebColors.getRGBColor("#425066");


            Font white = new Font(Font.FontFamily.HELVETICA, 15.0f, Font.BOLD, colorWhite);
            Font blue = new Font(Font.FontFamily.HELVETICA, 15.0f, Font.BOLD, colorBlue);
            FileOutputStream output = new FileOutputStream(pFile);
            Document document = new Document(PageSize.A4);
            PdfPTable table = new PdfPTable(new float[]{6, 25, 20, 20, 20, 25, 30, 20, 20});
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.getDefaultCell().setFixedHeight(50);
            table.setTotalWidth(PageSize.A4.getWidth());
            table.setWidthPercentage(100);
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

            Chunk noText = new Chunk("No.", white);
            PdfPCell noCell = new PdfPCell(new Phrase(noText));
            noCell.setFixedHeight(50);
            noCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            noCell.setVerticalAlignment(Element.ALIGN_CENTER);

            Chunk nameText = new Chunk("Id number", white);
            PdfPCell nameCell = new PdfPCell(new Phrase(nameText));
            nameCell.setFixedHeight(50);
            nameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            nameCell.setVerticalAlignment(Element.ALIGN_CENTER);

            Chunk phoneText = new Chunk("Full Name", white);
            PdfPCell phoneCell = new PdfPCell(new Phrase(phoneText));
            phoneCell.setFixedHeight(50);
            phoneCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            phoneCell.setVerticalAlignment(Element.ALIGN_CENTER);

            Chunk amountText = new Chunk("Phone Number", white);
            PdfPCell amountCell = new PdfPCell(new Phrase(amountText));
            amountCell.setFixedHeight(50);
            amountCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            amountCell.setVerticalAlignment(Element.ALIGN_CENTER);

            Chunk docText = new Chunk("DOC", white);
            PdfPCell doccell = new PdfPCell(new Phrase(docText));
            doccell.setFixedHeight(50);
            doccell.setHorizontalAlignment(Element.ALIGN_CENTER);
            doccell.setVerticalAlignment(Element.ALIGN_CENTER);

            Chunk TocText = new Chunk("TOC", white);
            PdfPCell Typecell = new PdfPCell(new Phrase(TocText));
            Typecell.setFixedHeight(50);
            Typecell.setHorizontalAlignment(Element.ALIGN_CENTER);
            Typecell.setVerticalAlignment(Element.ALIGN_CENTER);

            Chunk ObText = new Chunk("OB Number", white);
            PdfPCell OBcell = new PdfPCell(new Phrase(ObText));
            OBcell.setFixedHeight(50);
            OBcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            OBcell.setVerticalAlignment(Element.ALIGN_CENTER);

            Chunk genText = new Chunk("gender", white);
            PdfPCell gencell = new PdfPCell(new Phrase(genText));
            gencell.setFixedHeight(50);
            gencell.setHorizontalAlignment(Element.ALIGN_CENTER);
            gencell.setVerticalAlignment(Element.ALIGN_CENTER);

            Chunk statText = new Chunk("status", white);
            PdfPCell statcell = new PdfPCell(new Phrase(statText));
            statcell.setFixedHeight(50);
            statcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            statcell.setVerticalAlignment(Element.ALIGN_CENTER);


            Chunk footerText = new Chunk("Dennis kipkemei - Copyright @ 2022");
            PdfPCell footCell = new PdfPCell(new Phrase(footerText));
            footCell.setFixedHeight(70);
            footCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            footCell.setVerticalAlignment(Element.ALIGN_CENTER);
            footCell.setColspan(4);


            table.addCell(noCell);
            table.addCell(nameCell);
            table.addCell(phoneCell);
            table.addCell(amountCell);
            table.addCell(doccell);
            table.addCell(Typecell);
            table.addCell(OBcell);
            table.addCell(gencell);
            table.addCell(statcell);
            table.setHeaderRows(1);

            PdfPCell[] cells = table.getRow(0).getCells();


            for (PdfPCell cell : cells) {
                cell.setBackgroundColor(grayColor);
            }
            for (int i = 0; i < paymentUsersList.size(); i++) {
                addbuses pay = paymentUsersList.get(i);

                String id = String.valueOf(i + 1);
                String name = pay.getBus_number();
                String sname = pay.getCost();
                String phone = pay.getFrom();
                String date = pay.getEmail();
                String type = pay.getFrom();
                String ob = pay.getTo();
                String gen = pay.getIdno();



                table.addCell(id + ". ");
                table.addCell(name);
                table.addCell(sname);
                table.addCell(phone);
                table.addCell(date);
                table.addCell(type);
                table.addCell(ob);
                table.addCell(gen);


            }

            PdfPTable footTable = new PdfPTable(new float[]{6, 25, 20, 20, 20, 25, 30, 20});
            footTable.setTotalWidth(PageSize.A4.getWidth());
            footTable.setWidthPercentage(100);
            footTable.addCell(footCell);

            PdfWriter.getInstance(document, output);
            document.open();
            Font g = new Font(Font.FontFamily.HELVETICA, 25.0f, Font.NORMAL, grayColor);
            document.addCreator("Nchiru police station");
            document.add(new Paragraph(" CRIME REPORTING SYSTEM\n\n", g));
            document.add(table);
            document.add(footTable);

            document.close();
        }
    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    //onstart method used to check if the user is registered or not
    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser ==null){
            SendUserToLoginActivity();
        }
        else{
            //checking if the user exists in the firebase database
            CheckUserExistence();
        }
    }

    private void CheckUserExistence()
    {
        //get the user id
        final String currentUserId = mAuth.getCurrentUser().getUid();
        userref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (!dataSnapshot.hasChild(currentUserId)){
                    //user is authenticated but but his record is not present in real time firebase database
                    SendUserToStepTwoAuthentication();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void SendUserToStepTwoAuthentication()
    {
        Intent steptwoIntent = new Intent(print.this, Login.class);
        steptwoIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(steptwoIntent);
        finish();
    }

    private void SendUserToLoginActivity()
    {
        Intent loginIntent = new Intent(print.this, Login.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    public void previewDisabledUsersReport()
    {
        DisplayReport();
            /*if (hasPermissions(this, PERMISSIONS)) {
                DisplayReport();
            } else {
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
            }
    }

    private void DisplayReport()
    {
        pdfView.fromFile(pFile)
                .pages(0,2,1,3,3,3)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .load();


    }*/

    }
}