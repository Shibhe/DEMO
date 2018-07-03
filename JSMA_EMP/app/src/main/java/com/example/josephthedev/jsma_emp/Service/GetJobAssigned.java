package com.example.josephthedev.jsma_emp.Service;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.josephthedev.jsma_emp.Activity.DriverLocationActivity;
import com.example.josephthedev.jsma_emp.Activity.JobNotificationActivity;
import com.example.josephthedev.jsma_emp.Helper.CustomHttpClient;
import com.example.josephthedev.jsma_emp.Model.User;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class GetJobAssigned extends AsyncTask<String, String, JSONObject> {

    @SuppressLint("StaticFieldLeak")
    private Context context;
    private ProgressDialog progressDialog;
    JSONObject response = null;
    private User user = new User();
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "JSMA";
    public double latitude, longitude;

    public GetJobAssigned(Context context, double latitude, double longitude) {
        this.context = context;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("\tLoading Nearest Job...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        //progressDialog.setTitle("");
        progressDialog.show();
    }

    @Override
    protected JSONObject doInBackground(String... strings) {
        // TODO Auto-generated method stub
        // String urls = "http://localhost/JSMA/Login.php";
        String urls = "http://uncreditable-window.000webhostapp.com/JSMA/sendReqToDriver.php";
        progressDialog = new ProgressDialog(context);

       // ArrayList<NameValuePair> postParameters = new ArrayList<>();
       // postParameters.add(new BasicNameValuePair("Username", strings[0]));
       // postParameters.add(new BasicNameValuePair("Password", strings[1]));
        String res = null;

        try {
            response = CustomHttpClient.executeHttpGet(urls);
            //res = response;
           // res = res.substring(0);
            //res = res.substring(res.length() - 1);
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
        return response;
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        super.onPostExecute(result);
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        JSONObject jsonObject;
        //DatabaseReference driverLocation = FirebaseDatabase.getInstance().getReference().child("driversAvailable");
        int radius = 2;

        progressDialog.dismiss();

        try {
            JSONArray jobs = (JSONArray) result.get("clientJobs");

            for (int i = 0; i < result.length(); i++){

                jsonObject = (JSONObject) jobs.get(i);

                double lat = Double.parseDouble(jsonObject.getString("Latitude"));
                double longi = Double.parseDouble(jsonObject.getString("Longitude"));


                double distBetween = distanceBetween(new LatLng(lat, longi), new LatLng(latitude, longitude)) / 1000;

                if (distBetween <= 3000){
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    //navigate to Main Menu
                    progressDialog.dismiss();
                    Intent intent = new Intent(context, JobNotificationActivity.class);
                    user.setFirstName(jsonObject.getString("FirstName"));
                    user.setLastName(jsonObject.getString("LastName"));
                    //user.setUsername(jsonObject.getString("Username"));
                    //user.setRole(jsonObject.getString("Role_Name"));  50000

                    editor.putString("latitude1", String.valueOf(lat));
                    editor.putString("FirstName", user.getFirstName());
                    editor.putString("LastName", user.getLastName());
                    editor.putString("Latitude", jsonObject.getString("Latitude"));
                    editor.putString("Longitude", jsonObject.getString("Longitude"));
                    editor.putString("Location_Name", jsonObject.getString("Location_Name"));
                    editor.putString("PhoneNumber", jsonObject.getString("PhoneNumber"));
                    editor.putString("Job_Type", jsonObject.getString("Job_Type"));
                    editor.putString("Description", jsonObject.getString("Description"));
                    editor.putString("Required_Date", jsonObject.getString("Required_Date"));
                    editor.putString("longitude1",  String.valueOf(longi));

                    editor.commit();
                    context.startActivity(intent);
                    context.stopService(intent);
                  // break;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static Double distanceBetween(LatLng StartP, LatLng EndP) {
      /*  if (point1 == null || point2 == null) {
            return null;
        }
        double vw = SphericalUtil.computeDistanceBetween(point1, point2);

        return vw;*/
       int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));

        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }
}
