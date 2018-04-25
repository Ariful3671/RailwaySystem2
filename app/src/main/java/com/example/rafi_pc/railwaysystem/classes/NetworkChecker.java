package com.example.rafi_pc.railwaysystem.classes;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Arif on 24-04-18.
 */

public class NetworkChecker {

    Context context;

    public NetworkChecker(Context context) {
        this.context = context;
    }

    public boolean isConnected()
    {
        ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Service.CONNECTIVITY_SERVICE);
        if(connectivityManager!=null)
        {
            NetworkInfo info=connectivityManager.getActiveNetworkInfo();
            if(info!=null)
            {
                if(info.getState()==NetworkInfo.State.CONNECTED)
                {
                    return true;
                }

            }
        }
        return false;
    }
}
