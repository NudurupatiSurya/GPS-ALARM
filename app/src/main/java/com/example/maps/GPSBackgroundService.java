package com.example.maps;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.maps.Notifyfore.CHANNEL_ID;

public class GPSBackgroundService extends Service {
    private static final int AUTO_COMP_REQ_CODE =5 ;
    private GoogleMap mMap;
    EditText autoCompleteTextView;
    Button close,letsgo,stop;
    Marker markerlongpres = null;
    LatLng dlatlng1,finallatlng,ulatlng;
    Location dlocation,ulocation;
Toast toast;
Notification notification;
   public static MediaPlayer mediaPlayer ;
    Boolean useraccess=false,destinationcheck=false;
    private static final String TAG = "MyActivity";
    LocationManager locationManager;
    Marker userlocation,destination,tempmarker;
    Geocoder geocoder;
    Double radius = 250.00;
    GridLayout alarmwidget;
    View stopa,awa;
    Notifyfore notifyfore;
    public static NotificationManagerCompat notificationManagerCompat;
    String searchstring;
    private static final LatLngBounds LAT_LNG_BOUNDS =  new LatLngBounds(
            new LatLng(-48,-168),new LatLng(71,136));

    int count=0;
    //public static final String Channel_ID = "GPS Alarm";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.alarmsong);
       dlocation = new Location(" ");
       ulocation = new Location(" ");
       notifyfore = new Notifyfore();
       notificationManagerCompat = NotificationManagerCompat.from(this);
       /* stop = stop.findViewById(R.id.stop);
        letsgo = letsgo.findViewById(R.id.letsgo);
        alarmwidget = alarmwidget.findViewById(R.id.alarmwidget);*/

        createNotificationChannel();
    }
    public void createNotificationChannel(){

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();
       // Intent notificationIntent;
        dlatlng1 = new LatLng(bundle.getDouble("latitude"),bundle.getDouble("longitude"));
        // Toast.makeText(this,"orey nen run avthunna rooo",Toast.LENGTH_SHORT).show();
       /* if(mediaPlayer.isPlaying()) {
             notificationIntent = new Intent(this, AlarmScreen.class);
        }
        else{
             notificationIntent = new Intent(this, MapsActivity.class);
        }*/
        /* PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0,notificationIntent, 0);*/
        alarm();

        /*Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (ulocation.distanceTo(dlocation) <= radius) {
                    notificationManagerCompat.notify(1, notification);
                }
            }
        },0,1000);*/

         dlocation.setLatitude(dlatlng1.latitude);
         dlocation.setLongitude(dlatlng1.longitude);
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    void alarm(){
        toast.makeText(getApplicationContext(),"outside if ra"+((ulocation.distanceTo(dlocation))),Toast.LENGTH_SHORT).show();
        notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentTitle("GPS ALARM")
                .setContentText("You Have Reached Your Destination!")
                .setSmallIcon(R.drawable.ic_gps)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        if(ulocation.distanceTo(dlocation)<=radius){

            /*letsgo.setVisibility(View.INVISIBLE);
            stop.setVisibility(View.VISIBLE);
            alarmwidget.setVisibility(View.VISIBLE);*/
           // toast.makeText(getApplicationContext(),"ocha",Toast.LENGTH_SHORT).show();
           // mediaPlayer.start();
           // onDestroy();
            /*if(stopa !=null && awa !=null) {
                stopa.setVisibility(View.VISIBLE);
                awa.setVisibility(View.VISIBLE);
            }*/
            toast.makeText(this,"oeryadsfsadf",Toast.LENGTH_SHORT).show();
            mediaPlayer.start();
            notificationManagerCompat.notify(1, notification);
            //boolean stopbw = true;
            /*MapsActivity mapsActivity = new MapsActivity();

            mapsActivity.reached(true,true);*/
           // finish();
          //  notifyfore.controlButtons(true,true);
            //bundle.putBoolean("alaramrang",true);
            //stop.setVisibility(View.VISIBLE);
            Intent alarm = new Intent(this,AlarmScreen.class);
            startActivity(alarm);

        }
        //ulocation.distanceTo(dlocation)
    }
    public void viewstop(Button ID){
        //letsgo.setId(ID);
        stopa=ID;
        stop = ID;

        //ID.setVisibility(View.VISIBLE);
    }
    public void viewaw(GridLayout ID){
       // alarmwidget.setId(ID);
        awa = ID;
        alarmwidget = ID;
        //ID.setVisibility(View.VISIBLE);
    }
    void userloc() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
            }

            // final BitmapDescriptor bitmapDescriptor =null ;
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5, 10, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    count++;
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    LatLng latLng = new LatLng(latitude, longitude);
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {

                        List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 5);
                        String localityName = addressList.get(0).getSubLocality() + " ";
                        String countryName = addressList.get(0).getCountryName();
                        if (count > 1) {
                           // Toast.makeText(getApplicationContext(), "In if Condition", Toast.LENGTH_SHORT).show();
                           // userlocation.remove();
                            //userlocation=mMap.addMarker(new MarkerOptions().position(latLng).title("Your Location " + localityName + "," + countryName).icon(BitmapDescriptorFactory.defaultMarker()));
                           // mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                           // userlocation.setTag(0);
                            ulocation.setLatitude(latLng.latitude);
                            ulocation.setLatitude(latLng.longitude);
                            ulatlng = latLng;

                        } else {
                           // userlocation=mMap.addMarker(new MarkerOptions().position(latLng).title("Your Location " + localityName + "," + countryName).icon(BitmapDescriptorFactory.defaultMarker()));
                           // mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                           // userlocation.setTag(0);
                            ulocation.setLatitude(latLng.latitude);
                            ulocation.setLatitude(latLng.longitude);
                            ulatlng = latLng;
                            // mMap.addCircle(new CircleOptions().center(latLng).radius(500.56).strokeColor(Color.BLUE));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    alarm();
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 10, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    count++;
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    LatLng latLng = new LatLng(latitude, longitude);
                    //instantiate the class,geocoder
                    //Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {

                        List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 5);
                        String localityName = addressList.get(0).getSubLocality() + " ";
                        String countryName = addressList.get(0).getCountryName();
                        if (count > 1) {
                            Toast.makeText(getApplicationContext(), "In if Condition", Toast.LENGTH_SHORT).show();
                           // userlocation.remove();
                            //userlocation=mMap.addMarker(new MarkerOptions().position(latLng).title("Your Location " + localityName + "," + countryName).icon(BitmapDescriptorFactory.defaultMarker()));
                            //mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                            //userlocation.setTag(0);
                            ulocation.setLatitude(latLng.latitude);
                            ulocation.setLatitude(latLng.longitude);
                            ulatlng = latLng;
                        } else {
                           // userlocation=mMap.addMarker(new MarkerOptions().position(latLng).title("Your Location " + localityName + "," + countryName).icon(BitmapDescriptorFactory.defaultMarker()));
                           // mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                           // userlocation.setTag(0);
                            ulocation.setLatitude(latLng.latitude);
                            ulocation.setLatitude(latLng.longitude);
                            ulatlng = latLng;
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });
        }

    }

}
