package com.example.bme3890projectapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ForgotPassword extends AppCompatActivity {

    private EditText et_email;
    private EditText et_user;
    private android.widget.Button send;
    public static final String NAME_EXTRA = "com.example.bme3890projectapp.EXTRA.NAME";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //et_email = (EditText) findViewById(R.id.et_email);
        et_user = (EditText) findViewById(R.id.et_user);



    }

    public void enter(View view) {
        SharedPreferences loginInfo = getSharedPreferences("logins", Context.MODE_PRIVATE);
        String user = et_user.getText().toString();

        if (!loginInfo.contains(user)) {
            Toast.makeText(getApplicationContext(),"Username does not exist. Please sign up for a new account.",Toast.LENGTH_LONG).show();
        }

        else {
            Intent i = new Intent (ForgotPassword.this, SecurityQuestion.class);
            i.putExtra(NAME_EXTRA, user);
            startActivity(i);
        }

    }

    //public void sendEmail(View view) {

       // SharedPreferences loginInfo = getSharedPreferences("logins",
            //    Context.MODE_PRIVATE);
       // String user = et_user.getText().toString();
       // String password = loginInfo.getString(user, "");

      //  String address = et_email.getText().toString();
      //  Intent intent = new Intent(Intent.ACTION_SENDTO);
      //  intent.setData(Uri.parse("mailto:"));
      //  intent.putExtra(Intent.EXTRA_EMAIL, address);
      //  intent.putExtra(Intent.EXTRA_SUBJECT,"Your password is: " + password);

      //  if (intent.resolveActivity(getPackageManager()) != null) {
            //startActivity(intent);

      //  }
       // else {
           // Toast.makeText(getApplicationContext(), "no available app to send email", Toast.LENGTH_LONG).show();
      //  }

   // }


}