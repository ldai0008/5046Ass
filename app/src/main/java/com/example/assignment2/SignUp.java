package com.example.assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.assignment2.databinding.ActivitySignUpBinding;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private EditText name, email, password, confirmPassword;
    private Button register, login;
    private boolean nameValid, emailValid, passwordValid, confirmPassValid;
    private TextInputLayout nameError, emailError, passwordError, confirmPassError;
    private FirebaseAuth authentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        authentication = FirebaseAuth.getInstance();
        name = binding.etName;
        email = binding.etEmail;
        password = binding.etPassword;
        confirmPassword = binding.etConfirmpassword;
        register = binding.btnRegister;
        login = binding.bLoginbutton;
        nameError = binding.etNameError;
        emailError = binding.etEmailError;
        passwordError = binding.etPasswordError;
        confirmPassError = binding.etConfirmpasswordError;
        // execute the register button
        register.setOnClickListener(v -> SetValid());
        // execute the login button,go back to the log in page
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this, LogIn.class));
                finish();
            }
        });

    }

    public void SetValid() {
        // check the name
        if (name.getText().toString().isEmpty()) {
            nameError.setError("Please enter your name!");
            nameValid = false;
        } else {
            nameValid = true;
            nameError.setErrorEnabled(false);
        }

        // check the email
        if (email.getText().toString().isEmpty()) {
            emailError.setError(getResources().getString(R.string.email_error));
            emailValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            emailError.setError("Email address is invalid!");
            emailValid = false;
        } else {
            emailValid = true;
            emailError.setErrorEnabled(false);
        }

        // check the password
        if (password.getText().toString().isEmpty()) {
            passwordError.setError(getResources().getString(R.string.password_error));
            passwordValid = false;
        } else if (password.getText().length() < 6) {
            passwordError.setError("Enter at least 6 char!");
            passwordValid = false;
        } else {
            passwordValid = true;
            passwordError.setErrorEnabled(false);
        }

        // check the confirm password
        if (confirmPassword.getText().toString().isEmpty()) {
            confirmPassError.setError("Please confirm your password!");
            confirmPassValid = false;
        } else if (!confirmPassword.getText().toString().equals(password.getText().toString())) {
            confirmPassword.setError("Not equal, please try again!");
            confirmPassValid = false;
        } else {
            confirmPassValid = true;
            confirmPassError.setErrorEnabled(false);
        }
        // finish checking, then start activity
        if (nameValid && emailValid && passwordValid && confirmPassValid) {
            authentication.createUserWithEmailAndPassword(email.getText().toString(),
                    password.getText().toString())
                    .addOnCompleteListener(SignUp.this, task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUp.this, "Successful!", Toast.LENGTH_SHORT).show();
//                            startActivity(Intent.parseIntent(SignUp.this, MainActivity.class));
                        } else {
                            Toast.makeText(SignUp.this, "Failed, try again!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

}