package pl.jakub.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pl.jakub.chatapp.messages.MessagesAdapter;
import pl.jakub.chatapp.net.AsyncRequest;
import pl.jakub.chatapp.net.PostRequest;
import pl.jakub.chatapp.net.PutRequest;

/**
 * Activity responsible to bring
 * user chatting functionality.
 *
 * @author Jakub Zelmanowicz
 */
public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";

    private BroadcastReceiver messageReceiver;
    private IntentFilter intentFilter;

    // Current user and room id.
    private String userId;
    private String roomId;

    // Message textbox input.
    private EditText messageEditText;

    // Send message button.
    private Button sendButton;

    // Recycler view displaying messages
    // with its adapter and layout manager.
    private RecyclerView recyclerView;
    private MessagesAdapter messagesAdapter;
    private RecyclerView.LayoutManager layoutManager;

    /**
     * Creates the chat activity.
     * Prepares components and loads data.
     * Also handles orientation changes
     * and halts.
     *
     * @param savedInstanceState - saved instance of the app
     * e.g. before orientation occurred.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        roomId = intent.getStringExtra("roomId");

        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);

        prepareRecyclerView();

        initSendButton();
    }

    /**
     * Prepares the recycler view. Assigns
     * messages adapter and linear layout (vertical).
     */
    private void prepareRecyclerView() {
        recyclerView = findViewById(R.id.messagesRecyclerView);
        messagesAdapter = new MessagesAdapter();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(messagesAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    /**
     * On app resume (called after creation and start).
     * As well as on actual resume.
     *
     * @see pl.jakub.chatapp.services.MessagingService
     */
    @Override
    protected void onResume() {
        super.onResume();

        // Sets up the broadcast receiver to send
        // the incoming message from
        messageReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                // On message receive - send message to the user's
                // recycler view.
                String sender = intent.getStringExtra("sender");
                String content = intent.getStringExtra("content");
                messagesAdapter.pushMessage(sender, content);

                Log.d(TAG, "onReceive: " + sender + ": " + content);

            }

        };

        // Receive only broadcasts tagged ass MessageReceived.
        intentFilter = new IntentFilter("MessageReceived");

        // Registers the receiver.
        registerReceiver(messageReceiver, intentFilter);
    }

    /**
     * When activity was paused - unregister the
     * Broadcast receiver.
     */
    @Override
    protected void onPause() {
        if(messageReceiver != null)
            unregisterReceiver(messageReceiver);

        messageReceiver = null;
        super.onPause();
    }

    // ----

    /**
     * Saving state of the app.
     *
     * @param outState - state of the app pack.
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("userId", userId);
        outState.putString("roomId", roomId);
    }

    /**
     * When state is trying to be restored.
     *
     * @param savedInstanceState - state to restore.
     */
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        userId = savedInstanceState.getString("userId");
        roomId = savedInstanceState.getString("roomId");
    }

    // ----

    /**
     * When back button was pressed
     * - remove user from the room and
     * return to the previous activity.
     */
    @Override
    public void onBackPressed() {

        AsyncRequest leaveReq = new PutRequest("http://Chat-env.eba-afmawu2f.eu-central-1.elasticbeanstalk.com/api/v1/room/leave/"
                + userId + "/" + roomId, "");

        // Leaving the room.
        leaveReq.setOnResponse( res -> {

            runOnUiThread( () -> {

                Toast.makeText(this, "Opuszczono pokój", Toast.LENGTH_LONG).show();
                super.onBackPressed();

            } );

        } );

        leaveReq.run();

    }

    // ----

    /**
     * Initializes the send button.
     */
    private void initSendButton() {

        sendButton.setOnClickListener( e -> {

            // Get the message from message text box.
            String message = messageEditText.getText().toString();

            // Post the message to Web Chat Server.
            PostRequest req = new PostRequest("http://Chat-env.eba-afmawu2f.eu-central-1.elasticbeanstalk.com/api/v1/message",
                    String.format("{\"sender\": \"%s\", \"content\": \"%s\"}", userId, message));
            req.setOnResponse( reqRes ->  {

                // Add the message as user's.
                runOnUiThread( () -> {
                    messagesAdapter.pushMessage("Ja", message);
                } );


            });

            req.run();

        } );

    }

}