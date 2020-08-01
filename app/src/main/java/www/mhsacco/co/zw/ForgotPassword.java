package www.mhsacco.co.zw;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Requests.RequestHandler;
import SharedPreferences.SharedPrefManager;
import constants.Constants;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {


    private EditText edpaynumber, editTextEmail, edcellphone;
    private Button buttonSubmit;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        //  if(SharedPrefManager.getInstance(this).isLoggedIn()){
        //    finish();
        //    startActivity(new Intent(this, ProfileActivity.class));
        //   return;
        // }

        editTextEmail = (EditText) findViewById(R.id.email);
        edpaynumber = (EditText) findViewById(R.id.paynumber);
        edcellphone = (EditText) findViewById(R.id.cellphone);

        buttonSubmit = (Button) findViewById(R.id.submit);

        progressDialog = new ProgressDialog(this);

        buttonSubmit.setOnClickListener(this);


    }
// registering user
    private void registerUser() {
        final String email = editTextEmail.getText().toString().trim();
        final String paynumber = edpaynumber.getText().toString().trim();
        final String cellphone = edcellphone.getText().toString().trim();

        if (paynumber.isEmpty()) {
            edpaynumber.setError("paynumber is required");
            edpaynumber.requestFocus();
            return;
        }
        if (paynumber.length() < 5) {

            edpaynumber.setError("minimum lenght of paynumber is 5");
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Enter a valid email Please");
            editTextEmail.requestFocus();
            return;
        }


        if (cellphone.isEmpty()) {
            edcellphone.setError("cellphoneis required");
            edcellphone.requestFocus();
            return;
        }
        if (cellphone.length() != 10) {
            edcellphone.setError("please enter a valid phone number");
            edcellphone.requestFocus();
            return;

        }
        progressDialog.setMessage("Sending Request...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_FORGOTPASSWORD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject obj = new JSONObject(response);


                            if(obj.getBoolean("error")){
                                progressDialog.dismiss();
                                Toast.makeText(
                                        getApplicationContext(),
                                        obj.getString("message"),
                                        Toast.LENGTH_LONG
                                ).show();

                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPassword.this);
                                builder.setMessage("thank you for your submission MHSACCO WILL CONTACT YOU SHORTLY VIA YOUR email,Text or call")
                                        .setCancelable(false)
                                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });

                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();

                                progressDialog.dismiss();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        Toast.makeText(ForgotPassword.this, "check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("paynumber", paynumber);
                params.put("email", email);
                params.put("cellphone", cellphone);

                return params;
            }
        };


        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);


    }

    // check if user has a password reset pending

    private  void checkPendingPasswordReset(){
        edpaynumber =(EditText) findViewById(R.id.paynumber);
        final String paynumber = edpaynumber.getText().toString().trim();

        progressDialog.setMessage("initiating........");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_CHECKPENDINGPASSWORDRESET,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if(!jsonObject.getBoolean("error")){

                                AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPassword.this);
                                builder.setMessage("Kindly note that you have a pending password reset ... Kindly contact Mhsacco for a quick response")
                                        .setCancelable(false)
                                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });

                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();

                            }else{


                              registerUser();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(ForgotPassword.this, "check connection", Toast.LENGTH_SHORT).show();



                    }
                }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("paynumber", paynumber);
                return params;
            }
        };


        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }

    @Override
    public void onClick(View view) {
        if (view == buttonSubmit) {
            checkPendingPasswordReset();
            registerUser();

        }

    }

    public void onBackPressed() {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}

