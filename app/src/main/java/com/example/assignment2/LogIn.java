package com.example.assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.assignment2.databinding.ActivityLogInBinding;
import com.example.assignment2.roomdatabase.entity.PainRecord;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class LogIn extends AppCompatActivity {
    public static String email;
    public static PainRecord currPainRecord;

    private EditText userName, password;
    private Button login;
    private TextView register;
    private boolean EmailValid, PasswordValid;
    private TextInputLayout emailError, passwordError;
    private ActivityLogInBinding binding;
    private CheckBox rememberMe;
    private FirebaseAuth authentice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogInBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        // initial the variables
        authentice = FirebaseAuth.getInstance();
        userName = binding.etUserName;
        password = binding.etPassword;
        login = binding.bLogin;
        register = binding.bSignup;
        emailError = binding.etUsernameError;
        passwordError = binding.etPasswordError;
        rememberMe = binding.ckUsername;

        initData();
        // execute the login button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetValidValue();

            }
        });
        // execute the sigh up requirment
        register.setOnClickListener(v -> {
            Intent intent = new Intent(LogIn.this, SignUp.class);
            startActivity(intent);
        });

    }

    public void SetValidValue() {
        // check the email
        if (userName.getText().toString().isEmpty()) {
            emailError.setError(getResources().getString(R.string.email_error));
            EmailValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(userName.getText().toString()).matches()) {
            emailError.setError("Email address is invalid!");
            EmailValid = false;
        } else {
            EmailValid = true;
            emailError.setErrorEnabled(false);
        }

        // check the password
        if (password.getText().toString().isEmpty()) {
            passwordError.setError("Please enter your password!");
            PasswordValid = false;
        } else if (password.getText().length() < 6) {
            passwordError.setError("Enter at least 6 char!");
            PasswordValid = false;
        } else {
            PasswordValid = true;
            passwordError.setErrorEnabled(false);
        }
        if (EmailValid && PasswordValid) {
            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
            authentice.signInWithEmailAndPassword(userName.getText().toString(),
                    password.getText().toString())
                    .addOnCompleteListener(LogIn.this, task -> {
                        if (task.isSuccessful()) {
                            email = userName.getText().toString();
                            Intent intent = new Intent(LogIn.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LogIn.this, "failed!", Toast.LENGTH_SHORT).show();
                        }
                    });

        }

        if (rememberMe.isChecked()) {
            //Here you can write the codes if box is checked
            SharedPreferences sharedPreferences = getSharedPreferences("userInformation", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("userName", userName.getText().toString());
            editor.putString("password", password.getText().toString());
            editor.putBoolean("rememberMe", true);
            editor.commit();

        } else {
            //Here you can write the codes if box is not checked
            SharedPreferences sharedPreferences = getSharedPreferences("userInformation", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("userName", "");
            editor.putString("password", "");
            editor.putBoolean("rememberMe", false);
            editor.commit();

        }
    }

    // init the data
    private void initData() {
        SharedPreferences sharedPreferences = getSharedPreferences("userInformation", MODE_PRIVATE);
        boolean boolRememberMe = sharedPreferences.getBoolean("rememberMe", false);
        String strUserName = sharedPreferences.getString("userName", "");
        String strPassword = sharedPreferences.getString("password", "");
        if (boolRememberMe) {
            userName.setText(strUserName);
            password.setText(strPassword);
            rememberMe.setChecked(true);
        } else {
            userName.setText("");
            password.setText("");
            rememberMe.setChecked(false);
        }
    }

}