package com.example.bme3890projectapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ResetPassword extends AppCompatActivity {

    private EditText et_newPassword;
    private EditText et_confirmPassword;
    public String user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        et_newPassword = (EditText) findViewById(R.id.et_newPassword);
        et_confirmPassword = (EditText) findViewById(R.id.et_confirmPassword);
        Intent i = getIntent();
        user = i.getStringExtra(ForgotPassword.NAME_EXTRA);

    }

    public void resetPassword(View view) {
        String newPassword = et_newPassword.getText().toString();
        String confirmPassword = et_confirmPassword.getText().toString();

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(getApplicationContext(), "Passwords do not match. Please retype password.", Toast.LENGTH_LONG).show();
        }

        else {
            SharedPreferences pref = getSharedPreferences("logins", MODE_PRIVATE);
            SharedPreferences.Editor prefEditor = pref.edit();
            prefEditor.putString(user, newPassword);
            prefEditor.apply();
            Toast.makeText(getApplicationContext(), "Password reset. Please login.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ResetPassword.this, MainActivity.class);
            startActivity(intent);

        }

    }

}