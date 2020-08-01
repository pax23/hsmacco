package constants;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import Requests.RequestHandler;
import SharedPreferences.SharedPrefManager;
import www.mhsacco.co.zw.R;


public class NotificationService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mTimer = new Timer();
       mTimer.schedule(timerTask, 2, 604800 *1000);
        //mTimer.cancel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private Timer mTimer;
    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            Log.e("Log", "Running");

            notification();
        }
    };

    @Override
    public void onDestroy() {

        try {
            mTimer.cancel();
            timerTask.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent intent = new Intent("www.mhsacco.co.zw");
        intent.putExtra("yourvalue","torestore");
        sendBroadcast(intent);
    }


    public   void notification (){
        final String paynumber = (SharedPrefManager.getInstance(this).getpaynumber());

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_ACCOUNTCHECK,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        //progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if(!jsonObject.getBoolean("error")){
                                IntentFilter intentFilter = new IntentFilter();
                                intentFilter.addAction("RSSPUllService");

                                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(""));
                                @SuppressLint("WrongConstant") PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(),0,myIntent,Intent.FLAG_ACTIVITY_NEW_TASK);
                                Context context = getApplicationContext();
                                Notification.Builder builder;

                                builder = new Notification.Builder(context)
                                        .setContentTitle("MHSACCO")
                                        .setContentText("Congratulations your loan has been approved/Kindly contact us to get your loan transferred")
                                        .setContentIntent(pendingIntent)
                                        .setDefaults(Notification.DEFAULT_SOUND)
                                        .setAutoCancel(true)
                                        .setSmallIcon(R.drawable.ic_message_black_24dp);
                                Notification notification = builder.build();
                                NotificationManager notificationManager =(NotificationManager)getSystemService(context.NOTIFICATION_SERVICE);
                                notificationManager.notify(1,notification);

                            }else if (jsonObject.getBoolean("error")){
                               declined();

                            }
                            else {}

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //progressDialog.hide();
                        Toast.makeText(NotificationService.this, "check your network connection", Toast.LENGTH_SHORT).show();



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
    //check if application is declined
    private  void declined(){


        final String paynumber = (SharedPrefManager.getInstance(this).getpaynumber());

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_CHECKDECLINED,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if(!jsonObject.getBoolean("error")){

                          notifiy();

                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                   //     progressDialog.hide();
                        Toast.makeText(NotificationService.this, "check your network connection", Toast.LENGTH_SHORT).show();



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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void notifiy(){


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("RSSPUllService");

        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(""));
        @SuppressLint("WrongConstant") PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(),0,myIntent,Intent.FLAG_ACTIVITY_NEW_TASK);
        Context context = getApplicationContext();
        Notification.Builder builder;

        builder = new Notification.Builder(context)
                .setContentTitle("MHSACCO")
                .setContentText("Loan request has been declined/kindly contact us for further confirmation")
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_message_black_24dp);
        Notification notification = builder.build();
        NotificationManager notificationManager =(NotificationManager)getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,notification);

    }
}
