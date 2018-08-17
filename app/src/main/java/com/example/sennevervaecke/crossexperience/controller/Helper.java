package com.example.sennevervaecke.crossexperience.controller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by sennevervaecke on 5/9/2018.
 */

public class Helper {
    public static String ALIGN_TOP = "alignTop";
    public static String ALIGN_CENTER = "alignCenter";
    public static String ALIGN_BOTTOM = "alignBottom";

    //check if there is a connection to the internet
    public static boolean isInternetConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null &&
                connectivityManager.getActiveNetworkInfo().isConnected();
    }
    //check if there is an connection with wifi
    public static boolean isWifiConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return networkInfo != null && networkInfo.isConnected();
    }
    //check if there is a connection to mobile data
    public static boolean isMobileDataConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return networkInfo != null && networkInfo.isConnected();
    }

    public static void showMessage(View view, String message){
        showMessage(view, message, ALIGN_TOP);
    }
    public static void showMessage(View view, String message, String alignment){
        makeSnackbar(view, message, alignment).show();
    }
    public static Snackbar makeSnackbar(View view, String message, String alignment){
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        TextView tv = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        } else {
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
        }
        FrameLayout.LayoutParams layout = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
        if(ALIGN_TOP.equals(alignment)){
            layout.gravity = Gravity.TOP;
        } else if(ALIGN_CENTER.equals(alignment)){
            layout.gravity = Gravity.CENTER;
        } else if(ALIGN_BOTTOM.equals(alignment)){
            layout.gravity = Gravity.BOTTOM;
        }
        snackbarView.setLayoutParams(layout);
        return snackbar;
    }
    public static String firstToUpper(String value){
        if(value == null){
            return null;
        }
        if(value.length() == 0){
            return "";
        }
        if(value.length() == 1){
            return value.toUpperCase();
        }
        return value.substring(0, 1).toUpperCase() + value.substring(1, value.length());
    }
    public static String toCamelCase(String value){
        if(value == null){
            return null;
        }
        String[] splittedValue = value.split(" ");
        StringBuilder builder = new StringBuilder();
        for(String split : splittedValue){
            builder.append(firstToUpper(split));
        }
        return builder.toString();
    }
}
