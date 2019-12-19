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

    public static final String DATA_NAME = "name";
    public static final String DATA_AGE = "age";
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
        wvEHR.loadUrl("http://192.168.1.205:3000/");
        wvEHR.addJavascriptInterface(new JSInterface(), "Android");

        String name, age;

        Intent intent = getIntent();
        name = checkEmpty(intent.getStringExtra(DATA_NAME));
        age = checkEmpty(intent.getStringExtra(DATA_AGE));

        Map<String, String> map = new HashMap<String, String>();
        map.put(DATA_NAME, name);
        map.put(DATA_AGE, age);

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
