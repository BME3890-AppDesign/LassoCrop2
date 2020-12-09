package com.example.bme3890projectapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.util.Set;

public class ResultInfo extends AppCompatActivity {

    private BottomNavigationView navView;
    private TextView tv_date;
    private TextView tv_size;
    private ImageView iv_Image;
    public int id;
    public String name;
    public String path;
    public String date;
    public String size;
    public static final String NAME_EXTRA = "com.example.bme3890projectapp.EXTRA.NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_info);

        navView = (BottomNavigationView) findViewById(R.id.bnv_navbar);
        navView.setOnNavigationItemSelectedListener(bottomNavMethod);

        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_size = (TextView) findViewById(R.id.tv_size);
        iv_Image = (ImageView) findViewById(R.id.iv_Image);

        Intent i = getIntent();
        id = i.getIntExtra(MyResults.ID_EXTRA, -1);
        name = i.getStringExtra(MyResults.NAME_EXTRA);

        //Toast.makeText(getApplicationContext(), "" + id, Toast.LENGTH_LONG).show();

        SharedPreferences sizes = getSharedPreferences("sizes", Context.MODE_PRIVATE);
        SharedPreferences dates = getSharedPreferences("dates", Context.MODE_PRIVATE);
        SharedPreferences imagePath = getSharedPreferences("imagepath", Context.MODE_PRIVATE);

        // access date and change to array
        Set<String> dateSet = dates.getStringSet(name, null);
        String[] dateArray = new String[dateSet.size()];
        dateSet.toArray(dateArray);

        // access sizes and change to array
        Set<String> sizeSet = sizes.getStringSet(name,null);
        String[] sizeArray = new String[sizeSet.size()];
        sizeSet.toArray(sizeArray);

        // access image paths and change to array
        Set<String> pathSet = imagePath.getStringSet(name, null);
        String[] pathArray = new String[pathSet.size()];
        pathSet.toArray(pathArray);

        tv_size.setText("Artifact Size: " + sizeArray[id] + " mm");
        size = sizeArray[id];
        date = dateArray[id];
        tv_date.setText(dateArray[id]);

        path = pathArray[id];
        Bitmap imageBitmap = BitmapFactory.decodeFile(path);
        iv_Image.setImageBitmap(imageBitmap);

    }

    public void emailResults(View view) {


        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        // set the type to 'email'
        emailIntent.setType("vnd.android.cursor.dir/email");
        // the attachment
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Latest Artifact Size");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "My artifact size from " + date + " is: " + size + " mm.");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));

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