package pl.jakub.chatapp.services;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Listens for incoming FCM messages
 * and pushes them as a broadcast to all
 * subscribing elements.
 *
 * @author Jakub Zelmanowicz
 */
public class MessagingService extends FirebaseMessagingService {

    private static final String TAG = "MessagingService";

    /**
     * Obtaining a new FCM token.
     *
     * @param token - new token.
     */
    @Override
    public void onNewToken(@NonNull String token) {
        // Saving the token as a preference.
        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fcm_token", token).apply();
    }

    /**
     * Actual getting of the message.
     *
     * @param remoteMessage - incoming message.
     */
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        // Broadcasting the incoming message to other components.
        Intent broadcast = new Intent("MessageReceived");
        broadcast.putExtra("sender", remoteMessage.getNotification().getTitle());
        broadcast.putExtra("content", remoteMessage.getNotification().getBody());
        sendBroadcast(broadcast);

    }

}
