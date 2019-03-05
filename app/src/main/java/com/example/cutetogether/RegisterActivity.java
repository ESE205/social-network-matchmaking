package com.example.cutetogether;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class RegisterActivity extends AppCompatActivity {

    private EditText mUsername;
    private EditText mEmail;
    private EditText mConfirmEmail;
    private EditText mPassword;
    private Button mRegister;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);

        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        mUsername = findViewById(R.id.username_input_register);
        mEmail = findViewById(R.id.email_input_register);
        mConfirmEmail = findViewById(R.id.confirm_email_input_register);
        mPassword = findViewById(R.id.pwd_input_register);
        mRegister = findViewById(R.id.register_btn_register);


        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();
                attemptRegisterUser();
            }
        });




    }

    public void attemptRegisterUser(){
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        try {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("CT", "createUserWithEmail:success");
                                //FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                finish();
                                startActivity(intent);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("CT", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // ...
                    }
                });
    }
}
