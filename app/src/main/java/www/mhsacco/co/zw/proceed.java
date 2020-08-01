package www.mhsacco.co.zw;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class proceed extends AppCompatActivity implements View.OnClickListener {

    private  EditText edpaynumber;
    private TextView txtermsandconditions;
    Button btproceed;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceed);

         edpaynumber = findViewById(R.id.paynumber);
        btproceed = findViewById(R.id.proceed);
        txtermsandconditions = findViewById(R.id.termsandconditions);
        btproceed.setOnClickListener(this);

        txtermsandconditions.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);


          if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, LoginActivity.class));

        }

    }

    private void termsandconditions(){
        AlertDialog.Builder builde = new AlertDialog.Builder(this);
        builde.setMessage("By using this application, I the undersigned declare that the forgoing particulars are true to the best of my knowledge and believe and agree to abide by the constitution and By-Laws of MHSACCO,the loan policy and other resolutions that shall be made in future in respect of this loan.\n" +
                "I also the understand that the basic rules applicable to this application are as listed and understand the loan will be granted only according to this rules.\n" +
                "a) This loan is payable over a period of 1 (one) month.\n" +
                "b) Repayment of this loan becomes due on my next salary (even on my salaries in arrears).\n" +
                "c)The maximum loan amount $150.00.\n" +
                "d)Interest is 15% per month.\n" +
                "e) Penalty is charged at the rate of $10.00 every month defaulted.\n" +
                "f) This loan shall be paid through the debit order system or the payroll system.\n" +
                "1.Current payslip.\n" +
                "2.copy of national ID/ Valid Driver`s License.\n" +
                "3. 3months bank statement / Bank statement bearing 3 last salaries. \n" +
                "I also acknowledge my indebtness to Municipality of Harare Savings and Credit Co-operative Society (MHSACCO) .I hereby authorize MHSAACO to deduct the required amount every month towards the repayment of this loan .Should MHSACCO fail to deduct their monthly installment through the debit order system, I authorize MHSACCO to recover their funds through City of Harare`s THRIFT SCHEME.I therefore authorize City of Harare to process any instruction received from MHSACCO seeks to recover their funds. In the event that I leave City of Harare employ, I authorize MHSACCO to recover their funds from my terminal benefits, by this document, City of Harare is duly instructed to deduct from my terminal benefits amount equivalent to my loan balance\n" +
                "                                Municipality of Harare Savings and Credit Co-operative Society Limited.\n")
                .setCancelable(false)
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                })
                .setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent myintent = new Intent(proceed.this,LoginActivity.class);
                        startActivity(myintent);
                        dialog.cancel();
                        finish();

                    }
                });
        AlertDialog alertDialo = builde.create();
        alertDialo.show();
    }



    private void register() {


        final String paynumber = edpaynumber.getText().toString().trim();

        progressDialog.setMessage("initializing...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_CHECKDETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(proceed.this, "Kindly press the first time button as it appears like you had never user the application before", Toast.LENGTH_SHORT).show();
                            finish();
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();

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

    //check if they is a pending loan and deny loan application

    private  void checkifuserhaspendingloan (){


        edpaynumber =(EditText) findViewById(R.id.paynumber);
        edpaynumber.setText(SharedPrefManager.getInstance(this).getpaynumber());
        final String paynumber = edpaynumber.getText().toString().trim();

        progressDialog.setMessage("initiating........");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_CHECKPENDINGLOAN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if(!jsonObject.getBoolean("error")){
                                AlertDialog.Builder builder = new AlertDialog.Builder(proceed.this);
                                builder.setMessage("Kindly note that you have a pending loan approval.You are not elible to apply for another loan ")

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
                                existingUser();

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
                        Toast.makeText(proceed.this, "check your network connection", Toast.LENGTH_SHORT).show();



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
    //check if user has applied before or not i.e in confidentials
    private  void existingUser(){


        edpaynumber =(EditText) findViewById(R.id.paynumber);
        final String paynumber = edpaynumber.getText().toString().trim();

        progressDialog.setMessage("initiating........");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_CHECKDETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if(!jsonObject.getBoolean("error")){

                                Intent myintent =new Intent(proceed.this,applyreasons .class);
                                startActivity(myintent);
                                finish();

                            }else{
                                //check if user has entered details in employment section
                                detailsapplicantcheck();


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
                        Toast.makeText(proceed.this, "check your network connection", Toast.LENGTH_SHORT).show();



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
    //check if user has entered details in the first level i.e details
    private  void detailsapplicantcheck(){
        edpaynumber =(EditText) findViewById(R.id.paynumber);
        final String paynumber = edpaynumber.getText().toString().trim();

        progressDialog.setMessage("initiating........");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_DETALSAPPLICANTCECK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if(!jsonObject.getBoolean("error")){

                                employmentdetailscheck();

                            }else{


                                Intent myintent =new Intent(proceed.this,detailapplicant.class);
                                startActivity(myintent);
                                finish();

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
                        Toast.makeText(proceed.this, "check connection", Toast.LENGTH_SHORT).show();



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


//check if user has entered details in employment section

    private  void employmentdetailscheck(){
        edpaynumber =(EditText) findViewById(R.id.paynumber);
        final String paynumber = edpaynumber.getText().toString().trim();

        progressDialog.setMessage("initiating........");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_EMPLOYMENTDETAILSCHECK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if(!jsonObject.getBoolean("error")){

                                bankingdetailscheck();

                            }else{

                                //  employmentdetailscheck();
                                Intent myintent =new Intent(proceed.this,employmentdetails.class);
                                startActivity(myintent);
                                finish();

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
                        Toast.makeText(proceed.this, "check connection", Toast.LENGTH_SHORT).show();



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


    private  void bankingdetailscheck(){


        edpaynumber =(EditText) findViewById(R.id.paynumber);
        final String paynumber = edpaynumber.getText().toString().trim();

        progressDialog.setMessage("initiating........");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_BANKINGDETAILCHECK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if(!jsonObject.getBoolean("error")){
                                Intent myintent =new Intent(proceed.this,confidentials.class);
                                startActivity(myintent);
                                finish();
                            }else{


                                Intent myintent =new Intent(proceed.this,bankingdetails.class);
                                startActivity(myintent);
                                finish();

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
                        Toast.makeText(proceed.this, "check ", Toast.LENGTH_SHORT).show();



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




    //check if user has already entered
    private  void finishapplydetailcheck(){


        edpaynumber =(EditText) findViewById(R.id.paynumber);
        final String paynumber = edpaynumber.getText().toString().trim();

        progressDialog.setMessage("initiating........");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_FINISHAPPLYDETAILCHECK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if(!jsonObject.getBoolean("error")){

                                Intent myintent =new Intent(proceed.this,confidentials.class);
                                startActivity(myintent);
                                finish();

                            }else{
                                //check if user has entered banking details before
                                bankingdetailscheck();
                                //Intent myintent =new Intent(proceed.this,detailapplicant.class);
                                //startActivity(myintent);
                                //finish();

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
                        Toast.makeText(proceed.this, "check your network connection", Toast.LENGTH_SHORT).show();



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
    //bankingdetails check


    /////employment details check

//check account

    private  void accountcheck(){

        edpaynumber =(EditText) findViewById(R.id.paynumber);
        final String paynumber = edpaynumber.getText().toString().trim();

        progressDialog.setMessage("initiating........");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_ACCOUNTCHECK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if(!jsonObject.getBoolean("error")){

                                AlertDialog.Builder builder = new AlertDialog.Builder(proceed.this);
                                builder.setMessage("Kindly Check Statement to view your approved loans")
                                        .setCancelable(false)
                                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent myintent = new Intent(proceed.this,proceed.class);
                                                startActivity(myintent);

                                            }
                                        });

                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();


                            }else if(jsonObject.getBoolean("error")) {
                                declined();
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
                        Toast.makeText(proceed.this, "check your network connection", Toast.LENGTH_SHORT).show();



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


// check if user application denied

    public   void declined (){
        final String paynumber = (SharedPrefManager.getInstance(this).getpaynumber());

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_CHECKDECLINED,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if(!jsonObject.getBoolean("error")){

                                AlertDialog.Builder builder = new AlertDialog.Builder(proceed.this);
                                builder.setMessage("Kindly note that your application to MHSACCO has been disaproved due to impossibilities ")
                                        .setCancelable(false)
                                        .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();

                                            }
                                        });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();



                            }
                            else if(jsonObject.getBoolean("error")) {
                                loans();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //progressDialog.hide();
                        Toast.makeText(proceed.this, "check your network connection", Toast.LENGTH_SHORT).show();



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

    public void loans (){
        final String paynumber = (SharedPrefManager.getInstance(this).getpaynumber());

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_CHECKLOANS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if(!jsonObject.getBoolean("error")){

                                AlertDialog.Builder builder = new AlertDialog.Builder(proceed.this);
                                builder.setMessage("Kindly note that your application is in progress and will be notified shortly for approval ")
                                        .setCancelable(false)
                                        .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();

                                            }
                                        });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();



                            }
                            else if(jsonObject.getBoolean("error")) {
                                Toast.makeText(proceed.this, "You currently have no Applied Loans", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(proceed.this, "check your network connection", Toast.LENGTH_SHORT).show();



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
    public void onClick(View v) {
        switch (v.getId()){


            case R.id.proceed:
                checkifuserhaspendingloan();
                break;

            case R.id.termsandconditions:
                termsandconditions();
                break;

        }

    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //  Toast.makeText(detailapplicant.this,"click cancel to exit application",Toast.LENGTH_LONG).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent myintent = new Intent(proceed.this,variousitems.class);
                        startActivity(myintent);
                        finish();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();



                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}

