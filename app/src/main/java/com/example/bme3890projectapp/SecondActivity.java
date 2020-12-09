package com.example.bme3890projectapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SecondActivity extends AppCompatActivity {

    private TextView tv_welcome;
    private String url;
    private String url2;
    public String name, name2, name3, name4;
    private BottomNavigationView navView;
    public static final String NAME_EXTRA = "com.example.bme3890projectapp.EXTRA.NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        tv_welcome = (TextView) findViewById(R.id.tv_welcome);
        url = "https://www.skincancer.org/skin-cancer-information/basal-cell-carcinoma/";
        url2 = "https://redcap.vanderbilt.edu/surveys/?s=97XCD73KKM";

        Intent loginToApp = getIntent();
        //get extra info from intent
        name = loginToApp.getStringExtra(MainActivity.NAME_EXTRA);

        Intent i = getIntent();
        name2 = i.getStringExtra(ImageCalculations.NAME_EXTRA);

        Intent i1 = getIntent();
        name3 = i1.getStringExtra(MyResults.NAME_EXTRA);
        name4 = i1.getStringExtra(ResultInfo.NAME_EXTRA);

        //put string in textView
        if (name == null) {
            tv_welcome.setText("Welcome!");
        } else {
            tv_welcome.setText("Welcome, " + name + "!");
        }


        findViewById(R.id.btn_aboutBCC).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAboutBCC(url = "https://www.skincancer.org/skin-cancer-information/basal-cell-carcinoma/");

            }
        });

        navView = (BottomNavigationView) findViewById(R.id.bnv_navbar);
        navView.setOnNavigationItemSelectedListener(bottomNavMethod);
    }



    public void toHome(MenuItem item) {
        Intent i = new Intent(this, SecondActivity.class);
        i.putExtra(NAME_EXTRA, name);
        startActivity(i);
    }

    public void toThird(MenuItem item) {

        Intent i = new Intent(this, Email.class);
        i.putExtra(NAME_EXTRA, name);
        startActivity(i);
    }

    public void toNewVisit(View view) {
        Uri webpage2 = Uri.parse(url2);
        Intent i = new Intent(Intent.ACTION_VIEW, webpage2);
        if (i.resolveActivity(getPackageManager())!= null) {
            startActivity(i);
        }

    }

    public void toMyImages(View view) {
        Intent i = new Intent(this, MyImages.class);
        i.putExtra(NAME_EXTRA, name);
        startActivity(i);

    }

    public void toMyResults(View view) {
        Intent i = new Intent(this, MyResults.class);
        if (name == null){
            i.putExtra(NAME_EXTRA, name2);
            startActivity(i);
        }

        else {
            i.putExtra(NAME_EXTRA, name);
            startActivity(i);
        }


    }

    public void toAboutUs(View view) {
        Intent i = new Intent(this, AboutUs.class);
        startActivity(i);

    }

    public void toAboutBCC(String url) {
        Uri webpage = Uri.parse(url);
        Intent i = new Intent(Intent.ACTION_VIEW, webpage);
        if (i.resolveActivity(getPackageManager())!= null) {
            startActivity(i);
        }

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

    public void toCamera(View view) {
        Intent i = new Intent(this, Camera.class);
        if (name == null){
            i.putExtra(NAME_EXTRA, name3);
            startActivity(i);
        }

        else if (name3 == null) {
            i.putExtra(NAME_EXTRA, name4);
            startActivity(i);

        }
        else
            i.putExtra(NAME_EXTRA, name);
            startActivity(i);
    }

}