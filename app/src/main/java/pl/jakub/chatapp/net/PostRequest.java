package pl.jakub.chatapp.net;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Sends the request as POST.
 *
 * @author Jakub Zelmanowicz
 */
public class PostRequest extends AsyncRequest {

    protected final String body;

    public PostRequest(String url, String body) {
        super(url);
        this.body = body;
    }

    @Override
    public Request createRequest() {
        RequestBody requestBody = RequestBody.create(JSON, body);
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }
}
