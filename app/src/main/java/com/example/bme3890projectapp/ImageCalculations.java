package com.example.bme3890projectapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ImageCalculations extends AppCompatActivity implements View.OnTouchListener {

    private ImageView fullImage;
    private GraphView rgbChart;
    private int bitmapWidth, bitmapHeight, scaledHeight, scaledWidth;
    private BottomNavigationView navView;
    private double artSize;
    public static List<Point> points;
    private Bitmap imageBitmap, croppedBitmap;
    private int x1, y1, x2, y2, croppedWidth, croppedHeight;

    private TextView tv_artifactSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_calculations);


        fullImage = (ImageView) findViewById(R.id.iv_fullImage);
        rgbChart = (GraphView) findViewById(R.id.gv_graph);
        tv_artifactSize = (TextView) findViewById(R.id.tv_artifactSize);
        navView = (BottomNavigationView) findViewById(R.id.bnv_navbar);
        navView.setOnNavigationItemSelectedListener(bottomNavMethod);

        SharedPreferences imageInfo = getSharedPreferences("images",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor loginEditor = imageInfo.edit();

        Intent imageCalculations = getIntent();
        String currentPhotoPath = imageCalculations.getStringExtra(TakePhoto.NAME_EXTRA);
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        loginEditor.putString(date, currentPhotoPath);
        loginEditor.apply();

        imageBitmap = BitmapFactory.decodeFile(currentPhotoPath);
        fullImage.setImageBitmap(imageBitmap);
        bitmapWidth = imageBitmap.getWidth();
        bitmapHeight = imageBitmap.getHeight();
        scaledHeight = imageBitmap.getScaledHeight(294);
        scaledWidth = imageBitmap.getScaledWidth(294);


        artSize = (bitmapWidth/32.13)/1.335; //32.13 = ratio of pixels to mm, 10 -> converts to cm
        tv_artifactSize.setText("Artifact diameter: " + artSize + "mm");



        points = new ArrayList<Point>();
        fullImage.setOnTouchListener(this);



    }

    public boolean onTouch(View view, MotionEvent event) {

        if (points.size() < 2) {
            if (points.size() == 0) {
                Point point = new Point();
                point.x = (int) event.getX();
                point.y = (int) event.getY();
                points.add(point);
                //first.setText(point.toString());
            }
            else {
                Point point = new Point();
                point = new Point();
                point.x = (int) event.getX();
                point.y = (int) event.getY();
                points.add(point);
                //second.setText(point.toString());

            }

            return false;
        }
        else {
            Toast.makeText(getApplicationContext(), "2 pixels already touched on image. Click crop button", Toast.LENGTH_LONG).show();
            return true;
        }
    }

    public void cropImage(View view) {

        if (points.size() == 2) {
            Toast.makeText(getApplicationContext(), "inside loop", Toast.LENGTH_LONG).show();
            for (int i = 0; i < points.size(); i++) {
                if (i == 0) {
                    Point one = points.get(i);
                    x1 = one.getX();
                    y1 = one.getY();
                } else {
                    Point two = points.get(i);
                    x2 = two.getX();
                    y2 = two.getY();
                }
            }


            //Log.d("myTag", Integer.toString(x1) + ", " + Integer.toString(y1));
            //Log.d("myTag", Integer.toString(x2) + ", " + Integer.toString(y2));
            //Log.d("myTag", Integer.toString(x2 - x1) + ", " + Integer.toString(y2 - y1));
            croppedWidth = x2-x1;
            croppedHeight = y2-y1;

            croppedBitmap = Bitmap.createBitmap(imageBitmap ,x1, y1, croppedWidth, croppedHeight);

            fullImage.setImageBitmap(croppedBitmap);

        }

        else {
            Toast.makeText(getApplicationContext(), "Touch 2 pixels on the image again", Toast.LENGTH_LONG).show();
            points.clear();
        }
    }


    public void makeGraph(View view) {

        Intent imageCalculations = getIntent();
        String currentPhotoPath = imageCalculations.getStringExtra(TakePhoto.NAME_EXTRA);
        Bitmap imageBitmap = BitmapFactory.decodeFile(currentPhotoPath);


      // int length = (bitmapHeight-1)*(bitmapWidth-1);

        //graphing 3 random pixels
        DataPoint[] red = new DataPoint[3];
        int redValue = Color.red(imageBitmap.getPixel(0,0));
        red[0] = new DataPoint(0, redValue);
        redValue = Color.red(imageBitmap.getPixel(bitmapWidth/2,bitmapHeight/2));
        red[1] = new DataPoint(1,redValue);
        redValue = Color.red(imageBitmap.getPixel(bitmapWidth-1,bitmapHeight-1));
        red[2] = new DataPoint(2,redValue);


       /* for (int k = 0; k<= length; k++){
            for (int i = 0; i <= bitmapHeight/6; i++) {
                for (int j = 0; j <= bitmapWidth/6; j++) {
                    redValue = Color.red(imageBitmap.getPixel(j,i));
                    red[k] = new DataPoint(k, redValue);

                }
            }
        } */

        LineGraphSeries<DataPoint> redSeries = new LineGraphSeries<>(red);

        rgbChart.addSeries(redSeries);
        rgbChart.setTitle("Red Values of the Pixels");
        rgbChart.getGridLabelRenderer().setVerticalAxisTitle("Red Value");
        rgbChart.getGridLabelRenderer().setHorizontalAxisTitle("Pixel Number");

    }


    public void toHome(MenuItem item) {
        Intent i = new Intent(this, SecondActivity.class);
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