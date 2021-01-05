package com.example.maps;

/*import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;*/
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.Marker;
/*import com.google.android.libraries.places.compat.AutocompleteFilter;
import com.google.android.libraries.places.compat.AutocompleteFilter.Builder;
import com.google.android.libraries.places.compat.AutocompletePrediction;
import com.google.android.libraries.places.compat.AutocompletePredictionBuffer;
import com.google.android.libraries.places.compat.AutocompletePredictionBufferResponse;*/

/*import com.google.android.libraries.places.compat.GeoDataClient;
import com.google.android.libraries.places.compat.Place;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.Places;*/
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
/*import com.google.android.libraries.places.compat.PlaceBufferResponse;
import com.google.android.libraries.places.compat.ui.PlaceAutocomplete;*/

import java.io.IOException;
import java.nio.channels.ClosedByInterruptException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import static com.google.android.gms.tasks.Tasks.await;

public  class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener , GoogleApiClient.OnConnectionFailedListener
        , GoogleMap.OnMapLongClickListener , View.OnLongClickListener
        , AdapterView.OnItemSelectedListener , SeekBar.OnSeekBarChangeListener {
    private static final int AUTO_COMP_REQ_CODE =5 ;
    private GoogleMap mMap;
    EditText autoCompleteTextView;
    Button close,letsgo,stop;
    Marker markerlongpres = null;
    LatLng dlatlng,finallatlng,ulatlng;
    Location dlocation,ulocation;
    Circle destinationcircle;
    TextView seekbartextview;
    String placename;
    MediaPlayer mediaPlayer;
    Boolean useraccess=false,destinationcheck=false;
    private static final String TAG = "MyActivity";
    LocationManager locationManager;
    Marker userlocation,destination,tempmarker;
    Geocoder geocoder;
    boolean markerpressthere=false;
    SeekBar radiuseekbar;
    Spinner searchcompletespiner;
    int radius = 250;
    Double dlat,dlong;
    GridLayout alarmwidget;
    Toast toast ;
    String ssd;
    boolean alarmrang = false;
    private static final LatLngBounds LAT_LNG_BOUNDS =  new LatLngBounds(
            new LatLng(-48,-168),new LatLng(71,136));
/*    AsyncTask asyncTask = new AsyncTask() {
        @Override
        protected Object doInBackground(Object[] objects) {

            return null;
        }
    };*/

    GoogleMap mmap;
    int count=0;
    boolean longclick;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        MapsInitializer.initialize(getApplicationContext());
        autoCompleteTextView = (EditText) findViewById(R.id.input_search);
        autoCompleteTextView.setText(" ");
        close = (Button) findViewById(R.id.close);
        letsgo = (Button) findViewById(R.id.letsgo);
        letsgo.setOnClickListener(this);
        close.setOnClickListener(this);
        geocoder = new Geocoder(getApplicationContext());
        mediaPlayer = MediaPlayer.create(this,R.raw.alarmsong);
        stop=(Button) findViewById(R.id.stop);
        letsgo.setText("LET'S GOOO!!!!");
        stop.setText("Stop the Alarm");
        stop.setOnClickListener(this);
        dlocation = new Location(" ");
        ulocation = new Location(" ");
        seekbartextview = (TextView) findViewById(R.id.seekbartextview);
        seekbartextview.setText("250 Metres");
        radiuseekbar = (SeekBar) findViewById(R.id.radiusseekbar);
        radiuseekbar.setMax(1250);
        radiuseekbar.setMin(250);
        radiuseekbar.setOnSeekBarChangeListener(this);
        alarmwidget = (GridLayout) findViewById(R.id.alarmwidget);
toast = new Toast(this);

        try {
            init();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
        @RequiresApi(api = Build.VERSION_CODES.M)
        void userloc() {
         locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
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
                                Toast.makeText(getApplicationContext(), "In if Condition", Toast.LENGTH_SHORT).show();
                                userlocation.remove();
                                userlocation=mMap.addMarker(new MarkerOptions().position(latLng).title("Your Location " + localityName + "," + countryName).icon(BitmapDescriptorFactory.defaultMarker()));
                                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                                userlocation.setTag(0);
                                ulocation.setLatitude(latLng.latitude);
                                ulocation.setLatitude(latLng.longitude);
                                ulatlng = latLng;

                            } else {
                                userlocation=mMap.addMarker(new MarkerOptions().position(latLng).title("Your Location " + localityName + "," + countryName).icon(BitmapDescriptorFactory.defaultMarker()));
                                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                                userlocation.setTag(0);
                                ulocation.setLatitude(latLng.latitude);
                                ulocation.setLatitude(latLng.longitude);
                                ulatlng = latLng;
                                // mMap.addCircle(new CircleOptions().center(latLng).radius(500.56).strokeColor(Color.BLUE));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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
                                userlocation.remove();
                                userlocation=mMap.addMarker(new MarkerOptions().position(latLng).title("Your Location " + localityName + "," + countryName).icon(BitmapDescriptorFactory.defaultMarker()));
                                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                                userlocation.setTag(0);
                                ulocation.setLatitude(latLng.latitude);
                                ulocation.setLatitude(latLng.longitude);
                                ulatlng = latLng;
                            } else {
                                userlocation=mMap.addMarker(new MarkerOptions().position(latLng).title("Your Location " + localityName + "," + countryName).icon(BitmapDescriptorFactory.defaultMarker()));
                                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                                userlocation.setTag(0);
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

    @Override
   public void onClick(View v) {
        if(v.getId()==R.id.close){
            autoCompleteTextView.setText(" ");
        }
        else if(v.getId()==R.id.letsgo){
            alarmwidget.setVisibility(View.INVISIBLE);
            letsgo.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(),"Let's Go!!",Toast.LENGTH_SHORT).show();
            Intent intentbs = new Intent(this,GPSBackgroundService.class);
            Bundle bundle = new Bundle();
            bundle.putDouble("latitude",dlocation.getLatitude());
            bundle.putDouble("longitude",dlocation.getLongitude());
           // bundle.putString("ssd",ssd);
            //bundle.putDouble("longitude",dlong);
            bundle.putDouble("Radius",radius);
            intentbs.putExtras(bundle);
            GPSBackgroundService gpsBackgroundService = new GPSBackgroundService();
            gpsBackgroundService.viewstop(stop);
            gpsBackgroundService.viewaw(alarmwidget);
            //ContextCompat.startForegroundService(this,intentbs);
            startService(intentbs);
        }
        else if(v.getId()==R.id.stop){
            if(mediaPlayer.isPlaying()){
                mediaPlayer.stop();
                stop.setVisibility(View.INVISIBLE);
                letsgo.setVisibility(View.VISIBLE);
            }
        }
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mmap = googleMap;
        // Add a marker in Sydney and move the camera
       /* LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        */
       mMap.setOnMapLongClickListener(this);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void init() throws ExecutionException, InterruptedException {
        placename = autoCompleteTextView.toString();
        Log.d(TAG, "init:initializing");
        if (placename == " ")
        {
            useraccess=false;
        }
        else
        {
            useraccess=true;

        }

     //   autoCompleteTextView.setText(placename);
        autoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH
                        || i == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
                    //execute our method for searching
                    try {
                        geoLocate();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                return false;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void geoLocate() throws IOException {
        Log.d(TAG, "geoLocate.geolocating");
       String searchString = autoCompleteTextView.getText().toString();
       ssd = searchString;
        geocoder = new Geocoder(this);
        List<Address> list = new ArrayList<>();
        try {
               list = geocoder.getFromLocationName(searchString,1);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
//        Address address = list.get(0);
       // String a = String.valueOf(list.get(0));
        LatLng latlng;
        if(list.size()>0){
          Address address = list.get(0);
          //dlat = address.getLatitude();
          //dlong = address.getLongitude();
          latlng= new LatLng(address.getLatitude(),address.getLongitude());
            if(destinationcheck==false) {
                destination = mmap.addMarker(new MarkerOptions().position(latlng).title("Your Destination: " + searchString));
                mmap.animateCamera(CameraUpdateFactory.newLatLng(latlng));
                destination.setTag(0);
                destinationcheck=true;
                dlatlng = latlng;
                finallatlng = latlng;
                autoCompleteTextView.setText(" ");
                if(destinationcircle!=null){
                    destinationcircle.remove();
                }
                destinationcircle = mmap.addCircle(new CircleOptions().center(latlng).radius(radius));
                if(markerpressthere==true)
                {
                    markerlongpres.remove();
                }
                dlocation.setLatitude(latlng.latitude);
                dlocation.setLongitude(latlng.longitude);
            }
            else{
                destination.remove();
                destination = mmap.addMarker(new MarkerOptions().position(latlng).title("Your Destination: " + searchString));
                mmap.animateCamera(CameraUpdateFactory.newLatLng(latlng));
                destination.setTag(0);
                autoCompleteTextView.setText(" ");
                if(destinationcircle!=null){
                    destinationcircle.remove();
                }
               destinationcircle = mmap.addCircle(new CircleOptions().center(latlng).radius(radius));
                if(markerpressthere==true)
                {
                    markerlongpres.remove();
                }
                dlocation.setLatitude(latlng.latitude);
                dlocation.setLongitude(latlng.longitude);
                finallatlng = latlng;
            }
            letsgo.setVisibility(View.VISIBLE);
            alarmwidget.setVisibility(View.VISIBLE);
        }

        Toast.makeText(getApplicationContext(),"LET'S GOO!!!",Toast.LENGTH_LONG);
        userloc();
        //Toast.makeText(getApplicationContext(), address.getLatitude() + " " + address.getLongitude(), Toast.LENGTH_LONG);


    }
    @Override
    public boolean onLongClick(View v) {
        v.performHapticFeedback(1);
        return false;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapLongClick(LatLng latLng1) {
        alarmwidget.setVisibility(View.VISIBLE);
        List<Address> addressList = null;
      //  Toast.makeText(getApplicationContext(),""+latLng1.latitude,Toast.LENGTH_SHORT).show();
        try {
            addressList = geocoder.getFromLocation(latLng1.latitude, latLng1.longitude, 5);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(markerpressthere)
        {
            markerlongpres.remove();
        }
        /*String localityName = addressList.get(0).getSubLocality() + " ";
        String countryName = addressList.get(0).getCountryName();*/
        markerlongpres = mMap.addMarker(new MarkerOptions().position(latLng1).title("Your destination"));
        if(destinationcircle!=null){
            destinationcircle.remove();
        }
        destinationcircle = mMap.addCircle(new CircleOptions().center(latLng1).radius(radius).visible(true));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng1));
        markerlongpres.setTag(0);
        dlatlng = latLng1;
        letsgo.setVisibility(View.VISIBLE);
        userloc();
        finallatlng = latLng1;
        markerpressthere=true;

        if(destinationcheck==true){
            destination.remove();
            dlocation.setLatitude(latLng1.latitude);
            dlocation.setLongitude(latLng1.longitude);
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    void alarm(){
            if(ulocation.distanceTo(dlocation)<=radius){
                   //letsgo.setVisibility(View.INVISIBLE);
                stop.setVisibility(View.VISIBLE);
                alarmwidget.setVisibility(View.VISIBLE);
                   mediaPlayer.start();
            }
    }
int progress;
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            progress = radiuseekbar.getProgress();
            radius = progress;
            seekbartextview.setText(progress+" Meters");
            fluid();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        progress = radiuseekbar.getProgress();
        radius = progress;
        seekbartextview.setText(progress+" Meters");
        fluid();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        progress = radiuseekbar.getProgress();
        radius = progress;
        seekbartextview.setText(progress+" Meters");
        fluid();
    }
    public void fluid(){
        if(destinationcircle!=null){
            destinationcircle.remove();
        }
        destinationcircle = mMap.addCircle(new CircleOptions().center(finallatlng).radius(radius));
    }
 public void reached(boolean stopbw,boolean alabw){
   //  Toast.makeText(this,"oray",Toast.LENGTH_SHORT);

        if(stopbw == true && alabw == true){
            stop = (Button) findViewById(R.id.stop);
            alarmwidget = (GridLayout) findViewById(R.id.alarmwidget);
            stop.setVisibility(View.VISIBLE);
            alarmwidget.setVisibility(View.VISIBLE);
        }
 }

}


