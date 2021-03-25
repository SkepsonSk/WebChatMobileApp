package pl.jakub.chatapp.net;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.function.Consumer;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Abstract base of all other requests.
 * Provides all the necessary functionality for
 * request sending.
 *
 * @author Jakub Zelmanowicz.
 */
public abstract class AsyncRequest implements Runnable {

    /**
     * Setting the default HTTP information.
     */
    protected static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    // ---- ---- ---- ----

    protected final String url;
    private Consumer<Response> onResponse;

    public AsyncRequest(String url) {
        this.url = url;
    }

    public void setOnResponse(Consumer<Response> onResponse) {
        this.onResponse = onResponse;
    }

    public abstract Request createRequest();

    /**
     * Sends the request async to avoid suspending
     * the main thread.
     */
    @Override
    public void run() {

        OkHttpClient client = new OkHttpClient();

        Request request = createRequest();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                call.cancel();
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if (onResponse != null)
                    onResponse.accept(response);
            }

        });

    }

}
