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
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Requests.RequestHandler;
import SharedPreferences.SharedPrefManager;
import constants.Constants;


public class detailapplicant extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "detailapplicant";

    private DatePickerDialog.OnDateSetListener mDatelistener;

    private ImageView imgbackbtn;
    //private TextInputLayout  tidatetime;

    private Spinner spinnertitle, spgender;

    private TextView sperror,dateofbirth;

    private EditText  edfirstname,edsurname, eddateofbirth, ednationalid, edmobilenumber, edemail, edpaynumber;

    private Button buttonnext;

    private ProgressDialog progressDialog;
    private Pattern pattern;
    private Matcher matcher;

    private static final String DATE_PATTERN =
            "(0?[1-9]|1[012]) [/.-] (0?[1-9]|[12][0-9]|3[01]) [/.-] ((19|20)\\d\\d)";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailapplicant);

        setSupportActionBar((Toolbar)findViewById(R.id.toofirststep));

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       // dateofbirth= (TextView) findViewById(R.id.dateofbirth);
       // tidatetime = findViewById(R.id.datetime);

        imgbackbtn = findViewById(R.id.imgbackbtn);


        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        eddateofbirth = findViewById(R.id.dateofbirth);


        edemail = (EditText) findViewById(R.id.email);
        edpaynumber = (EditText) findViewById(R.id.uniquepaynumber);

        edfirstname = (EditText) findViewById(R.id.firstname);
        edsurname = (EditText)findViewById(R.id.surname);

        edemail.setText(SharedPrefManager.getInstance(this).getUserEmail());
        edpaynumber.setText(SharedPrefManager.getInstance(this).getpaynumber());
        //  edapplicantname.setText(SharedPrefManager.getInstance(this).getKeyUserConfirm());



        // edgender = (EditText) findViewById(R.id.gender);

        ednationalid = (EditText) findViewById(R.id.nationalid);
        edmobilenumber = (EditText) findViewById(R.id.mobilenumber);

        spinnertitle = (Spinner) findViewById(R.id.sptitle);
        spgender = (Spinner) findViewById(R.id.gender);
        //sperror = (TextView) findViewById(R.id.spinnererror);

        buttonnext = (Button) findViewById(R.id.next);
        progressDialog = new ProgressDialog(this);

        //click listener


        imgbackbtn.setOnClickListener(this);
        buttonnext.setOnClickListener(this);
       // eddateofbirth.setOnClickListener(this);
       // tidatetime.setOnClickListener(this);

        // Initializing a String Array of title
        List<String> title = new ArrayList<>();
        title.add(0, "Select title");
        title.add("Mr");
        title.add("Ms");
        title.add("Mrs");
        title.add("Doctor");
        title.add("Professor");

        // Initializing an ArrayAdapter
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,title
        );
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnertitle.setAdapter(spinnerArrayAdapter);


        // Initializing a String Array of gender

        List<String> genders = new ArrayList<>();
        genders.add(0, "Select gender");
        genders.add("male");
        genders.add("female");


      // Initializing array adapter of gender
        ArrayAdapter<String> spinnerArrayAdapte = new ArrayAdapter<String>(
                this,R.layout.spinner_item,genders
        );
        spinnerArrayAdapte.setDropDownViewResource(R.layout.spinner_item);
        spgender.setAdapter(spinnerArrayAdapte);

        spgender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("gender")) {


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnertitle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals(" title")) {


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void autodate() {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        detailapplicant.this,
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
                eddateofbirth.setText(date);
            }
        };
    }



    public Boolean checkDateFormat(String date){
        if (date == null || !date.matches("^(1[0-9]|0[1-9]|3[0-1]|2[1-9])/(0[1-9]|1[0-2])/[0-9]{4}$"))
            return false;
        SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
        try {
            format.parse(date);
            return true;
        }catch (ParseException e){
            return false;
        }
    }



    private void register() {
        final String email = SharedPrefManager.getInstance(this).getUserEmail();
        final String userEmail = (SharedPrefManager.getInstance(this).getUserEmail());


       // final String title = edtitle.getText().toString().trim();
        final String firstname = edfirstname.getText().toString().trim();
        final String surname = edsurname.getText().toString().trim();
        final String gender = spgender.getSelectedItem().toString();
        final String dob = eddateofbirth.getText().toString().trim();
        final String nationalid = ednationalid.getText().toString().trim();
        final String mobilenumber = edmobilenumber.getText().toString().trim();
        final String title = spinnertitle.getSelectedItem().toString();
      //  final String displaydate =txtddisplaydate.setText();
        final String emaili = edemail.getText().toString().trim();
        final String paynumber = edpaynumber.getText().toString().trim();

        //concatenation of firrstname and surname to form one
        final  String applicant = firstname +" "+ surname;


        if (title == "Select title" ){


            AlertDialog.Builder builder = new AlertDialog.Builder(detailapplicant.this);
            builder.setMessage("Please kindly Select title at the top")
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
        if(gender == "Select gender"){
            AlertDialog.Builder builder = new AlertDialog.Builder(detailapplicant.this);
            builder.setMessage("Please kindly Select gender in the middle")
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

        if (firstname.isEmpty()) {
            edfirstname.setError("applicant name is required");
            edfirstname.requestFocus();
            return;
        }
        if (firstname.length()<4) {
            edfirstname.setError("enter a valid name");
            edfirstname.requestFocus();
            return;

        }
        if (surname.isEmpty()) {
            edsurname.setError("surname is required");
            edsurname.requestFocus();
            return;
        }
        if (surname.length()<4) {
            edsurname.setError("enter a valid surname");
            edsurname.requestFocus();
            return;

        }


        if (dob.isEmpty()) {
           eddateofbirth.setError("dob required");
            eddateofbirth.requestFocus();
            return;

        }

        if (dob.length()<6) {
            eddateofbirth.setError("enter valid date of birth");
            eddateofbirth.requestFocus();
            return;

        }



        if (nationalid.isEmpty()) {
            ednationalid.setError("nationalid required");
            ednationalid.requestFocus();
            return;

        }
        if (nationalid.length()<7) {
            ednationalid.setError("enter valid nationalid");
            ednationalid.requestFocus();
            return;

        }
        if (mobilenumber.isEmpty()) {
            edmobilenumber.setError("mobile number required");
            edmobilenumber.requestFocus();
            return;

        }

        if (mobilenumber.length()!=10) {
            edmobilenumber.setError("enter valid mobilenumber");
            edmobilenumber.requestFocus();
            return;

        }
        if(emaili.isEmpty()){
            edemail.setError("email is required");
            edemail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emaili).matches()) {
            edemail.setError("Enter a valid email Please");
            edemail.requestFocus();
            return;
        }
        progressDialog.setMessage("initializing...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_APPLICANT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Intent myIntent = new Intent(detailapplicant.this,employmentdetails.class);
                            startActivity(myIntent);finish();
                           // Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(detailapplicant.this, "check your internet connection", Toast.LENGTH_SHORT).show();


                    }
                }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("title",  title);
                params.put("applicantname",applicant);
                params.put("gender", gender);
                params.put("dob", dob);
                params.put("nationalid", nationalid);
                params.put("mobilenumber", mobilenumber);
                params.put("paynumber", paynumber);
                return params;
            }
        };


        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }

    //check if user has already updated details


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next:
                matcher = Pattern.compile(DATE_PATTERN).matcher((CharSequence) eddateofbirth);

//Birthday validator
     if (!matcher.matches()) {
                Toast.makeText(getApplicationContext(), "Invalid Birthday!", Toast.LENGTH_SHORT).show();
            }
                register();
                break;

            case R.id.datetime:
                autodate();
                break;

            case R.id.imgbackbtn:
                Intent myintent = new Intent(detailapplicant
                .this,variousitems.class);
                startActivity(myintent);
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
                        Intent myintent =new Intent(detailapplicant.this, variousitems.class);
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

