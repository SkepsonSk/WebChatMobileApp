package pl.jakub.chatapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Consumer;

/**
 * Simple utility tools.
 *
 * @author Jakub Zelmanowicz
 */
public class Utils {

    /**
     * Checks the Internet connection
     * using Android system data as well as
     * a simple HTTP test.
     *
     * @param context - Android app context,
     * @param success - what to do on success,
     * @param failed - what to do on failure.
     */
    public static void checkInternetConnection(Context context,
                                               Consumer<Void> success,
                                               Consumer<Void> failed) {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected())
            failed.accept(null);
            //errTextView.setText(context.getText(R.string.no_internet_conn_err));

        else {

            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();

                if (urlc.getResponseCode() != 200)
                    failed.accept(null);
                    //errTextView.setText(context.getText(R.string.no_internet_conn_err));

            } catch (IOException e) {
                failed.accept(null);
                //errTextView.setText(context.getText(R.string.no_internet_conn_err));
            }

            success.accept(null);

        }

    }

}
