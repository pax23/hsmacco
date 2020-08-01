package www.mhsacco.co.zw;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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
import profile.profile;

//Points to note methods in this activity have been migrated to proceed activity i.e they are no longer used here

public class variousitems extends AppCompatActivity implements View.OnClickListener {

    androidx.gridlayout.widget.GridLayout mainGrid;
    private DatePickerDialog.OnDateSetListener mDatelistener;
    private static final String TAG = "detailapplicant";

    private CardView cardapply ,cardprofile,cardcal,cardterms,cardcontact,cardabout,cardstatement,cardchangepass,cardforgotpassword;
    private EditText edpaynumber,ednationalid,edmobilenumber;
    private ProgressDialog progressDialog;
    private TextView textViewUsername, text1;
    AlertDialog ad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_variousitems);

        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
           finish();
            startActivity(new Intent(this, LoginActivity.class));
       }



        edpaynumber = (EditText) findViewById(R.id.paynumber);
        edpaynumber.setText(SharedPrefManager.getInstance(this).getpaynumber());
        mainGrid = (androidx.gridlayout.widget.GridLayout) findViewById(R.id.mainGrid);
        cardcal =(CardView) findViewById(R.id.cal);
        cardapply = (CardView) findViewById(R.id.apply);
        cardcontact =(CardView) findViewById(R.id.contact);
        cardprofile = (CardView) findViewById(R.id.profile);
        cardterms = (CardView) findViewById(R.id.terms);
      //  cardabout = (CardView)findViewById(R.id.about);
        cardstatement = (CardView) findViewById(R.id.statement);
        cardchangepass = (CardView) findViewById(R.id.changepass);
        cardforgotpassword = (CardView) findViewById(R.id.logout);

        cardapply.setOnClickListener(this);
        cardprofile.setOnClickListener(this);
        cardcontact.setOnClickListener(this);
        cardcal.setOnClickListener(this);
         cardterms.setOnClickListener(this);
         //cardabout.setOnClickListener(this);
         cardstatement.setOnClickListener(this);
         cardchangepass.setOnClickListener(this);
         cardforgotpassword.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        //bthelpdesk.setOnClickListener(this);

        mainGrid = (androidx.gridlayout.widget.GridLayout) findViewById(R.id.mainGrid);

        //Set Event

        //setToggleEvent(mainGrid);

    }
    private void setToggleEvent(GridLayout mainGrid) {
        //Loop all child item of Main Grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            final CardView cardView = (CardView) mainGrid.getChildAt(i);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (cardView.getCardBackgroundColor().getDefaultColor() == -1) {
                        //Change background color
                        cardView.setCardBackgroundColor(Color.parseColor("#FF6F00"));
                        Toast.makeText(variousitems.this, "State : True", Toast.LENGTH_SHORT).show();

                    } else {
                        //Change background color
                        cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        Toast.makeText(variousitems.this, "State : False", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
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
                            Toast.makeText(variousitems.this, "Kindly press the first time button as it appears like you had never user the application before", Toast.LENGTH_SHORT).show();
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(variousitems.this);
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
                        Toast.makeText(variousitems.this, "check your network connection", Toast.LENGTH_SHORT).show();



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

                                Intent myintent =new Intent(variousitems.this,applyreasons .class);
                                startActivity(myintent);
                               // finish();

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
                        Toast.makeText(variousitems.this, "check your network connection", Toast.LENGTH_SHORT).show();



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


                                Intent myintent =new Intent(variousitems.this,detailapplicant.class);
                                startActivity(myintent);
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
                        Toast.makeText(variousitems.this, "check connection", Toast.LENGTH_SHORT).show();



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
                                Intent myintent =new Intent(variousitems.this,employmentdetails.class);
                                startActivity(myintent);
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
                        Toast.makeText(variousitems.this, "check connection", Toast.LENGTH_SHORT).show();



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
                                Intent myintent =new Intent(variousitems.this,confidentials.class);
                                 startActivity(myintent);
                                finish();
                            }else{


                                Intent myintent =new Intent(variousitems.this,bankingdetails.class);
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
                        Toast.makeText(variousitems.this, "check ", Toast.LENGTH_SHORT).show();



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

                                Intent myintent =new Intent(variousitems.this,confidentials.class);
                                startActivity(myintent);
                                finish();

                            }else{
                                //check if user has entered banking details before
                                bankingdetailscheck();
                                //Intent myintent =new Intent(variousitems.this,detailapplicant.class);
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
                        Toast.makeText(variousitems.this, "check your network connection", Toast.LENGTH_SHORT).show();



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

    // check profile
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

                                AlertDialog.Builder builder = new AlertDialog.Builder(variousitems.this);
                                builder.setMessage("Kindly Check Statement to view your approved loans")
                                        .setCancelable(false)
                                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent myintent = new Intent(variousitems.this,variousitems.class);
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
                        Toast.makeText(variousitems.this, "check your network connection", Toast.LENGTH_SHORT).show();



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

                                AlertDialog.Builder builder = new AlertDialog.Builder(variousitems.this);
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
                        Toast.makeText(variousitems.this, "check your network connection", Toast.LENGTH_SHORT).show();



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

                                AlertDialog.Builder builder = new AlertDialog.Builder(variousitems.this);
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
                                Toast.makeText(variousitems.this, "You currently have no Applied Loans", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(variousitems.this, "check your network connection", Toast.LENGTH_SHORT).show();



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



    //calendar
    private void autodate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                variousitems.this,
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
                //edpaybackperiod.setText(date);
            }
        };
    }
    //sending a notification
   /* private void notification(){
        String message="MHSACCO";
        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(variousitems.this)
                .setSmallIcon(R.drawable.ic_message_black_24dp)
                .setContentTitle("New Notification")
                .setContentText(message)
                .setAutoCancel(true);

      //  Intent intent = new Intent(variousitems.this, variousitems.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("message",message);

        PendingIntent pendingIntent = PendingIntent.getActivity(variousitems.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,builder.build());



    }



*/

   private  void terms(){


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
                       Intent myintent = new Intent(variousitems.this,LoginActivity.class);
                       startActivity(myintent);
                       dialog.cancel();
                       finish();

                   }
               });
       AlertDialog alertDialo = builde.create();
       alertDialo.show();



   }
    private void dialContactPhone(final String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }

    private  void about(){


        AlertDialog.Builder builde = new AlertDialog.Builder(this);
        builde.setMessage("Municipality Of Harare offers  wide range of services from Savings Loans")
                .setCancelable(false)
                .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                }) ;
        AlertDialog alertDialo = builde.create();
        alertDialo.show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
// activity to apply for the loan
            case R.id.apply:
               Intent myinnt = new Intent(variousitems.this,proceed.class);
                startActivity(myinnt);
                finish();
                break;

// check profile
     case R.id.profile:

         Intent myint = new Intent(variousitems.this, profile.class);
         startActivity(myint);
         finish();
         //former actions
         // accountcheck();
         break;

     case R.id.terms:
        terms();
         break;


            case R.id.cal:
         autodate();
         break;
            case R.id.contact:



             dialContactPhone("0775097764");
                break;

            //case R.id.about:
              //  about();
                //break;

            case R.id.statement:
                Intent myintent = new Intent(variousitems.this,RecyclerActivity.class);
                startActivity(myintent);
                finish();
                   break;

            case R.id.changepass:
                Intent myinten = new Intent(variousitems.this, changepass.class);
                startActivity(myinten);
                finish();
                break;

            case R.id.logout:
                //android.os.Process.killProcess(android.os.Process.myPid());
                //System.exit(1);
                Intent myintn = new Intent(variousitems.this,LoginActivity.class);
                startActivity(myintn);
                finish();
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
                        Intent myintent = new Intent(variousitems.this,LoginActivity.class);
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
