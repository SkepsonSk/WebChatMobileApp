package pl.jakub.chatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pl.jakub.chatapp.net.AsyncRequest;
import pl.jakub.chatapp.net.GetRequest;
import pl.jakub.chatapp.net.PostRequest;
import pl.jakub.chatapp.net.PutRequest;
import pl.jakub.chatapp.rooms.Room;
import pl.jakub.chatapp.rooms.RoomAdapter;
import pl.jakub.chatapp.viewmodel.UserViewModel;

/**
 * Main activity of the Chat Application.
 *
 * @author Jakub Zelmanowicz
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ChatAppActivity";

    /**
     * ViewModel managing the user's data.
     */
    private UserViewModel userViewModel;

    private EditText nameEditText;
    private TextView errTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userViewModel = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication())
                .create(UserViewModel.class);

        nameEditText = findViewById(R.id.nameEditText);
        errTextView = findViewById(R.id.errTextView);

        // Should the room be opened on app creation?
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("room")) {
            Log.d(TAG, "onCreate: ROOM PRESENT: " + getIntent().getStringExtra("room"));

            String userId = getSharedPreferences("_", MODE_PRIVATE).getString("fcm_user_id", null);

            if (userId != null) {
                String roomId = getIntent().getStringExtra("room");
                Log.d(TAG, "onCreate: ROOM PRESENT: " + userId);

                // Starting chat activity.
                Intent chat = new Intent(MainActivity.this, ChatActivity.class);
                chat.putExtra("userId", userId);
                chat.putExtra("roomId", roomId);
                startActivity(chat);
            }

            return;
        }

        // Checking Internet connection.
        AsyncTask.execute( () -> {
            Utils.checkInternetConnection(MainActivity.this,
                    suc -> {
                        errTextView.setVisibility(View.INVISIBLE);
                        initializeRooms();
                    },
                    fail -> {
                        errTextView.setText(getText(R.string.no_internet_conn_err));
                        errTextView.setVisibility(View.VISIBLE);
                    } );
        } );

    }

    private RecyclerView recyclerView;
    private RoomAdapter roomAdapter;
    private RecyclerView.LayoutManager layoutManager;

    // Download all the rooms from the Web Chat Server.
    private void initializeRooms() {

        AsyncRequest roomsReq = new GetRequest("http://Chat-env.eba-afmawu2f.eu-central-1.elasticbeanstalk.com/api/v1/room");
        roomsReq.setOnResponse( roomsRes -> {

            String id, name;
            List<Room> rooms = new ArrayList<>();

            try {

                JSONArray arr = new JSONArray(roomsRes.body().string());
                JSONObject obj;

                for (int i = 0 ; i < arr.length() ; i++) {
                    obj = arr.getJSONObject(i);
                    id = obj.getString("id");
                    name = obj.getString("name");
                    rooms.add(new Room(id, name));
                }

            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }

            // Display rooms in UI.
            runOnUiThread( () -> {
                recyclerView = findViewById(R.id.roomsRecyclerView);

                roomAdapter = new RoomAdapter(rooms);
                roomAdapter.setOnRoomSelect(this::connectToRoom);

                layoutManager = new GridLayoutManager(this, 2);
                recyclerView.setAdapter(roomAdapter);
                recyclerView.setLayoutManager(layoutManager);
            } );

        } );

        roomsReq.run();

    }

    /**
     * Connecting to the specific
     * room.
     *
     * @param room - room to join to.
     */
    private void connectToRoom(Room room) {

        String token = getSharedPreferences("_", MODE_PRIVATE)
                .getString("fcm_token", "");
        String name = nameEditText.getText().toString();

        // Creates a user on the backend side.
        AsyncRequest userReq = new PostRequest("http://<URL>/api/v1/user",
                "{\"name\": \"" + name + "\", \"token\": \"" + token + "\"}");
        userReq.setOnResponse( userRes -> {

            String userId = null;

            try {
                JSONObject userObj = new JSONObject(userRes.body().string());
                userId = userObj.getString("id");
                getSharedPreferences("_", MODE_PRIVATE).edit().putString("fcm_user_id", userId).apply();

                userViewModel.changeUuid(userId);
            } catch (IOException | JSONException ioException) {
                ioException.printStackTrace();
            }

            Log.d(TAG, "connectToRoom: " + userViewModel.getUuid().getValue());

            // Adds user to the room on backend side.
            AsyncRequest joinRequest = new PutRequest(String.format("http://<URL>/api/v1/room/%s/%s", userId, room.getId()), "");
            joinRequest.setOnResponse(joinRes -> {

                Intent chat = new Intent(MainActivity.this, ChatActivity.class);
                chat.putExtra("userId", userViewModel.getUuid().getValue());
                chat.putExtra("roomId", room.getId());
                startActivity(chat);

            } );
            joinRequest.run();

        } );

        userReq.run();

    }


}