package com.example.bme3890projectapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SignUp extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText et_newPassword;
    private EditText et_newUsername;
    private EditText et_confirmPassword;
    private EditText et_securityAnswer;
    private EditText et_confirmAnswer;
    private android.widget.Button create;
    public Spinner dropdown;
    public String question;
    public static final String NAME_EXTRA = "com.example.bme3890projectapp.EXTRA.NAME";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        et_newUsername = (EditText) findViewById(R.id.et_newUsername);
        et_newPassword = (EditText) findViewById(R.id.et_newPassword);
        et_confirmPassword = (EditText) findViewById(R.id.et_confirmPassword);
        et_securityAnswer = (EditText) findViewById(R.id.et_securityAnswer);
        create = (Button) findViewById(R.id.btn_createAccount);
        et_confirmAnswer = (EditText) findViewById(R.id.et_confirmAnswer);
        // CREATE DROP DOWN MENU FOR SECURITY QUESTIONS
        //get the spinner from the xml.
        dropdown = findViewById(R.id.spinner);
        //create a list of items for the spinner.
        String[] securityQuestions = new String[]{"What is the name of your favorite teacher?", "What was the make and model of your first car?", "What was your high school mascot?", "What street did you live on in third grade?", "Who was your childhood best friend?"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, securityQuestions);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);


    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        question = parent.getItemAtPosition(pos).toString();
        //Toast.makeText(parent.getContext(), question, Toast.LENGTH_SHORT).show();

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void createAccount(View view) {

        SharedPreferences loginInfo = getSharedPreferences("logins", Context.MODE_PRIVATE);
        SharedPreferences.Editor loginEditor2 = loginInfo.edit();
        // new shared preferences files for security question and answer, key = username
        SharedPreferences securityAns = getSharedPreferences("securityAs", Context.MODE_PRIVATE);
        SharedPreferences.Editor securityEditor = securityAns.edit();
        SharedPreferences securityQ = getSharedPreferences("securityQs", Context.MODE_PRIVATE);
        SharedPreferences.Editor securityQuestionsEditor = securityQ.edit();

        String newUsername = et_newUsername.getText().toString();
        String newPassword = et_newPassword.getText().toString();
        String confirmPassword = et_confirmPassword.getText().toString();
        String securityAnswer = et_securityAnswer.getText().toString();
        String confirmAnswer = et_confirmAnswer.getText().toString();

        if (loginInfo.contains(newUsername)) {
            Toast.makeText(getApplicationContext(),"Username already on file. Please pick a different username.",Toast.LENGTH_LONG).show();
        }

        else if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(getApplicationContext(), "Passwords do not match. Please retype password.", Toast.LENGTH_LONG).show();
        }
        else if (!securityAnswer.equals(confirmAnswer)) {
            Toast.makeText(getApplicationContext(), "Answers to the security question do not match. Please retype answer.", Toast.LENGTH_LONG).show();
        }
        else {
            loginEditor2.putString(newUsername, newPassword);
            loginEditor2.apply();
            securityEditor.putString(newUsername, securityAnswer);
            securityEditor.apply();
            securityQuestionsEditor.putString(newUsername, question);
            securityQuestionsEditor.apply();
            Toast.makeText(getApplicationContext(),"Account created!",Toast.LENGTH_LONG).show();
            Intent loginToApp = new Intent (SignUp.this, SecondActivity.class);
            loginToApp.putExtra(NAME_EXTRA, newUsername);
            startActivity(loginToApp);

        }



    }
}