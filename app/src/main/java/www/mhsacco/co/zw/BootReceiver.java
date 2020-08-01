package www.mhsacco.co.zw;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import constants.NotificationService;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Service Stops","OHHHHHHH");
        context.startService(new Intent(context, NotificationService.class));
    }
}
