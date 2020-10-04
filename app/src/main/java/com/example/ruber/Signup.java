package com.example.ruber;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {

    EditText txtEmail, txtPassword, txtConfirmPassword;
    Button btn_register;
    ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().setTitle("Signup");
        txtEmail = (EditText)findViewById(R.id.txt_email);
        txtPassword = (EditText)findViewById(R.id.txt_password);
        txtConfirmPassword = (EditText)findViewById(R.id.txt_confirmPassword);
        btn_register = (Button) findViewById(R.id.btnRegister);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        firebaseAuth = FirebaseAuth.getInstance();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = txtEmail.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();
                String confirmpassword = txtConfirmPassword.getText().toString().trim();


                if (TextUtils.isEmpty(email)){
                    Toast.makeText(Signup.this, "Please Entre Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    Toast.makeText(Signup.this, "Please Entre Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(confirmpassword)){
                    Toast.makeText(Signup.this, "Please Entre Confirm Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length()<6){

                    Toast.makeText(Signup.this, "Password too short", Toast.LENGTH_SHORT).show();

                }

                progressBar.setVisibility(View.VISIBLE);

                if (password.equals(confirmpassword)){

                    firebaseAuth.createUserWithEmailAndPassword(email, password)

                            .addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        //startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                        Toast.makeText(Signup.this, "Register Complete", Toast.LENGTH_SHORT).show();

                                        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
                                        Users user_object = new Users(txtEmail.getText().toString(),txtPassword.getText().toString());
                                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                        Log.d("TAGME" , "user id"+ firebaseUser.getUid());
                                        databaseReference.child(firebaseUser.getUid()).setValue(user_object)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful())
                                                        {
                                                            Toast.makeText(getApplicationContext(),"User data saved",Toast.LENGTH_LONG).show();
                                                        }else{
                                                            Toast.makeText(getApplicationContext(),"User data could not be saved",Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                    } else {
                                        Log.d("TAGME", "exception xas " + task.getException().getLocalizedMessage());

                                        Toast.makeText(Signup.this, "Authentication Failde", Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });


                }

            }
        });
    }
}
