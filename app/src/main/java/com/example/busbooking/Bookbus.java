package com.example.busbooking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Bookbus extends AppCompatActivity {
ImageButton image1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookbus);

        image1=findViewById(R.id.imageView1);
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    image1.setBackgroundResource(getResources().getIdentifier("seat", "drawable", getPackageName()));
                } else {

                    image1.setBackgroundResource(getResources().getIdentifier("seat", "drawable", getPackageName()));
                }


            }
        });
    }
}