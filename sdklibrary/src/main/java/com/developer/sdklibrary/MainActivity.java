package com.developer.sdklibrary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
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
    public static String returnData;

    public static Callback<JSONObject> callback = null;


    android.webkit.WebView wvEHR;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wvEHR = findViewById(R.id.wvEMH);
        wvEHR.getSettings().setJavaScriptEnabled(true);
        wvEHR.getSettings().setDomStorageEnabled(true);
        wvEHR.loadUrl("http://192.168.1.68:3000");
       // wvEHR.loadUrl("https://dev.gen2.odoc.life/di");
        wvEHR.addJavascriptInterface(new JSInterface(), "mobile");

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE))
            { WebView.setWebContentsDebuggingEnabled(true); }
        }

    }

    private String checkEmpty(String text) {
        if (text == null) {
            return "N/A";
        }
        return text;
    }

    @Override
    public void onBackPressed() {
//        if (wvEHR.canGoBack())
//            wvEHR.goBack();
//        else
//            super.onBackPressed();

    }

    public interface Callback<T> {

        void onSuccess(T result);

    }

    private class JSInterface {
        Context mContext;

        JSInterface() {
        }

        JSInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void pageClosed() {
            MainActivity.this.finish();
        }

        @JavascriptInterface
        public void backToDevice(String backToAndroidData) {
            Log.d("DataFromWeb", backToAndroidData);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("token", true);
                jsonObject.put("value", backToAndroidData);
                callback.onSuccess(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @JavascriptInterface
        public void openExternalLink(String url) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("token", false);
                jsonObject.put("value", url);
                callback.onSuccess(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        @JavascriptInterface
        public void addPrescriptionMetaData(String status) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("meta_pres", status);
                callback.onSuccess(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        @JavascriptInterface
        public void investigationRequested(Boolean requested) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("investigationRequested", requested);
                callback.onSuccess(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
