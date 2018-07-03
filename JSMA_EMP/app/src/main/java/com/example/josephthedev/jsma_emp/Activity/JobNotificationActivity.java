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

import com.example.josephthedev.jsma_emp.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_notification);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        accept  = findViewById(R.id.accept);
        time = findViewById(R.id.time);
        location = findViewById(R.id.location);

        double lat = Double.parseDouble(sharedPreferences.getString("latitude1", null));
        double longi = Double.parseDouble(sharedPreferences.getString("longitude1", null));

        String name = sharedPreferences.getString("FirstName", null) + " " + sharedPreferences.getString("LastName", null);
        String pNumber = sharedPreferences.getString("PhoneNumber", null);

         origin = getAddressFromLocation(lat, longi, this);
         destination = sharedPreferences.getString("Location_Name", null);



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
        location.setText(sharedPreferences.getString("Location_Name", null));

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r.stop();
                intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/dir/?api=1&origin=" + origin + "&destination=" + destination + "&travelmode=driving"));
                startActivity(intent);
            }
        });
    }

    public static String getAddressFromLocation(final double latitude, final double longitude, final Context context) {

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String result = null;
        try {
            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    sb.append(address.getAddressLine(i)); //.append("\n");
                }

                sb.append(address.getFeatureName()).append(" ");
                sb.append(address.getThoroughfare()).append(", ");
                sb.append(address.getSubLocality()).append(", ");
                sb.append(address.getLocality()).append(" ");
                sb.append(address.getPostalCode()).append(" ");

                result = sb.toString();
            }
        } catch (IOException e) {
            Log.e("Location Address Loader", "Unable connect to Geocoder", e);
        }

        return result;

    }
}
