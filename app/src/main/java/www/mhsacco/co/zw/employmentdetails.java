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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Requests.RequestHandler;
import SharedPreferences.SharedPrefManager;
import constants.Constants;

public class employmentdetails extends AppCompatActivity implements View.OnClickListener {


    private ImageView imgbackbutton;

    private Spinner semployer;
    private TextView temployer;
    private EditText edemployer, edposition, eddateofemployment,edpay,edsuburb,edphone;
    private Button buttonReg;
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employmentdetails);

        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }


        imgbackbutton = findViewById(R.id.imgbackbtn);

        edposition = (EditText) findViewById(R.id.position);
        //edemployer = (EditText) findViewById(R.id.sidemployer);
        eddateofemployment = (EditText) findViewById(R.id.datejoinedemployment);
        edpay = (EditText) findViewById(R.id.pay);
        edsuburb = (EditText) findViewById(R.id.suburb);
        edphone = (EditText) findViewById(R.id.phone);
        semployer = (Spinner) findViewById(R.id.sidemployer);
        //temployer = (TextView) findViewById(R.id.tidemployer);

        edpay.setText(SharedPrefManager.getInstance(this).getpaynumber());
       

        buttonReg = (Button) findViewById(R.id.nexty);
        progressDialog = new ProgressDialog(this);
        buttonReg.setOnClickListener(this);
        imgbackbutton.setOnClickListener(this);


        List<String> employer = new ArrayList<>();
        employer.add(0,"Select employer");
        employer.add("HMP");
        employer.add("IMPILO");
        employer.add("COH");
        employer.add("HMMAS");
        employer.add("MHSACCO");

        // Initializing array adapter of gemployer
        ArrayAdapter<String> spinnerArrayAdapte = new ArrayAdapter<String>(
                this,R.layout.spinner_item,employer
        );
        spinnerArrayAdapte.setDropDownViewResource(R.layout.spinner_item);
        semployer.setAdapter(spinnerArrayAdapte);

        semployer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        final String position = edposition.getText().toString().trim();
        final String employer = semployer.getSelectedItem().toString();
        final String dateofemployment = eddateofemployment.getText().toString().trim();
        final String paynumber = edpay.getText().toString().trim();
        final String suburb = edsuburb.getText().toString().trim();
        final String phone = edphone.getText().toString().trim();


        if (employer == "Select employer" ){
           // TextView temployer = (TextView)semployer.getSelectedView();
           /// temployer.setError("enter employer");
          //  temployer.setText("Select employer");
            //temployer.setTextColor(Color.RED);

            AlertDialog.Builder builder = new AlertDialog.Builder(employmentdetails.this);
            builder.setMessage("Please kindly Select employer at the top")
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
        if (position.isEmpty()) {
            edposition.setError("position is required");
            edposition.requestFocus();
            return;
        }




        if (dateofemployment.isEmpty()) {
            eddateofemployment.setError("date joined employment is required");
            eddateofemployment.requestFocus();
            return;
        }
        if (dateofemployment.length()<6) {
            eddateofemployment.setError("enter valid date joined employment");
            eddateofemployment.requestFocus();
            return;

        }
        if (paynumber.isEmpty()) {
            edpay.setError("paynumber required");
            edpay.requestFocus();
            return;
        }
        if (suburb.isEmpty()) {
            edsuburb.setError("enter suburb");
            edsuburb.requestFocus();
            return;
        }
        if (suburb.length()<6) {
            edsuburb.setError("enter valid suburb");
            edsuburb.requestFocus();
            return;

        }
        if (phone.isEmpty()) {
            edphone.setError("enter phone");
            edphone.requestFocus();
            return;
        }

        if (phone.length()<6) {
            edphone.setError("enter valid phone number");
            edphone.requestFocus();
            return;

        }



        progressDialog.setMessage("initializing...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_EMPLOY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                       progressDialog.dismiss();


                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Intent intent = new Intent(employmentdetails.this, bankingdetails.class);
                            startActivity(intent);
                           finish();
                            //startActivity(new Intent(this,employmentdetails.detailapplicant.class));

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
                        Toast.makeText(employmentdetails.this, "check your internet connection", Toast.LENGTH_SHORT).show();
                        ;
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("position", position);
                params.put("employer", employer);
                params.put("dateofemployment", dateofemployment);
                params.put("paynumber", paynumber);
                params.put("suburb", suburb);
                params.put("phone", phone);
                return params;
            }
        };


        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);


    }

    @Override
    public void onClick(View view) {
        if (view == buttonReg){
            registerUser();
        }




        if(view == imgbackbutton){ startActivity(new Intent(this, variousitems.class));
            finish();}

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
                        Intent myintent =new Intent(employmentdetails.this, variousitems.class);
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

