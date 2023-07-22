package com.example.busbooking;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class ticketadapter extends FirebaseRecyclerAdapter<Bookedbuses,ticketadapter.MyviewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ticketadapter(@NonNull FirebaseRecyclerOptions<Bookedbuses> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ticketadapter.MyviewHolder holder, int position, @NonNull Bookedbuses model) {
        holder.busno.setText(model.getBus_number());
        holder.from.setText(model.getFrom());
        holder.to.setText(model.getTo());
        holder.departuretime.setText(model.getDeparture());
        holder.Amount.setText(model.getCost());
        holder.Idnumbe.setText(model.getIdno());
        holder.Name.setText(model.getFullname());

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder=new AlertDialog.Builder(holder.Name.getContext());
                builder.setTitle("Are you sure ?");
                builder.setMessage("Deleted data can't be undo");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference("Tickets").child(getRef(holder.getAdapterPosition()).getKey()).removeValue();
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(holder.Name.getContext(), "cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }

        });
    }

    @NonNull
    @Override
    public ticketadapter.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.tickets,parent,false);
        return new
                MyviewHolder(view);
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        TextView busno,from,to,departuretime,Amount,Name,Idnumbe;
        Button cancel;
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);

            busno=itemView.findViewById(R.id.textbusnoT);
            from=itemView.findViewById(R.id.textfromT);
            to=itemView.findViewById(R.id.texttoT);
            departuretime=itemView.findViewById(R.id.textdepartureT);
            Amount=itemView.findViewById(R.id.textamountT);
            Name=itemView.findViewById(R.id.textnameT);
            Idnumbe=itemView.findViewById(R.id.textidnumberT);
            cancel=itemView.findViewById(R.id.cancel);

        }
    }
}
