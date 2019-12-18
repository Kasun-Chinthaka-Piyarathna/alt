package com.developer.sdklibrary;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

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

    public static void openWebView(String firstname, String dob) {
        Intent intent = new Intent(appContext, MainActivity.class);
        intent.putExtra(MainActivity.DATA_NAME, firstname);
        intent.putExtra(MainActivity.DATA_AGE, dob);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        appContext.startActivity(intent);

    }
}
