package profile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Requests.RequestHandler;
import SharedPreferences.SharedPrefManager;
import constants.Constants;
import www.mhsacco.co.zw.LoginActivity;
import www.mhsacco.co.zw.R;
import www.mhsacco.co.zw.RecyclerActivity;
import www.mhsacco.co.zw.confidentials;
import www.mhsacco.co.zw.detailapplicant;
import www.mhsacco.co.zw.variousitems;

public class profile extends AppCompatActivity implements View.OnClickListener {

    private List<ListItemPro> listing;
    TextView personalinfobtn, txtstatemetbtn, txttermsbtn;
    private Spinner spbankname,spaccountype;
    private TextView txttitle,txtapplicantname,txtgender,txtdob,txtnationalid,txtmobilenumber,txtretrievepay;
    private TextView txtemployer_position,txtdateofemployment,txtpaynumber,txtsurbub,txtphone;
     private TextView txtbankname,txtbranchname,txtbranchcode,txtaccountname,txtaccountnumber,txtaccounttype;
    private ProgressDialog progressDialog;

    private ImageView imgback;

    // private TextView textViewLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //if(!SharedPrefManager.getInstance(this).isLoggedIn()) {
          //  finish();
            //startActivity(new Intent(this, LoginActivity.class));
       // }
        //detail applicant referenmces
        txttitle = findViewById(R.id.title_applicantname);
        txtgender = findViewById(R.id.gender_dob);
        txtnationalid = findViewById(R.id.nationalid_mobilenumber);
        txtretrievepay = findViewById(R.id.payretrieve);
        progressDialog = new ProgressDialog(this);


        // banking detail references
        txtbankname = findViewById(R.id.idbankbranch);
        txtbranchcode = findViewById(R.id.idbranchcodeaccountname);
        txtbankname = findViewById(R.id.idbankbranch);
        txtaccountnumber = findViewById(R.id.idaccountnumber);

        // employment detail reference
        txtemployer_position = findViewById(R.id.idemployer_position);
        txtdateofemployment = findViewById(R.id.iddatejoined_employment);
        txtphone = findViewById(R.id.idsuburb_phone);
        txtsurbub = findViewById(R.id.suburb);

        //buttons  textviews
        imgback = findViewById(R.id.back);

        txtstatemetbtn = findViewById(R.id.idstatementbtn);
        txttermsbtn = findViewById(R.id.idterms);
        txtstatemetbtn.setOnClickListener(this);
        txttermsbtn.setOnClickListener(this);

        imgback.setOnClickListener(this);

        getdetailapplicantProfile();
        getbankingdetailProfile();
        getemploymentProfile();
    }
public void display(){
}

//retrieve banking details
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
                    Intent myintent = new Intent(profile.this,LoginActivity.class);
                    startActivity(myintent);
                    dialog.cancel();
                    finish();

                }
            });
    AlertDialog alertDialo = builde.create();
    alertDialo.show();



}
    public void getdetailapplicantProfile() {

        //final  String paynumber ="123456";
        final String paynumber = (SharedPrefManager.getInstance(this).getpaynumber());
       // String url ="http://192.168.1.4/tanaka/android/output/profile.php";

        progressDialog.setMessage("initializing...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();


                        try {

                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject o = array.getJSONObject(i);
                                // retrieving data from db

                                final String title = o.getString("title");
                               final String applicantname= o.getString("applicantname");
                                final String gender = o.getString("gender");
                                final String dob = o.getString("dob");
                                final String nationalid = o.getString("nationalid");
                                final String mobilenumber= o.getString("mobilenumber");
                                final String retrievepay= o.getString("paynumber");

                                String title_applicantname = title +"   "+ applicantname;
                                String gender_dob =  gender +"       "+ dob;
                                String nationalid_mobilenumber = nationalid +"    "+""+"cell:     "+mobilenumber;


                                //set vales to txt
                                txttitle.setText(title_applicantname) ;
                                txtgender.setText(gender_dob);
                                txtnationalid.setText(nationalid_mobilenumber);
                                txtretrievepay.setText(retrievepay);

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
                        Toast.makeText(profile.this, "check your internet connection", Toast.LENGTH_SHORT).show();

                    }

                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("paynumber",paynumber);
                return params;
            }
        };


        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);


    }
    public void getbankingdetailProfile() {

        //final  String paynumber ="123456";
        final String paynumber = (SharedPrefManager.getInstance(this).getpaynumber());
       // String url ="http://192.168.1.4/tanaka/android/output/profilebankingdetails.php";

        progressDialog.setMessage("initializing...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_GETBANKINGDETAILPROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();


                        try {

                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject o = array.getJSONObject(i);
                                // retrieving data from db

                                final String bankname = o.getString("bankname");
                                final String branchname= o.getString("branchname");
                                final String branchcode = o.getString("branchcode");
                                final String accountname= o.getString("accountname");
                                final String accountnumber = o.getString("accountnumber");


                                String bankname_branchname = bankname+"    "+ branchname;
                                String branchcode_accountname = branchcode  +"    "+accountname;
                              //  String accountnumber= accountnumber;


                                //set vales to txt
                                txtbankname.setText(bankname_branchname) ;
                                txtbranchcode.setText(branchcode_accountname);
                                txtaccountnumber.setText(accountnumber);


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
                        Toast.makeText(profile.this, "check your internet connection", Toast.LENGTH_SHORT).show();

                    }

                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("paynumber",paynumber);
                return params;
            }
        };


        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);


    }
    private  void getemploymentProfile(){
       // final  String paynumber ="123456";
        final String paynumber = (SharedPrefManager.getInstance(this).getpaynumber());
      //  String url ="http://192.168.1.4/tanaka/android/output/profileEmploymentdetails.php";

        progressDialog.setMessage("initializing...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_PROFILEEMPLOYMENTDETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();


                        try {

                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject o = array.getJSONObject(i);
                                // retrieving data from db
                                final String employer = o.getString("employer");
                                final String position= o.getString("position");
                                final String dateofemployment = o.getString("dateofemployment");
                                final String suburb= o.getString("suburb");
                                final String phone= o.getString("phone");


                                String employer_position = employer +"       "+ position;

                                //set vales to txt
                                txtemployer_position.setText(employer_position) ;
                                txtdateofemployment.setText(dateofemployment);
                                txtsurbub.setText(suburb);

                               // txtnationalid.setText(nationalid_mobilenumber);
                                txtphone.setText(phone);

                            }
                            //final String pay = (String) getTitle();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(profile.this, "check your internet connection", Toast.LENGTH_SHORT).show();

                        AlertDialog.Builder builder = new AlertDialog.Builder(profile.this);
                        builder.setMessage("network error")
                                .setCancelable(false)
                                .setPositiveButton("retry", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        getdetailapplicantProfile();
                                        getbankingdetailProfile();
                                        getemploymentProfile();

                                    }
                                })
                                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent myintent =new Intent(profile.this, variousitems.class);
                                        startActivity(myintent);
                                        finish();

                                    }
                                });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();



                    }

                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("paynumber",paynumber);
                return params;
            }
        };


        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.idstatementbtn:
                Intent myintent =new Intent(profile.this, RecyclerActivity.class);
                startActivity(myintent);
                finish();
                break;
            case R.id.idterms:
                terms();
                break;

            case R.id.back:
                Intent myinten =new Intent(profile.this, variousitems.class);
                startActivity(myinten);
                finish();
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
                        Intent myintent =new Intent(profile.this, variousitems.class);
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

