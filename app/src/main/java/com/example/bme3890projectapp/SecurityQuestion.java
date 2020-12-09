package com.example.bme3890projectapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SecurityQuestion extends AppCompatActivity {

    private TextView tv_securityQuestion;
    private EditText et_answer;
    public String user;
    public static final String NAME_EXTRA = "com.example.bme3890projectapp.EXTRA.NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_question);

        tv_securityQuestion = (TextView) findViewById(R.id.tv_securityQuestion);
        et_answer = (EditText) findViewById(R.id.et_answer);
        Intent i = getIntent();
        user = i.getStringExtra(ForgotPassword.NAME_EXTRA);

        SharedPreferences securityQ = getSharedPreferences("securityQs", Context.MODE_PRIVATE);

        String question = securityQ.getString(user, "");
        tv_securityQuestion.setText(question);

    }

    public void submit(View view) {

        SharedPreferences securityA = getSharedPreferences("securityAs", Context.MODE_PRIVATE);
        String enteredAnswer = et_answer.getText().toString();
        String answer = securityA.getString(user, "");
        if (!enteredAnswer.equals(answer)) {
            Toast.makeText(getApplicationContext(), "Answer to security question is incorrect. Please try again.", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "Answer is correct. Please reset your password.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(SecurityQuestion.this, ResetPassword.class);
            intent.putExtra(NAME_EXTRA, user);
            startActivity(intent);
        }
    }


}