package com.developer.sdklibrary;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import org.json.JSONObject;

public class WebView {

    private static WebView instance;
    private static Application appContext = null;

    WebView(Application application) {
        this.appContext = application;
    }

    public static void init(Application application) {
        instance = new WebView(application);

    }

    public static WebView getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Webview not initialized");
        }
        return instance;
    }


    public static void displayToastMsg(Context c, String message) {

        Toast.makeText(c, message, Toast.LENGTH_SHORT).show();

    }

    public static void attachListener(MainActivity.Callback<JSONObject> callback) {
        MainActivity.callback = callback;
    }

    public static String getData() {

        return MainActivity.returnData;

    }

    public static void openWebView(String phoneNumber, String firstName, String lastName, String authToken, String appId, String jsonData) {
        Intent intent = new Intent(appContext, MainActivity.class);
        intent.putExtra(MainActivity.DATA_PHONE_NUMBER, phoneNumber);
        intent.putExtra(MainActivity.DATA_FIRST_NAME, firstName);
        intent.putExtra(MainActivity.DATA_LAST_NAME, lastName);
        intent.putExtra(MainActivity.DATA_AUTH_TAKEN, authToken);
        intent.putExtra(MainActivity.DATA_APP_ID, appId);
        intent.putExtra(MainActivity.DATA_JSON_DATA, jsonData);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        appContext.startActivity(intent);

    }
}
