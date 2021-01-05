package com.example.maps;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.data.DataBufferUtils;

import com.google.android.gms.maps.model.LatLngBounds;


import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


public class Notifyfore extends Application {
   /* public void controlButtons(Boolean stopbool,Boolean Alarmwidgbool){
        if(stopbool == true && Alarmwidgbool == true){
            MapsActivity mapsActivity = new MapsActivity();
            mapsActivity.reached(true,true);
        }
    }*/
   public static final String CHANNEL_ID = "exampleServiceChannel";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }
    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "GPS ALARM",
                    NotificationManager.IMPORTANCE_HIGH
            );
            final NotificationManager manager = getSystemService((NotificationManager.class));
            manager.createNotificationChannel(notificationChannel);

    }
}
}
