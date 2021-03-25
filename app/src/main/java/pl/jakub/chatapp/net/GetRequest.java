package pl.jakub.chatapp.net;

import okhttp3.Request;

/**
 * Sends the request as GET.
 *
 * @author Jakub Zelmanowicz
 */
public class GetRequest extends AsyncRequest {

    public GetRequest(String url) {
        super(url);
    }

    @Override
    public Request createRequest() {
        return new Request.Builder()
                .url(url)
                .build();
    }

}
