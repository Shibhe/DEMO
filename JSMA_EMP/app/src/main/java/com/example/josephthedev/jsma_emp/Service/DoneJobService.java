package com.example.josephthedev.jsma_emp.Service;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.josephthedev.jsma_emp.Activity.DriverLocationActivity;
import com.example.josephthedev.jsma_emp.Activity.RatingActivity;
import com.example.josephthedev.jsma_emp.Helper.CustomHttpClient;
import com.example.josephthedev.jsma_emp.Model.User;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DoneJobService extends AsyncTask<String, String, String> {

    @SuppressLint("StaticFieldLeak")
    private Context context;
    private ProgressDialog progressDialog;
    String response = null;
   // private User user = new User();
   // SharedPreferences sharedpreferences;
   // public static final String MyPREFERENCES = "JSMA" ;

    public DoneJobService(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("\tPlease wait...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        // TODO Auto-generated method stub
        String urls = "https://uncreditable-window.000webhostapp.com/JSMA/addJobAssigned.php";
        progressDialog = new ProgressDialog(context);

        ArrayList<NameValuePair> postParameters = new ArrayList<>();
        postParameters.add(new BasicNameValuePair("Job_ID", strings[0] ));
        postParameters.add(new BasicNameValuePair("Emp_ID", strings[1] ));
        postParameters.add(new BasicNameValuePair("Duration", strings[2] ));
        postParameters.add(new BasicNameValuePair("Job_Status", strings[3] ));

        String res = null;
        try {
            response = CustomHttpClient.executeHttpPost(urls, postParameters);
            res = response;
            res= res.replaceAll("\\s+","");
        }
        catch (Exception e) {
            //txt_Error.setText(e.toString());
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
        return res;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        JSONObject jsonObject;

        progressDialog.dismiss();

        try {

            jsonObject = new JSONObject(result);

            if(jsonObject.getInt("success") == 1){

                    //navigate to Main Menu
                    progressDialog.dismiss();
                    Intent i = new Intent(context, RatingActivity.class);

                    context.startActivity(i);
            }
            else{
                progressDialog.dismiss();
                Toast.makeText(context, "Oops!! something went wrong. Please try again later", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
