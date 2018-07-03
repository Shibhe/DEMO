package com.example.josephthedev.jsma_emp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.josephthedev.jsma_emp.Activity.DriverLocationActivity;
import com.example.josephthedev.jsma_emp.Activity.JobNotificationActivity;
import com.example.josephthedev.jsma_emp.Service.LoginService;

import java.sql.DriverPropertyInfo;

public class LoginActivity extends AppCompatActivity {

    Button login;
    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               validateUserLogin();
            }
        });
    }

    public void validateUserLogin(){
        username.setError(null);
        password.setError(null);

        if (username.length() == 0 && username.isFocused()) {
            username.setError("Username cannot be empty");
        }

        if (password.length() == 0 && username.isFocused()) {
            password.setError("Password cannot be empty");
        } else {
            LoginService task = new LoginService(LoginActivity.this);
            task.execute(username.getText().toString(), password.getText().toString());
        }
    }
}
