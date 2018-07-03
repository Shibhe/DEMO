package com.example.josephthedev.jsma_emp.Service;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.josephthedev.jsma_emp.Activity.DriverLocationActivity;
import com.example.josephthedev.jsma_emp.Helper.CustomHttpClient;
import com.example.josephthedev.jsma_emp.Model.User;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginService extends AsyncTask<String, String, String> {

    @SuppressLint("StaticFieldLeak")
    private Context context;
    private ProgressDialog progressDialog;
    String response = null;
    private User user = new User();
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "JSMA" ;

    public LoginService(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("\tLoading...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Login status");
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        // TODO Auto-generated method stub
        // String urls = "http://localhost/JSMA/Login.php";
        String urls = "https://uncreditable-window.000webhostapp.com/JSMA/Login.php";
        progressDialog = new ProgressDialog(context);

        ArrayList<NameValuePair> postParameters = new ArrayList<>();
        postParameters.add(new BasicNameValuePair("Username", strings[0] ));
        postParameters.add(new BasicNameValuePair("Password", strings[1] ));
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
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        JSONObject jsonObject;

        progressDialog.dismiss();

        try {

            jsonObject = new JSONObject(result);

            if(jsonObject.getInt("success") == 1){

                if (jsonObject.getString("Role_Name").equals("Technician")){
                SharedPreferences.Editor editor = sharedpreferences.edit();
                //navigate to Main Menu
                progressDialog.dismiss();
                Intent i = new Intent(context, DriverLocationActivity.class);
                user.setUser_ID(jsonObject.getString("User_ID"));
                user.setFirstName(jsonObject.getString("FirstName"));
                user.setLastName(jsonObject.getString("LastName"));
                user.setUsername(jsonObject.getString("Username"));
                user.setRole(jsonObject.getString("Role_Name"));

                editor.putString("User_ID", user.getUser_ID());
                editor.putString("FirstName", user.getFirstName());
                editor.putString("LastName", user.getLastName());
                editor.putString("Username", user.getUsername());
                editor.putString("Role_Name", user.getRole());

                editor.commit();
                //i.("currentUser", jsonObject.toString());
                Toast.makeText(context, "Wecome " + user.getFirstName() + " " + user.getLastName(), Toast.LENGTH_LONG).show();
                context.startActivity(i);
            } else {
                    progressDialog.dismiss();
                    Toast.makeText(context, "Sorry! You're not registered to the system", Toast.LENGTH_LONG).show();
                }
            }
            else{
                progressDialog.dismiss();
                Toast.makeText(context, "Username or Password is incorrect", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
