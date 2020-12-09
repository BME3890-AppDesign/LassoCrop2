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
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class ImageCalculations extends AppCompatActivity implements View.OnTouchListener {

    private ImageView fullImage;
    private GraphView rgbChart;
    private int bitmapWidth, bitmapHeight;
    private BottomNavigationView navView;
    private double artSize;
    public static List<Point> points;
    private Bitmap imageBitmap, croppedBitmap;
    private int x1, y1, x2, y2, croppedWidth, croppedHeight;
    public String name;
    public String currentPhotoPath;
    public String date;
    private TextView tv_artifactSize;
    public static final String NAME_EXTRA = "com.example.bme3890projectapp.EXTRA.NAME";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_calculations);


        fullImage = (ImageView) findViewById(R.id.iv_fullImage);
        rgbChart = (GraphView) findViewById(R.id.gv_graph);
        tv_artifactSize = (TextView) findViewById(R.id.tv_artifactSize);
        navView = (BottomNavigationView) findViewById(R.id.bnv_navbar);
        navView.setOnNavigationItemSelectedListener(bottomNavMethod);

        Intent loginToApp = getIntent();
        //get extra info from intent
        name = loginToApp.getStringExtra(TakePhoto.USERNAME_EXTRA);
        SharedPreferences imagePaths = getSharedPreferences("images",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor loginEditor = imagePaths.edit();

        Intent imageCalculations = getIntent();
        currentPhotoPath = imageCalculations.getStringExtra(TakePhoto.NAME_EXTRA);


        loginEditor.putString(name, currentPhotoPath);
        loginEditor.apply();

        imageBitmap = BitmapFactory.decodeFile(currentPhotoPath);
        fullImage.setImageBitmap(imageBitmap);
        bitmapWidth = imageBitmap.getWidth();
        bitmapHeight = imageBitmap.getHeight();
        //scaledHeight = imageBitmap.getScaledHeight(294);
        //scaledWidth = imageBitmap.getScaledWidth(294);

        points = new ArrayList<Point>();
        fullImage.setOnTouchListener(this);

    }

    public void saveImage(View view) {

        date = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z", Locale.getDefault()).format(new Date());

        SharedPreferences imagePath = getSharedPreferences("imagepath", Context.MODE_PRIVATE);
        SharedPreferences.Editor imagepathEditor = imagePath.edit();

        SharedPreferences sizes = getSharedPreferences("sizes", Context.MODE_PRIVATE);
        SharedPreferences.Editor resultEditor = sizes.edit();

        SharedPreferences dates = getSharedPreferences("dates", Context.MODE_PRIVATE);
        SharedPreferences.Editor datesEditor = dates.edit();

        if (imagePath.getStringSet(name, null ) == null) {
            String [] pathArray = {currentPhotoPath};
                    Set<String> mySet = new HashSet<String>(Arrays.asList(pathArray));
            imagepathEditor.putStringSet(name, mySet).apply();

            String [] sizeArray = {"" + artSize};
                Set<String> s2 = new HashSet<String>(Arrays.asList(sizeArray));
            resultEditor.putStringSet(name, s2);
            resultEditor.apply();

            String [] dateArray = {date};
            Set<String> s3 = new HashSet<String>(Arrays.asList(dateArray));
            datesEditor.putStringSet(name, s3);
            datesEditor.apply();
            Toast.makeText(getApplicationContext(),"Saved.", Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, SecondActivity.class);
            i.putExtra(NAME_EXTRA,name);
            startActivity(i);


        }
        else {
            Set<String> s = imagePath.getStringSet(name, null);
            s.add(currentPhotoPath);
            imagepathEditor.putStringSet(name,s).apply();

            Set<String> set = sizes.getStringSet(name, null);
            set.add("" + artSize);
            resultEditor.putStringSet(name,set).apply();

            Set<String> set2 = dates.getStringSet(name, null);
            set2.add(date);
            datesEditor.putStringSet(name,set2).apply();

            Toast.makeText(getApplicationContext(),"Saved.", Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, SecondActivity.class);
            i.putExtra(NAME_EXTRA,name);
            startActivity(i);

        }



    }

    public boolean onTouch(View view, MotionEvent event) {

        int xRatio = imageBitmap.getWidth() / fullImage.getWidth();
        int yRatio = imageBitmap.getHeight() / fullImage.getHeight();

        if (points.size() < 2) {
            if (points.size() == 0) {
                Point point = new Point();
                point.x = (int) event.getX()*xRatio;
                point.y = (int) event.getY()*yRatio;
                points.add(point);
                //Toast.makeText(getApplicationContext(), point.toString(), Toast.LENGTH_LONG).show();
                //first.setText(point.toString());
            }
            else {
                Point point = new Point();
                point = new Point();
                point.x = (int) (event.getX()*xRatio);
                point.y = (int) (event.getY()*yRatio);
                points.add(point);
                //Toast.makeText(getApplicationContext(), point.toString(), Toast.LENGTH_LONG).show();
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
            //Toast.makeText(getApplicationContext(), "inside loop", Toast.LENGTH_LONG).show();
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
            croppedBitmap = Bitmap.createBitmap(imageBitmap ,x2, y2, croppedWidth, croppedHeight);
            //Toast.makeText(getApplicationContext(), croppedWidth, Toast.LENGTH_LONG).show();
            //Toast.makeText(getApplicationContext(), bitmapWidth, Toast.LENGTH_LONG).show();
            fullImage.setImageBitmap(croppedBitmap);

            points.clear();

            artSize = (croppedWidth/32.13)/1.335; //32.13 = ratio of pixels to mm, 1.335 = ratio of relative size (on screen) to actual size
            tv_artifactSize.setText("Artifact diameter: " + artSize + " mm");

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