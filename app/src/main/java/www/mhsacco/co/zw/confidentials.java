package www.mhsacco.co.zw;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Requests.RequestHandler;
import SharedPreferences.SharedPrefManager;
import constants.Constants;

public class confidentials extends AppCompatActivity implements View.OnClickListener{
    private Button btupload ,btselectid;
    private EditText edidnumber,edpaynumber;
    private ImageView imgview,imgview1;

    private FloatingActionButton fab;
    //static
    private  final int PIC_REQUEST = 2;
    private  final int IMG_REQUEST = 1;
    String imageEncoded;
    List<String> imagesEncodedList;
    private Bitmap bitmap;
    private ProgressDialog progressDialog;

    


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confidentials);


        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }


        edidnumber = (EditText)findViewById(R.id.idnumber);
        edpaynumber = (EditText) findViewById(R.id.paynumber);

        btupload = (Button)findViewById(R.id.upload);
        btselectid = (Button)findViewById(R.id.selectid);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        imgview = (ImageView)findViewById(R.id.imageView);
        imgview1 = (ImageView)findViewById(R.id.imgView1);
        


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        btselectid.setOnClickListener(this);
        btupload.setOnClickListener(this);
        fab.setOnClickListener(this);


        edpaynumber.setText(SharedPrefManager.getInstance(this).getpaynumber());

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.selectid:
                selectimage();
                break;

            case R.id.upload:
                uploadimage();
                break;

            case R.id.fab:
                takepicture();
                break;
        }
    }

    private void takepicture()
    {
        Intent takeimageintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takeimageintent.resolveActivity(getPackageManager())!=null)
        {

            startActivityForResult(takeimageintent,PIC_REQUEST);
        }

    }
    private void selectimage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
      //  intent.putExtra(intent.EXTRA_ALLOW_MULTIPLE,false);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG_REQUEST);
    }
    public int getSquareCropDimensionForBitmap(Bitmap bitmap) {
        return Math.min(bitmap.getWidth(), bitmap.getHeight());
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMG_REQUEST && resultCode==RESULT_OK && data!=null)
        {

            Uri path = data.getData();


            //Bundle extras = data.getExtras();
            //Bitmap imageBitmap;
            //imageBitmap = (Bitmap) extras.get("data");
            //imgview.setImageBitmap(imageBitmap);
//Picasso.with(mContext).load(filePath).centerInside().resize(YourWidth,YourHeight).into(imageview);
            try {
               bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                 int dimension = getSquareCropDimensionForBitmap(bitmap);

                bitmap = ThumbnailUtils.extractThumbnail(bitmap, dimension, dimension);

                imgview.setImageBitmap(bitmap);
                imgview.setVisibility(View.VISIBLE);
                edidnumber.setVisibility(View.VISIBLE);
                btupload.setVisibility(View.VISIBLE);
                btselectid.setVisibility(View.GONE);
         } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    


//UPLOADING THE IMAGE

    private void uploadimage(){
        final String idnumber = edidnumber.getText().toString().trim();
        if(idnumber.isEmpty()){
            edidnumber.setError("idnumber required");
            edidnumber.requestFocus();
            return;
        }
        if (idnumber.length()<7) {
            edidnumber.setError("enter valid idnumber");
            edidnumber.requestFocus();
            return;

        }
        btupload.setVisibility(View.VISIBLE);
        btselectid.setVisibility(View.INVISIBLE);
        progressDialog.show();
        progressDialog.setCancelable(false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_UPLOAD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Intent myIntent = new Intent(confidentials.this, finishapply.class);
            startActivity(myIntent);
            finish();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String Response = jsonObject.getString("response");
                            Toast.makeText(confidentials.this,Response,Toast.LENGTH_LONG).show();
                            imgview.setImageResource(0);
                            imgview.setVisibility(View.GONE);
                            edidnumber.setText("");
                            edidnumber.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(confidentials.this, "check connection", Toast.LENGTH_SHORT).show();
                //Toast.makeText(
                  //      getApplicationContext(),
                  //      error.getMessage(),
                   //     Toast.LENGTH_LONG
               // ).show();

            }
        }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("idnumber", edidnumber.getText().toString().trim());
                params.put("paynumber", edpaynumber.getText().toString().trim());
                params.put("image",imageToString(bitmap));


                return params;
            }
        };
        RequestHandler.getInstance(confidentials.this).addToRequestQueue(stringRequest);

    }
    private String imageToString(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
        byte[] imgbytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgbytes,Base64.DEFAULT);
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
                        Intent myintent =new Intent(confidentials.this, variousitems.class);
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