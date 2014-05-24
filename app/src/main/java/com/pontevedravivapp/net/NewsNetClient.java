package com.pontevedravivapp.net;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


public class NewsNetClient {
    private static final String BASE_URL = "http://pontevedraviva.com";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String relativeUrl, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(relativeUrl), params, responseHandler);
    }

    public static void loadImage(String url, AsyncHttpResponseHandler responseHandler) {
        client.get(url, null, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
