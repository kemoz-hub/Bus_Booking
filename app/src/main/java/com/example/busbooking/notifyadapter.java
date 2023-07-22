package com.example.busbooking;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class notifyadapter extends ArrayAdapter {
    private Activity context;
    List<messages> messagesList;

    public notifyadapter(Activity context, List<messages> messages) {
        super(context, R.layout.notifyme, messages);
        this.context = context;
        this.messagesList = messages;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listItem = inflater.inflate(R.layout.notifyme, null, true);

        TextView idno = listItem.findViewById(R.id.messageN);


        messages cmm = messagesList.get(position);

        idno.setText(cmm.getMessage());


        return listItem;

    }
}