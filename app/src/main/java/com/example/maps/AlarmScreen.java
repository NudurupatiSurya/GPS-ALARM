package com.example.maps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static com.example.maps.GPSBackgroundService.mediaPlayer;

public class AlarmScreen extends AppCompatActivity implements View.OnClickListener {
//MediaPlayer mediaPlayer;
Button stopi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_screen);

      //  mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.alarmsong);
      //  mediaPlayer.start();
        stopi = (Button) findViewById(R.id.stopi);
        stopi.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.stopi){

                mediaPlayer.stop();
                Intent intent = new Intent(this,MapsActivity.class);
                startActivity(intent);


        }
    }
}
