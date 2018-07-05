package com.example.josephthedev.jsma_emp.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.josephthedev.jsma_emp.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.example.josephthedev.jsma_emp.Service.LoginService.MyPREFERENCES;

public class RatingActivity extends AppCompatActivity {

    public RatingBar ratingBar;
    public TextView ratingText;
    public Button submit;
    public Intent intent;
    public String origin, destination;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        ratingBar = findViewById(R.id.ratingBar);
        ratingText = findViewById(R.id.rate_me);
        submit = findViewById(R.id.submit);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        double lat = Double.parseDouble(sharedPreferences.getString("latitude1", null));
        double longi = Double.parseDouble(sharedPreferences.getString("longitude1", null));

         origin = getAddressFromLocation(lat, longi, this);
         destination = sharedPreferences.getString("Location_Name", null);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (rating <= 2.0){
                    ratingText.setText("Poor");
                } else if (rating <= 4.0) {
                    ratingText.setText("Better");
                } else {
                    ratingText.setText("Excellent");
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open(v);
            }
        });
    }

    public void open(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);

       // LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
       // View layout = inflater.inflate(R.layout.phonenumber, null);
       // final EditText phoneBox = layout.findViewById(R.id.phoneNumber);


       // alertDialogBuilder.setView(layout);
        //alertDialogBuilder.setTitle("Confirmation");
        alertDialogBuilder.setMessage("Show Directions?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/dir/?api=1&origin=" + origin + "&destination=" + destination + "&travelmode=driving"));
                        startActivity(intent);
                    }
                });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                intent = new Intent(RatingActivity.this, DriverLocationActivity.class);
                startActivity(intent);
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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
