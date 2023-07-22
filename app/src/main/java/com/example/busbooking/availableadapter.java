package com.example.busbooking;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class availableadapter extends FirebaseRecyclerAdapter<addbuses,availableadapter.MyviewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public availableadapter(@NonNull FirebaseRecyclerOptions<addbuses> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyviewHolder holder, int position, @NonNull addbuses model) {

        holder.busno.setText(model.getBus_number());
        holder.from.setText(model.getFrom());
        holder.to.setText(model.getTo());
        holder.departuretime.setText(model.getDeparture());
        holder.Amount.setText(model.getCost());



        holder.book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseUser user;
                DatabaseReference reference,reference2;
                user= FirebaseAuth.getInstance().getCurrentUser();
                reference= FirebaseDatabase.getInstance().getReference("users");
                String userid=user.getUid();

                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.busno.getContext())
                        .setContentHolder(new ViewHolder(R.layout.payment))
                        .setExpanded(true, 1200)
                        .setGravity(Gravity.CENTER)
                        .create();

                View v = dialogPlus.getHolderView();

                TextView Amount =(EditText) v.findViewById(R.id.AmountP);
                TextView idno=(EditText)v.findViewById(R.id.idnop);
                TextView to=(EditText)v.findViewById(R.id.ToPP);
                TextView from=(EditText)v.findViewById(R.id.FromPP);
                TextView depart=(EditText)v.findViewById(R.id.departureP);
                TextView bus=(EditText)v.findViewById(R.id.BusnoP);
                TextView fullname=(EditText)v.findViewById(R.id.Names);
                AutoCompleteTextView paymethod=v.findViewById(R.id.payMeth);
                AutoCompleteTextView seat=v.findViewById(R.id.seatno);
                EditText paycode=(EditText)v.findViewById(R.id.code);
                TextView phone=v.findViewById(R.id.phoneP);
                Button book=v.findViewById(R.id.paid);



                to.setText(model.getTo());
                from.setText(model.getFrom());
                bus.setText(model.getBus_number());
                Amount.setText(model.getCost());
                depart.setText(model.getDeparture());


                String[] statusk={"Mpesa","Airtel Money","Telkom money"};

                ArrayAdapter<String> aa=new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_spinner_item,statusk);
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                paymethod.setAdapter(aa);
                paymethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        Toast.makeText(v.getContext(), statusk[i],Toast.LENGTH_LONG).show();
                        String text=adapterView.getItemAtPosition(i).toString();
                        paymethod.setText(text);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                String[] seatk={"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","27","28"};

                ArrayAdapter<String> bb=new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_spinner_item,seatk);
                bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                seat.setAdapter(bb);
                seat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        Toast.makeText(v.getContext(), seatk[i],Toast.LENGTH_LONG).show();
                        String text=adapterView.getItemAtPosition(i).toString();
                        paymethod.setText(text);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                reference.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userhelper userprofile = snapshot.getValue(userhelper.class);
                        if (userprofile != null) {

                            String name = userprofile.getId();
                            String full = userprofile.getFullname();
                            String phoneno = userprofile.getPhone();


                            idno.setText(name);
                            fullname.setText(full);
                            phone.setText(phoneno);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                dialogPlus.show();

                book.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                      DatabaseReference databaseReference;
                        FirebaseDatabase rootNode;
                        rootNode = FirebaseDatabase.getInstance();
                        databaseReference = rootNode.getReference("Tickets");

                        String cost1=Amount.getText().toString().trim();
                        String idnumber=idno.getText().toString().trim();
                        String Pay=paymethod.getText().toString().trim();
                        String seatnot=seat.getText().toString().trim();
                        String code1=paycode.getText().toString().trim();
                        String busno=bus.getText().toString().trim();
                        String frome=from.getText().toString().trim();
                        String toe=to.getText().toString().trim();
                        String departe=depart.getText().toString().trim();

                        databaseReference.orderByChild("seatno").equalTo(seatnot).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    Toast.makeText(v.getContext(), "seat  number already booked", Toast.LENGTH_SHORT).show();
                                    seat.setError("seat number has been booked");

                                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                                    builder.setTitle("seat already booked");
                                    builder.setMessage("This seat number is already booked");
                                    builder.show();



                                }
                                else{

                                    reference.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                            userhelper userprofile = snapshot1.getValue(userhelper.class);
                                            if (userprofile != null) {

                                                String name = userprofile. getId();
                                                String full = userprofile.getFullname();
                                                String phoneno = userprofile.getPhone();


                                                idno.setText( name );
                                                fullname.setText(full);
                                                phone.setText(phoneno);





                                                DatabaseReference reference1,reference2,reference3;
                                                reference1=FirebaseDatabase.getInstance().getReference("Payments");
                                                reference2=FirebaseDatabase.getInstance().getReference("Tickets");
                                                reference3=FirebaseDatabase.getInstance().getReference("messages");

                                                paymentfirebase paymentfirebase=new paymentfirebase(full,cost1,name,Pay,code1);
                                                reference1.child(cost1).setValue(paymentfirebase);

                                                Bookedbuses bookedbuses=new Bookedbuses(full,"",busno,"",frome,toe,departe,cost1,idnumber,seatnot);
                                                reference2.child(cost1).setValue(bookedbuses);

                                                String messo="Dear" +full+ "your busno is" +busno+ "and seat no is " +seatnot+ "travel safe";

                                                messages messages=new messages(messo);
                                                reference3.child(userid).setValue(messages);

                                                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                                                builder.setTitle("Booked successfully");
                                                builder.setMessage("You have successfully booked busno" +busno+ "with seatno"+seatnot+"");
                                                builder.show();


                                                Toast.makeText(v.getContext(), "Booked successfully", Toast.LENGTH_SHORT).show();

                                                String message="Dear" +full+ "your busno is" +busno+ "and seat no is " +seatnot+ "travel safe";
                                                NotificationCompat.Builder builder1 = new NotificationCompat.Builder(v.getContext())
                                                        .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                                                        .setContentTitle("Bus Booking system")
                                                        .setContentText(message)
                                                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                                                        .setAutoCancel(true);

                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                    NotificationChannel channel = new NotificationChannel("My notification", "My notification", NotificationManager.IMPORTANCE_DEFAULT);
                                                    NotificationManager manager = v.getContext().getSystemService(NotificationManager.class);
                                                    manager.createNotificationChannel(channel);
                                                    builder1.setChannelId("My notification");
                                                }

                                                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(v.getContext());
                                                managerCompat.notify(0, builder1.build());


                                                Intent intent = new Intent(v.getContext(), print.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                intent.putExtra("message3", "Dear" +full+ "your busno is" +busno+ "and seat no is " +seatnot+ "travel safe");

                                                PendingIntent pendingIntentk = PendingIntent.getActivity(v.getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                                builder1.setContentIntent(pendingIntentk);

                                                NotificationManager notificationManager = (NotificationManager) v.getContext().getSystemService(NOTIFICATION_SERVICE);

                                                notificationManager.notify(0, builder1.build());

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });


                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }

                        });

                    }
                });

            }
        });
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.availablebuses,parent,false);
        return  new MyviewHolder(view);
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        TextView busno,from,to,departuretime,Amount;
        Button book;
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            busno=itemView.findViewById(R.id.textbusno);
            from=itemView.findViewById(R.id.textfrom);
            to=itemView.findViewById(R.id.textto);
            departuretime=itemView.findViewById(R.id.textdeparture);
            Amount=itemView.findViewById(R.id.textamount);
            book=itemView.findViewById(R.id.bookC);

        }
    }
}
