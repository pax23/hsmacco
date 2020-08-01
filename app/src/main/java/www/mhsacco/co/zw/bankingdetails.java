package www.mhsacco.co.zw;

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
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Requests.RequestHandler;
import SharedPreferences.SharedPrefManager;
import constants.Constants;

public class bankingdetails extends AppCompatActivity implements View.OnClickListener {

     private Spinner spbankname,spaccountype;
     private TextView txtbankname,txtaccounttype;
    private EditText edbranchname, edbranchcode,edaccountname,edaccountnumber,edaccounttype,edpaynumber;
    private Button buttonnexti;
    private ProgressDialog progressDialog;

   // private TextView textViewLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bankingdetails);


        if(!SharedPrefManager.getInstance(this).isLoggedIn()) {
           finish();
           startActivity(new Intent(this, LoginActivity.class));
        }

        spbankname = (Spinner) findViewById(R.id.selectbank);
       // txtbankname = (TextView) findViewById(R.id.tbankname);
        edbranchname = (EditText) findViewById(R.id.branchname);
        edbranchcode = (EditText) findViewById(R.id.branchcode);
        edaccountname = (EditText) findViewById(R.id.accountname);
        edaccountnumber = (EditText) findViewById(R.id.accountnumber);
        edpaynumber = (EditText) findViewById(R.id.paynumber);
        spaccountype = (Spinner) findViewById(R.id.accounttype);
        //txtaccounttype = (TextView) findViewById(R.id.taccounttype);

        edpaynumber.setText(SharedPrefManager.getInstance(this).getpaynumber());
        buttonnexti = (Button) findViewById(R.id.next);
        buttonnexti.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);


        List<String> categories = new ArrayList<>();
        categories.add(0,"select bankname");
        categories.add("cabs");
        categories.add("standard chartered");
        categories.add("cbz");
        categories.add("steward bank");
        categories.add("fbc");

        //initializing adapter class of bank

        ArrayAdapter<String> spinnerArrayAdapt = new ArrayAdapter<String>(
                this,R.layout.spinner_item,categories
        );
        spinnerArrayAdapt.setDropDownViewResource(R.layout.spinner_item);
        spbankname.setAdapter(spinnerArrayAdapt);


        List<String> accounttype = new ArrayList<>();
        accounttype.add(0,"select accounttype");
        accounttype.add("savings");


        ArrayAdapter<String> spinnerArrayAdapte = new ArrayAdapter<String>(
                this,R.layout.spinner_item,accounttype
        );
        spinnerArrayAdapte.setDropDownViewResource(R.layout.spinner_item);
        spaccountype.setAdapter(spinnerArrayAdapte);

        spaccountype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("gender"))
                {


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spbankname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals(" bankname"))
                {


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }




    private void registerUser() {
        final String branchname = edbranchname.getText().toString().trim();
        final String branchcode = edbranchcode.getText().toString().trim();
        final String accountname = edaccountname.getText().toString().trim();
        final String accountnumber = edaccountnumber.getText().toString().trim();
        final String bankname = spbankname.getSelectedItem().toString();
        final String accounttype = spaccountype.getSelectedItem().toString();
        final String paynumber = edpaynumber.getText().toString().trim();


        if (bankname == "select bankname" ){
            TextView sperror = (TextView)spbankname.getSelectedView();
            sperror.setError("enter bankname");
            sperror.setText("Select bankname");
            sperror.setTextColor(Color.RED);
            AlertDialog.Builder builder = new AlertDialog.Builder(bankingdetails.this);
            builder.setMessage("Please kindly Select bankname at the top")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            progressDialog.dismiss();

            return;


        }
        if (branchname.isEmpty()) {
            edbranchname.setError("branchname is required");
            edbranchname.requestFocus();
            return;
        }
        if(branchname.length()<5){
            edbranchname.setError("enter branchname");
            edbranchname.requestFocus();
            return;
        }
        if (branchcode.isEmpty()) {
            edbranchcode.setError("branchcode is required");
            edbranchcode.requestFocus();
            return;
        }
        if(branchcode.length()<4){
            edbranchcode.setError("enter valid branchcode");
            edbranchcode.requestFocus();
            return;
        }

        if (accountname.isEmpty()) {
            edaccountname.setError("accountname is required");
            edaccountname.requestFocus();
            return;
        }
        if(accountname.length()<6){
            edaccountname.setError("enter valid accountname");
            edaccountname.requestFocus();
            return;
        }
        if (accountnumber.isEmpty()) {
            edaccountnumber.setError("accountnumber is required");
            edaccountnumber.requestFocus();
            return;
        }
        if(accountnumber.length()<6){
            edaccountnumber.setError(" enter valid accountnumber");
            edaccountnumber.requestFocus();
            return;
        }

        if(accounttype== "select accounttype") {
            TextView gendererror = (TextView) spaccountype.getSelectedView();
            gendererror.setError("enter accounttype");
            gendererror.setText("Select accounttype");
            gendererror.setTextColor(Color.RED);

            AlertDialog.Builder builder = new AlertDialog.Builder(bankingdetails.this);
            builder.setMessage("Please kindly Select account type at the bottom")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            progressDialog.dismiss();

            return;
        }




        if (accounttype.isEmpty()) {
            edaccounttype.setError("accounttype is required");
            edaccounttype.requestFocus();
            return;
        }


        progressDialog.setMessage("initializing...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_BANKING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(!jsonObject.getBoolean("error")) {
                                Intent myIntent = new Intent(bankingdetails.this, confidentials.class);
                                startActivity(myIntent);
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
                        Toast.makeText(bankingdetails.this, "check your internet connection", Toast.LENGTH_SHORT).show();

                    }

                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("bankname", bankname);
                params.put("branchname", branchname);
                params.put("branchcode", branchcode);
                params.put("accountname", accountname);
                params.put("accountnumber", accountnumber);
                params.put("accounttype", accounttype);
                params.put("paynumber", paynumber);
                return params;
            }
        };


        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next:
                registerUser();
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
                        Intent myintent =new Intent(bankingdetails.this, variousitems.class);
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

