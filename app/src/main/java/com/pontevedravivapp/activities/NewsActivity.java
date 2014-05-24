package com.pontevedravivapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.pontevedravivapp.R;
import com.pontevedravivapp.net.NewsNetClient;

import org.apache.http.Header;


public class NewsActivity extends Activity {
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        String url = intent.getStringExtra("url");

        TextView textViewTitle = (TextView) findViewById(R.id.tv_title);
        TextView textViewContent = (TextView) findViewById(R.id.tv_content);


        textViewTitle.setText(title);
        textViewContent.setText(Html.fromHtml(content));

        //TODO: Mostrar la imagen bien.
        // imageView = (ImageView) findViewById(R.id.imageView);
        //NewsNetClient.loadImage(url, new HttpResponseHandler());

    }


    private class HttpResponseHandler extends BinaryHttpResponseHandler {

        @Override
        public void onSuccess(byte[] imageData) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            imageView.setImageBitmap(bitmap);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            super.onFailure(statusCode, headers, responseBody, error);

        }

    }

}
