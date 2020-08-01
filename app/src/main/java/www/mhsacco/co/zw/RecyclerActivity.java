package www.mhsacco.co.zw;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Recyclerview.ListItem;
import Recyclerview.MyAdapter;
import Requests.RequestHandler;
import SharedPreferences.SharedPrefManager;
import constants.Constants;

public  class RecyclerActivity extends AppCompatActivity{



    private  RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItem> listItems;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /// check if user is logged in
        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        accountcheck();
    }
    //check if user has an accepted loan
    private  void accountcheck(){

        final String paynumber = (SharedPrefManager.getInstance(this).getpaynumber());
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("initializing........");
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

                            if (!jsonObject.getBoolean("error")) {

                                recyclerView = findViewById(R.id.recycler);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(new LinearLayoutManager(RecyclerActivity.this));

                                listItems = new ArrayList<>();

                                loadrecyclerviewdata();
                                adapter =new MyAdapter(listItems,RecyclerActivity.this);
                                recyclerView.setAdapter(adapter);

                            } else if (jsonObject.getBoolean("error")) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(RecyclerActivity.this);
                                builder.setMessage("Kindly note that you currently have no generated statement")
                                        .setCancelable(false)
                                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
        Intent myintent = new Intent(RecyclerActivity.this,variousitems.class);
        startActivity(myintent);
        finish();

                                            }
                                        });

                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();





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
                        Intent myin = new Intent(RecyclerActivity.this,variousitems.class);
                        startActivity(myin);
                        Toast.makeText(RecyclerActivity.this, "check your network connection", Toast.LENGTH_SHORT).show();
                        finish();





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
    //load recycerview
    private  void  loadrecyclerviewdata(){

        final String paynumber = (SharedPrefManager.getInstance(this).getpaynumber());

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading data");
        progressDialog.setCancelable(false);
        progressDialog.show();

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_RECYCLER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


               progressDialog.dismiss();
                try {

                    //  JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        listItems.add(new ListItem(o.getString("refnumber"), o.getString("purpose"),o.getString("amount"),o.getString("appdate")));
                    }
                    MyAdapter adapter = new MyAdapter(listItems, RecyclerActivity.this);
                    recyclerView.setAdapter(adapter);
                    //adapter = new MyAdapter(listItems,getApplicationContext());

                    //  Toast.makeText(getApplicationContext(),response.trim(),Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RecyclerActivity.this, "check your network connection", Toast.LENGTH_SHORT).show();
                        Intent myin = new Intent(RecyclerActivity.this,variousitems.class);
                        startActivity(myin);
                        finish();
                       // Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();

                    }
                }){
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
    public void onBackPressed() {
        //super.onBackPressed();
        //  Toast.makeText(detailapplicant.this,"click cancel to exit application",Toast.LENGTH_LONG).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent myintent =new Intent(RecyclerActivity.this, variousitems.class);
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