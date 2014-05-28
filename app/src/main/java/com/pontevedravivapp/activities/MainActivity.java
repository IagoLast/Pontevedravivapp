package com.pontevedravivapp.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.pontevedravivapp.NewsAdapter;
import com.pontevedravivapp.R;
import com.pontevedravivapp.net.NewsNetClient;
import com.pontevedravivapp.xml.Entry;
import com.pontevedravivapp.xml.PontevedraVivaXmlParser;

import org.apache.http.Header;

import java.io.ByteArrayInputStream;
import java.util.List;

public class MainActivity extends Activity {
    ListView listView;
    ActionBar actionBar;
    ImageView progressBar;
    Activity activity;
    AnimationDrawable animationDrawable;


    List<Entry> entries;
    private String currentUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadUI();
        if (savedInstanceState != null) {
            Log.i("Main", "loadedFromCache");
            currentUrl = savedInstanceState.getString("currentUrl");
            entries = (List<Entry>) savedInstanceState.getSerializable("entries");
            populateListView(entries);
        } else {
            downloadData(getString(R.string.url_news));
        }
    }

    private void loadUI() {
        progressBar = (ImageView) findViewById(R.id.progressBar1);
        listView = (ListView) findViewById(R.id.listView1);
        animationDrawable = (AnimationDrawable) progressBar.getBackground();
        actionBar = getActionBar();
        activity = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        animationDrawable.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        animationDrawable.stop();
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        bundle.putString("currentUrl", currentUrl);
        bundle.putSerializable("entries", (java.io.Serializable) entries);
        super.onSaveInstanceState(bundle);
    }

    private void downloadData(String url) {
        currentUrl = url;
        showLoading(true);
        NewsNetClient.get(url, null, new HttpResponseHandler());
    }


    private void populateListView(List<Entry> items) {
        entries = items;
        NewsAdapter newsAdapter = new NewsAdapter(this, items);
        listView.setAdapter(newsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(activity, NewsActivity.class);
                Entry entry = entries.get(position);
                intent.putExtra("title", entry.getTitle());
                intent.putExtra("content", entry.getText());
                intent.putExtra("url", entry.getImageUrl());
                startActivity(intent);
            }
        });
        showLoading(false);
    }


    private void showLoading(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            actionBar.setTitle(R.string.loading);
            listView.setVisibility(View.GONE);
            animationDrawable.start();

        } else {
            progressBar.setVisibility(View.GONE);
            actionBar.setTitle(R.string.app_name);
            listView.setVisibility(View.VISIBLE);
            animationDrawable.stop();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String url;
        switch (item.getItemId()) {
            case R.id.action_reload:
                downloadData(currentUrl);
                return true;
            case R.id.action_culture:
                url = getResources().getString(R.string.url_culture);
                downloadData(url);
                return true;
            case R.id.action_news:
                url = getResources().getString(R.string.url_news);
                downloadData(url);
                return true;
            case R.id.action_opinion:
                url = getResources().getString(R.string.url_opinion);
                downloadData(url);
                return true;
            case R.id.action_sports:
                url = getResources().getString(R.string.url_sports);
                downloadData(url);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private class HttpResponseHandler extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            super.onSuccess(statusCode, headers, responseBody);
            try {
                populateListView(PontevedraVivaXmlParser.parse(new ByteArrayInputStream(responseBody)));
            } catch (Exception e) {
                actionBar.setTitle(R.string.error_processing_news);
                showLoading(false);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            super.onFailure(statusCode, headers, responseBody, error);
            actionBar.setTitle(R.string.error_downloading_news);
            error.printStackTrace();
            showLoading(false);
        }

    }
}
