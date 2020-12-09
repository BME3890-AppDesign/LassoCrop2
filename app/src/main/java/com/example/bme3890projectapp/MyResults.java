package com.example.bme3890projectapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Set;

public class MyResults extends AppCompatActivity {

    private BottomNavigationView navView;
    public String name;
    public static final String NAME_EXTRA = "com.example.bme3890projectapp.EXTRA.NAME";
    public static final String ID_EXTRA = "com.example.bme3890projectapp.EXTRA.ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_results);

        navView = (BottomNavigationView) findViewById(R.id.bnv_navbar);
        navView.setOnNavigationItemSelectedListener(bottomNavMethod);

        Intent loginToApp = getIntent();
        //get extra info from intent
        name = loginToApp.getStringExtra(SecondActivity.NAME_EXTRA);
        //Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG).show();


        //access shared preference file -> size, determine the length of the array
        SharedPreferences sizes = getSharedPreferences("sizes", Context.MODE_PRIVATE);
        SharedPreferences dates = getSharedPreferences("dates", Context.MODE_PRIVATE);

        // get string set and size for sizes

        //int sizeSet = s.size();

        // get string set for dates and convert into list so i can iterate through it
        Set<String> s = sizes.getStringSet(name, null);
        Set<String> dateSet = dates.getStringSet(name, null);
        //Toast.makeText(getApplicationContext(), "array size: " + sizeDateSet, Toast.LENGTH_LONG).show();

        //layout in which I want to add button
        LinearLayout layout = (LinearLayout) findViewById(R.id.layout_linear);
        if (dateSet == null) {

            Toast.makeText(getApplicationContext(), "No results yet. Please take a photo to generate results.", Toast.LENGTH_LONG).show();
        }

        else {

            int sizeDateSet = dateSet.size();
            String[] dateArray = new String[dateSet.size()];
            dateSet.toArray(dateArray);
            for (int i = 0; i <= dateSet.size()-1; i++) {
            //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            //   LinearLayout.LayoutParams.MATCH_PARENT,
            //  LinearLayout.LayoutParams.WRAP_CONTENT);
                Button btn = new Button(this);
                btn.setId(i);
                final int id_ = btn.getId();
                btn.setText(dateArray[i]);
                if (i % 2 == 0) {
                    btn.setBackgroundColor(getResources().getColor(R.color.colorBlueMain)); }
                else {
                    btn.setBackgroundColor(getResources().getColor(R.color.colorBlueDark));
                }
                btn.setTextColor(Color.WHITE);
                layout.addView(btn);
                btn = ((Button) findViewById(id_));
                int finalI = i;
                btn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        Intent intent = new Intent(MyResults.this, ResultInfo.class);
                        intent.putExtra(ID_EXTRA, finalI);
                        intent.putExtra(NAME_EXTRA,name);
                        startActivity(intent);

                }
            });


        }
        //create button
               }


       // Button btn = new Button(this);
        //btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
       // btn.setText("Send");
       // btn.setId(1);
       // layout.addView(btn);


    }

    public void toHome(MenuItem item) {
        Intent i = new Intent(this, SecondActivity.class);
        i.putExtra(NAME_EXTRA,name);
        startActivity(i);
    }

    public void toCamera(View view) {
        Intent i = new Intent(this, Camera.class);
        startActivity(i);
    }

    public void toThird(MenuItem item) {
        Intent i = new Intent(this, Email.class);
        startActivity(i);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.mi_home:
                    toHome(null);
                    break;
                case R.id.mi_email:
                    toThird(null);
                    break;
                case R.id.mi_photo:
                    toCamera(null);
                    break;
            }
            return true;
        }
    };
}