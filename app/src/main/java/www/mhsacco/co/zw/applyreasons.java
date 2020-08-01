package www.mhsacco.co.zw;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import Requests.RequestHandler;
import SharedPreferences.SharedPrefManager;
import constants.Constants;

public class applyreasons extends AppCompatActivity implements View.OnClickListener {
//this activity is the one which works if the user has already furnished his/her details


    private static final String TAG = "detailapplicant";
    private TextView txtddisplaydate;
    private DatePickerDialog.OnDateSetListener mDatelistener;

    private EditText edreason,edamount,edpaybackperiod,edpaynumber;
    Button btfinish;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finishapply);

        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        txtddisplaydate = (TextView) findViewById(R.id.datetime);
        edpaynumber = (EditText) findViewById(R.id.paynumber);
        edreason = (EditText) findViewById(R.id.reason);
        edamount = (EditText) findViewById(R.id.amount);
        edpaybackperiod = (EditText) findViewById(R.id.payback);
        btfinish =(Button) findViewById(R.id.finish);
        edpaynumber.setText(SharedPrefManager.getInstance(this).getpaynumber());
        progressDialog = new ProgressDialog(this);

        btfinish.setOnClickListener(this);
        //txtddisplaydate.setOnClickListener(this);
    }


    private void autodate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                applyreasons.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDatelistener,
                year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        mDatelistener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                edpaybackperiod.setText(date);
            }
        };
    }


    private void film(){
        final String reason = edreason.getText().toString().trim();
        final String amount = edamount.getText().toString().trim();
        final String paybackperiod = edpaybackperiod.getText().toString().trim();
        final String paynumber = edpaynumber.getText().toString().trim();


        if(reason.isEmpty()){
            edreason.setError("reason is required");
            edreason.requestFocus();
            return;
        }
        if (reason.length()<6) {
            edreason.setError("enter valid reason");
            edreason.requestFocus();
            return;

        }

        if (amount.isEmpty()) {
            edamount.setError("amount is required");
            edamount.requestFocus();
            return;
        }
        if (amount.length()<2) {
            edamount.setError("enter reasonable amount");
            edamount.requestFocus();
            return;

        }


        if (paybackperiod.isEmpty()) {
            edpaybackperiod.setError("paybackperiod is required");
            edpaybackperiod.requestFocus();
            return;
        }
        if (paybackperiod.length()<6) {
            edpaybackperiod.setError("enter valid payback period");
            edpaybackperiod.requestFocus();
            return;

        }

        progressDialog.setMessage("initializing...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_FINISH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        //Intent myIntent = new Intent(applyreasons.this, payslipupload.class);
                      //  startActivity(myIntent);
                      //  finish();


                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            AlertDialog.Builder builde = new AlertDialog.Builder(applyreasons.this);
                            builde.setMessage("Thank you for your loan application with MHSACCO LOANS.You will be notified shortly for your loan approval")
                                    .setCancelable(false)
                                    .setPositiveButton("MAIN MENU", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(applyreasons.this, variousitems.class));
                                            finish();

                                        }
                                    })
                                    .setNegativeButton("LOG OUT", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(applyreasons.this, LoginActivity.class));
                                            finish();

                                        }
                                    });
                            AlertDialog alertDialo = builde.create();
                            alertDialo.show();

                            //  Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(applyreasons.this, "check your internet connection", Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("reason", reason);
                params.put("amount", amount);
                params.put("paybackperiod", paybackperiod);
                params.put("paynumber", paynumber);
                return params;
            }
        };


        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);


    }




    @Override
    public void onClick(View v) {
        if(v==btfinish){
            film();
        }


        if(v==txtddisplaydate){
            autodate();
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
                        Intent myintent =new Intent(applyreasons.this, variousitems.class);
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
