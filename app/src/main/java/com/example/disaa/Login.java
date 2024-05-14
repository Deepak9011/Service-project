package com.example.disaa;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    EditText email_value_EditText, password_value_EditText;
    Button button_Button, loginGooglesignIn_Button;
    FirebaseAuth mAuth;
    ProgressBar progressBar_ProgressBar;
    TextView registerNow_TextView;
    GoogleSignInOptions gso;
    GoogleApiClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();
        email_value_EditText = findViewById(R.id.email_value);
        password_value_EditText = findViewById(R.id.password_value);
        button_Button = findViewById(R.id.button);
        progressBar_ProgressBar = findViewById(R.id.progressBar);
        registerNow_TextView = findViewById(R.id.registerNow);
        loginGooglesignIn_Button = findViewById(R.id.loginGooglesignIn);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.id.)).requestEmail().build();
        gsc = GoogleApiClient.getClient(this, gso);
        mggoglesignInClient  = GoogleSignIn.getClient(this, gso);
//


        registerNow_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
                finish();
            }
        });

        button_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar_ProgressBar.setVisibility(View.VISIBLE);
                String email, password;
                email = String.valueOf(email_value_EditText.getText());
                password = String.valueOf(password_value_EditText.getText());
                progressBar_ProgressBar = findViewById(R.id.progressBar);

//                if we use a toString() => for null value it give Null Pointer Exception
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(Login.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(Login.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar_ProgressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(Login.this, "Login Successfull.",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Login.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        loginGooglesignIn_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BeginSignInRequest signInRequest = BeginSignInRequest.builder()
                        .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                                .setSupported(true)
                                // Your server's client ID, not your Android client ID.
                                .setServerClientId(getString(R.string.web_client_id))
                                // Only show accounts previously used to sign in.
                                .setFilterByAuthorizedAccounts(true)
                                .build())
                        .build();


                return;
            }
        });

    }

    @Override
    public  void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
//            do something
        }
    }
}