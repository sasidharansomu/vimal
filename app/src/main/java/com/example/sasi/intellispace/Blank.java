package com.example.sasi.intellispace;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Blank extends AppCompatActivity {
  String name;
  String date;
    CalendarView cal;
    ImageButton ib;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        name=getIntent().getExtras().getString("name");
        setContentView(R.layout.activity_blank);
        cal= (CalendarView) findViewById(R.id.calendarView);
        ib= (ImageButton) findViewById(R.id.imageButton);
        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month ,
                                            int dayOfMonth) {
                // TODO Auto-generated method stub
                month=month+1;
                String s=String.valueOf(month);
                s="0"+s;
                      date=dayOfMonth+"-"+s+"-"+year;
                Toast.makeText(getBaseContext(),"Selected Date is\n\n"
                                +date ,
                        Toast.LENGTH_LONG).show();
            }
        });

        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookingAdapter.setBookdate(date);
                Toast.makeText(Blank.this,  date+"", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Blank.this,Speechengine.class));
            }
        });

    }
}
