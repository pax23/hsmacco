package www.mhsacco.co.zw;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import SharedPreferences.SharedPrefManager;
import constants.Constants;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextpaynumber, editTextPassword;
    private Button buttonLogin;
    //private Button buttonSignup;
    private ProgressDialog progressDialog;
    private TextView txtforgotpassword,txtsignup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextpaynumber = (EditText) findViewById(R.id.paynumber);
        editTextPassword = (EditText) findViewById(R.id.password);

        buttonLogin = (Button) findViewById(R.id.buttonLogin);

        txtsignup = (TextView) findViewById(R.id.buttonSignup);
        txtforgotpassword =(TextView) findViewById(R.id.forgotpassword);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        buttonLogin.setOnClickListener(this);
        txtsignup.setOnClickListener(this);
        txtforgotpassword.setOnClickListener(this);
    }

    // login details

    private void userLogin(){
        final String paynumber = editTextpaynumber.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        if(paynumber.isEmpty()){
            editTextpaynumber.setError("paynumber is required");
            editTextpaynumber.requestFocus();
            return;
        }
        if(paynumber.length()< 5){

            editTextpaynumber.setError("minimum length of paynumber is 5");
            return;
        }

        if (paynumber.isEmpty()) {
            editTextpaynumber.setError("paynumber is required");
            editTextpaynumber.requestFocus();
            return;
        }


        if (password.isEmpty()) {
            editTextPassword.setError("password is required");
            editTextPassword.requestFocus();
            return;
        }
        if (password.length()<6) {
            editTextPassword.setError("minimum length of password is 6");
            editTextPassword.requestFocus();
            return;

        }


        progressDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            ////check if they is a pending loan and deny loan application
                            if(!obj.getBoolean("error")){
                                SharedPrefManager.getInstance(getApplicationContext())
                                        .userLogin(
                                                obj.getInt("id"),
                                                obj.getString("paynumber"),
                                                obj.getString("email"),
                                                obj.getString("confirmm")
                                        );
                              //  editTextpaynumber.setText( obj.getString("email"));
                               startActivity(new Intent(getApplicationContext(), variousitems.class));
                                finish();
                            }else{
                                Toast.makeText(
                                        getApplicationContext(),
                                        obj.getString("message"),
                                        Toast.LENGTH_LONG
                                ).show();
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
                        Toast.makeText(LoginActivity.this, "check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("paynumber", paynumber);
                params.put("password", password);
                return params;
            }

        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onClick(View view) {
        if(view == buttonLogin){

            userLogin();
            //finish();
        }
        if(view == txtsignup){
            startActivity(new Intent(this, kusaina.class));
            finish();
        }
        if(view == txtforgotpassword){
            startActivity(new Intent(this, ForgotPassword.class));
            finish();
        }
    }
    @Override
    public void onBackPressed() {
      //  moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);

    }
}
