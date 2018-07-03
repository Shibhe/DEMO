package com.example.josephthedev.jsma_emp.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.josephthedev.jsma_emp.R;

public class RatingActivity extends AppCompatActivity {

    public RatingBar ratingBar;
    public TextView ratingText;
    public Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        ratingBar = findViewById(R.id.ratingBar);
        ratingText = findViewById(R.id.rate_me);
        submit = findViewById(R.id.submit);


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
                // Toast.makeText(getApplicationContext(),
                //    String.valueOf(ratingBar.getRating()), Toast.LENGTH_LONG).show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RatingActivity.this, DriverLocationActivity.class);
                startActivity(i);
            }
        });
    }
}
