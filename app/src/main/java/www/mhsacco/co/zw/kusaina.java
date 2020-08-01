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

import java.util.HashMap;
import java.util.Map;

import Requests.RequestHandler;
import constants.Constants;

public class kusaina extends AppCompatActivity implements View.OnClickListener {


    private EditText edpaynumber, editTextEmail, edpassword,edpasswordconfirm, edconfirmm;
    private TextView txttermsandconditions,txtlogin;
    private Button buttonRegister;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kusaina);

        // if(SharedPrefManager.getInstance(this).isLoggedIn()){
        //   finish();
        // startActivity(new Intent(this, ProfileActivity.class));
        //return;
        // }

        editTextEmail = (EditText) findViewById(R.id.email);
        edpaynumber = (EditText) findViewById(R.id.paynumber);
        edpassword = (EditText) findViewById(R.id.password);
        edpasswordconfirm =(EditText) findViewById(R.id.passwordconfirm);//confirm password
        edconfirmm = (EditText) findViewById(R.id.confirmm);

        txttermsandconditions = (TextView) findViewById(R.id.termsandconditions);

        txtlogin = (TextView) findViewById(R.id.login);
        buttonRegister = (Button) findViewById(R.id.next);

        progressDialog = new ProgressDialog(this);

        buttonRegister.setOnClickListener(this);
        txtlogin.setOnClickListener(this);
        txttermsandconditions.setOnClickListener(this);
    }

    private void registerUser() {
        final String email = editTextEmail.getText().toString().trim();
        final String paynumber = edpaynumber.getText().toString().trim();
        final String password = edpassword.getText().toString().trim();
        final String passwordconfirm = edpasswordconfirm.getText().toString().trim();
        final String access = edconfirmm.getText().toString().trim();

        if (paynumber.isEmpty()) {
            edpaynumber.setError("paynumber is required");
            edpaynumber.requestFocus();
            return;
        }
        if (paynumber.length() < 5) {

            edpaynumber.setError("minimum length of paynumber is 5");
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


        if (password.isEmpty()) {
            edpassword.setError("password is required");
            edpassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            edpassword.setError("minimum lenght of password is 6");
            edpassword.requestFocus();
            return;

        }

        if(passwordconfirm.isEmpty()){
            edpasswordconfirm.setError("confirm your password");
            edpasswordconfirm.requestFocus();
            return;
        }
        if(password!=passwordconfirm){
            edpassword.setError("password do not match");
            edpasswordconfirm.setError("password do not match");
            return;
        }
        progressDialog.setMessage("sending request...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            edpaynumber.getText().clear();
                            edpassword.getText().clear();
                            editTextEmail.getText().clear();
                            Intent myintent = new Intent(kusaina.this,LoginActivity.class);
                            startActivity(myintent);
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
                        //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        Toast.makeText(kusaina.this, "check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("paynumber", paynumber);
                params.put("email", email);
                params.put("password", password);
                params.put("access", access);
                return params;
            }
        };


        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);


    }

    @Override
    public void onClick(View view) {
        if (view == buttonRegister) {
            registerUser();
        }
        if (view == txtlogin) {
            Intent myintent = new Intent(kusaina.this, LoginActivity.class);
            startActivity(myintent);
        }
        if (view == txttermsandconditions) {

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
                            dialog.dismiss();

                        }
                    })
                    .setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            moveTaskToBack(true);
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);

                        }
                    });
            AlertDialog alertDialo = builde.create();
            alertDialo.show();

        }



    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(kusaina.this, LoginActivity.class));
        finish();
    }
}

