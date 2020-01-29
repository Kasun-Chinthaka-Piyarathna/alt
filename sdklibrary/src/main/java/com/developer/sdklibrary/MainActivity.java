package com.developer.sdklibrary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String DATA_PHONE_NUMBER = "phoneNumber";
    public static final String DATA_FIRST_NAME = "firstName";
    public static final String DATA_LAST_NAME = "lastName";
    public static final String DATA_AUTH_TAKEN = "authToken";
    public static final String DATA_APP_ID = "appId";
    public static final String DATA_JSON_DATA = "jsonData";
    private static final String JS_FUNCTION_INIT = "token";
    private static final String TAG = "EMHActivity";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.webkit.WebView wvEHR;

        wvEHR = findViewById(R.id.wvEMH);
        wvEHR.getSettings().setJavaScriptEnabled(true);
        wvEHR.getSettings().setDomStorageEnabled(true);
        wvEHR.loadUrl("https://dev.gen2.odoc.life/di");
        wvEHR.addJavascriptInterface(new JSInterface(), "Android");

        String phoneNumber, firstName, lastName, authToken, appId, jsonData;

        Intent intent = getIntent();
        phoneNumber = checkEmpty(intent.getStringExtra(DATA_PHONE_NUMBER));
        firstName = checkEmpty(intent.getStringExtra(DATA_FIRST_NAME));
        lastName = checkEmpty(intent.getStringExtra(DATA_LAST_NAME));
        authToken = checkEmpty(intent.getStringExtra(DATA_AUTH_TAKEN));
        appId = checkEmpty(intent.getStringExtra(DATA_APP_ID));
        jsonData = checkEmpty(intent.getStringExtra(DATA_JSON_DATA));

        Map<String, String> map = new HashMap<String, String>();
        map.put(DATA_PHONE_NUMBER, phoneNumber);
        map.put(DATA_FIRST_NAME, firstName);
        map.put(DATA_LAST_NAME, lastName);
        map.put(DATA_AUTH_TAKEN, authToken);
        map.put(DATA_APP_ID, appId);
        map.put(DATA_JSON_DATA, jsonData);

        final JSONObject json = new JSONObject(map);

        Log.d(TAG, json.toString());

        wvEHR.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                view.evaluateJavascript(JS_FUNCTION_INIT + "(" + json.toString() + ")", null);
            }
        });

    }

    private String checkEmpty(String text) {
        if (text == null) {
            return "N/A";
        }
        return text;
    }

    private class JSInterface {
        JSInterface() {

        }

        @JavascriptInterface
        public void pageClosed() {
            MainActivity.this.finish();
        }
    }
}
