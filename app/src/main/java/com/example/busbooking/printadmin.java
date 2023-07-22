package com.example.busbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.busbooking.databinding.ActivityPrintBinding;
import com.example.busbooking.databinding.ActivityPrintadminBinding;
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

public class printadmin extends Drawerbase {
    ActivityPrintadminBinding activityPrintadminBinding;

    FirebaseAuth mAuth;
    DatabaseReference userref;
    DatabaseReference payref;


    List<addbuses> complaintsfirebaseList;

    public  static  String[]PERMISSIONS={
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static int PERMISSION_ALL = 12;


    public static File pFile;
    private File payfile;
    private PDFView pdfView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPrintadminBinding = ActivityPrintadminBinding.inflate(getLayoutInflater());
        setContentView(activityPrintadminBinding.getRoot());
        allocateActivityTitle("print ticket receipt");

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_employer);

        bottomNavigationView.setSelectedItemId(R.id.Mreports);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.Musers:
                        startActivity(new Intent(getApplicationContext(),Manageusers.class));
                        overridePendingTransition(0,0);
                        return  true;

                    case R.id.Add_Buses:
                        startActivity(new Intent(getApplicationContext(),MainActivity2.class));
                        overridePendingTransition(0,0);
                        return  true;
                }
                return false;
            }
        });
        mAuth = FirebaseAuth.getInstance();
        userref = FirebaseDatabase.getInstance().getReference().child("users");
        payref = FirebaseDatabase.getInstance().getReference().child("Buses");
        pdfView = findViewById(R.id.pdf_viewer1);


        Button reportButton = findViewById(R.id.gen1);
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previewDisabledUsersReport();

            }
        });

        //
        complaintsfirebaseList= new ArrayList<>();

        //create files in charity care folder
        payfile = new File("/storage/emulated/0/Report/");

        //check if they exist, if not create them(directory)
        if ( !payfile.exists()) {
            payfile.mkdirs();
        }
        pFile = new File(payfile, "Bus booking.pdf");

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
                    Log.d("Buses", "Departure: " + pays.getDeparture());
                    Log.d("Buses","phone:"+pays.getEmail());
                    Log.d("Buses","from:"+pays.getFrom());
                    Log.d("Buses","To:"+pays.getTo());
                    Log.d("Buses","Id number:"+pays.getIdno());

                    /* The error before was cause by giving incorrect data type
                    You were adding an object of type PaymentUsers yet the arraylist expects obejct of type DisabledUsers
                     */
                    complaintsfirebaseList.add(pays);


                }
                //create a pdf file and catch exception beacause file may not be created
                try {
                    createPaymentReport(complaintsfirebaseList);
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
        BaseColor colorWhite = WebColors.getRGBColor("#ffffff");
        BaseColor colorBlue = WebColors.getRGBColor("#056FAA");
        BaseColor grayColor = WebColors.getRGBColor("#425066");



        Font white = new Font(Font.FontFamily.HELVETICA, 15.0f, Font.BOLD, colorWhite);
        Font blue = new Font(Font.FontFamily.HELVETICA, 15.0f, Font.BOLD, colorBlue);
        FileOutputStream output = new FileOutputStream(pFile);
        Document document = new Document(PageSize.A4);
        PdfPTable table = new PdfPTable(new float[]{6, 25, 20, 25,20,25,30,20});
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setFixedHeight(50);
        table.setTotalWidth(PageSize.A4.getWidth());
        table.setWidthPercentage(100);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

        Chunk noText = new Chunk("No.", white);
        PdfPCell noCell = new PdfPCell(new Phrase(noText));
        noCell.setFixedHeight(50);
        noCell.setBorder(10);
        noCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        noCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk nameText = new Chunk("Bus number", white);
        PdfPCell nameCell = new PdfPCell(new Phrase(nameText));
        nameCell.setFixedHeight(50);
        nameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        nameCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk phoneText = new Chunk("Amount", white);
        PdfPCell phoneCell = new PdfPCell(new Phrase(phoneText));
        phoneCell.setFixedHeight(50);
        phoneCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        phoneCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk amountText = new Chunk("Departure", white);
        PdfPCell amountCell = new PdfPCell(new Phrase(amountText));
        amountCell.setFixedHeight(50);
        amountCell.setVerticalAlignment(Element.ALIGN_CENTER);
        amountCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk docText = new Chunk("phone number", white);
        PdfPCell doccell = new PdfPCell(new Phrase(docText));
        doccell.setFixedHeight(50);
        doccell.setHorizontalAlignment(Element.ALIGN_CENTER);
        doccell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk TocText = new Chunk("From", white);
        PdfPCell Typecell = new PdfPCell(new Phrase(TocText));
        Typecell.setFixedHeight(50);
        Typecell.setHorizontalAlignment(Element.ALIGN_CENTER);
        Typecell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk ObText = new Chunk("To", white);
        PdfPCell OBcell = new PdfPCell(new Phrase(ObText));
        OBcell.getHorizontalAlignment();
        OBcell.setFixedHeight(50);
        OBcell.setBorder(Rectangle.TOP|Rectangle.RIGHT| Rectangle.LEFT);
        OBcell.setHorizontalAlignment(Element.ALIGN_BOTTOM);
        OBcell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk genText = new Chunk("Id number", white);
        PdfPCell gencell = new PdfPCell(new Phrase(genText));
        gencell.setFixedHeight(50);
        gencell.setHorizontalAlignment(Element.ALIGN_CENTER);
        gencell.setVerticalAlignment(Element.ALIGN_CENTER);
        gencell.setColspan(4);




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

        PdfPTable footTable = new PdfPTable(new float[]{6, 25, 20, 20,20,25,30,20});
        footTable.setTotalWidth(PageSize.A4.getWidth());
        footTable.setWidthPercentage(100);
        footTable.addCell(footCell);
        footTable.getHorizontalAlignment();

        PdfWriter.getInstance(document, output);
        document.open();
        Font g = new Font(Font.FontFamily.HELVETICA, 25.0f, Font.NORMAL, grayColor);
        document.add(new Paragraph(" Bus ticket Booking\n\n", g));
        document.add(table);
        document.add(footTable);

        document.close();
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
        Intent steptwoIntent = new Intent(printadmin.this, MainActivity.class);
        steptwoIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(steptwoIntent);
        finish();
    }

    private void SendUserToLoginActivity()
    {
        Intent loginIntent = new Intent(printadmin.this, MainActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    public void previewDisabledUsersReport()
    {
        DisplayReport();
       /* if (hasPermissions(this, PERMISSIONS)) {
            DisplayReport();
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }*/
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


    }

}