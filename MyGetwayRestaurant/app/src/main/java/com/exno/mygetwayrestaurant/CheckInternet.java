package com.exno.mygetwayrestaurant;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckInternet {
    Context context;

    public CheckInternet(Context context) {
        this.context = context;
    }

    Boolean getInternetStatus() {
    ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo nInfo = cm.getActiveNetworkInfo();
    return  nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
}

}
