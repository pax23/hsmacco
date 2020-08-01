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

public class changepass extends AppCompatActivity implements View.OnClickListener {
    private EditText edoldpassword, ednewpassword, edconfirm;
    private Button changepass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepass);

        edoldpassword = findViewById(R.id.oldpassword);
        ednewpassword = findViewById(R.id.newpassword);
        edconfirm = findViewById(R.id.confirmpass);

        changepass = findViewById(R.id.change);
        changepass.setOnClickListener(this);


        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

    }

    public void changepass() {

        final String oldpassword = edoldpassword.getText().toString().trim();
        final String newpassword = ednewpassword.getText().toString().trim();
        final String confirm = edconfirm.getText().toString().trim();

        if (oldpassword.isEmpty()) {
            edoldpassword.requestFocus();
            edoldpassword.setError("field required");
            return;
        }
        if (newpassword.isEmpty()) {
            ednewpassword.requestFocus();
            ednewpassword.setError("field required");
            return;
        }
        if
        (newpassword.length() < 6 && confirm.length() < 6) {
            ednewpassword.requestFocus();
            ednewpassword.setError("password length should not be less than 6 characters");
            return;
        }
        if (confirm.isEmpty()) {
            edconfirm.requestFocus();
            edconfirm.setError("field required");
            return;
        }

        boolean correct = edconfirm.equals(ednewpassword);
        if (correct)
        {
            edconfirm.requestFocus();
            edconfirm.setError("password do not match");
        }



        final String paynumber = (SharedPrefManager.getInstance(this).getpaynumber());
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setMessage("initializing");
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_CHANGEPASS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if(!jsonObject.getBoolean("error")){  AlertDialog.Builder builder = new AlertDialog.Builder(changepass.this);
                                builder.setMessage("password reset has been successful ")

                                        .setCancelable(false)
                                        .setPositiveButton("exit", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(changepass.this,variousitems.class);
                                                startActivity(intent);
                                                finish();

                                            }
                                        });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();


                            }
                            else if(jsonObject.getBoolean("error")) {
                                Toast.makeText(changepass.this, "kindly verify the old password you have entered and try again", Toast.LENGTH_SHORT).show
                                        ();
                                edoldpassword.getText().clear();
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
                        Toast.makeText(changepass.this, "check your network connection", Toast.LENGTH_SHORT).show();



                    }
                }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("paynumber", paynumber);
                params.put("oldpassword", oldpassword);
                params.put("confirm", confirm);
                return params;
            }
        };


        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.change:
                changepass();
                break;


        }

    }
    @Override
    public void onBackPressed() {

                        Intent myintent =new Intent(changepass.this, variousitems.class);
                        startActivity(myintent);
                        finish();


    }

}
