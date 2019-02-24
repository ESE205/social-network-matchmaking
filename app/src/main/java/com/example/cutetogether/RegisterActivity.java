package com.example.cutetogether;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RegisterActivity extends AppCompatActivity {

    private String mUsername;
    private String mEmail;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }
}
