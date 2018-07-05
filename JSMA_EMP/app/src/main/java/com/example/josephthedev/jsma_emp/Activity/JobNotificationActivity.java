package com.example.josephthedev.jsma_emp.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.josephthedev.jsma_emp.Model.User;
import com.example.josephthedev.jsma_emp.R;
import com.example.josephthedev.jsma_emp.Service.DoneJobService;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.example.josephthedev.jsma_emp.Service.LoginService.MyPREFERENCES;

public class JobNotificationActivity extends AppCompatActivity {

    private Button accept;
    private Intent intent;
    private TextView time, location;
    private SharedPreferences sharedPreferences;
    Ringtone r = null;
    Uri notification = null;
    String origin = null;
    String destination = null;

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_notification);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        accept  = findViewById(R.id.accept);
        time = findViewById(R.id.time);
        location = findViewById(R.id.location);

        String name = sharedPreferences.getString("FirstName", null) + " " + sharedPreferences.getString("LastName", null);
        String pNumber = sharedPreferences.getString("PhoneNumber", null);
        final String User_ID = sharedPreferences.getString("User_ID", null);
        final String Job_ID = sharedPreferences.getString("Job_ID", null);
        final String dur = "10";
        final String status = "Not done";

        if (sharedPreferences != null) {
            try {
                 notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                 r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                 r.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        time.setText(name + ", " + pNumber);
        location.setText(sharedPreferences.getString("Location_Name", null) + System.lineSeparator() + sharedPreferences.getString("Required_Date", null));

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r.stop();

                DoneJobService doneJobService = new DoneJobService(JobNotificationActivity.this);
                doneJobService.execute(Job_ID,User_ID, dur, status);
            }
        });
    }
}
