package pl.jakub.chatapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Experimental feature.
 * Listens for network changes.
 *
 * @author Jakub Zelmanowicz
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    private static final String TAG = "NetworkChangeReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: Internet state changed " + intent.getAction());
        Toast.makeText(context, "STATE CHANGED", Toast.LENGTH_LONG).show();
    }

}
