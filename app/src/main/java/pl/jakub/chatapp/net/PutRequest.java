package pl.jakub.chatapp.net;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Sends the request as PUT.
 *
 * @author Jakub Zelmanowicz
 */
public class PutRequest extends PostRequest {

    public PutRequest(String url, String body) {
        super(url, body);
    }

    @Override
    public Request createRequest() {
        RequestBody requestBody = RequestBody.create(JSON, body);
        return new Request.Builder()
                .url(url)
                .put(requestBody)
                .build();
    }
}
