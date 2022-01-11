package com.example.gps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Telephony;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {
    // check permission
    TextView lat;
    TextView lon;
    TextView address;
    LocationManager location;
    TextView distance;
    TextView time;
    double sum = 0;
    private Location old;
    private long t = SystemClock.elapsedRealtime();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lat = findViewById(R.id.lat);
        lon = findViewById(R.id.lon);
        address = findViewById(R.id.address);
        location = (LocationManager) getSystemService(LOCATION_SERVICE);
        distance = findViewById(R.id.distance);
        time = findViewById(R.id.time);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //  ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1);
            return;
        }
        location.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, this);
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
            switch(requestCode){
                case 1:
                    if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                        location.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, this);
                    break;
                default:
                    break;
            }
    }
    @Override
    public void onLocationChanged(@NonNull Location location) {
        double la = location.getLatitude();
        double lo = location.getLongitude();
        lat.setText(la+"");
        lon.setText(lo+"");
        List<Address> a = null;
        long tafter = SystemClock.elapsedRealtime();
        Geocoder geocoder = new Geocoder(this,Locale.US);
        try {
            a = geocoder.getFromLocation(la,lo,1);
            Log.d("Tag2",a.toString());
            address.setText(a.get(0).getAddressLine(0));
        } catch (IOException e) {
            Log.d("TAG2",e.toString());
            e.printStackTrace();
        }
        if(old!=null){
            sum+= location.distanceTo(old)/1609.34;
            DecimalFormat twoDForm = new DecimalFormat("##.##");
            sum = Double.valueOf(twoDForm.format(sum));
            distance.setText(sum+" miles");
            time.setText((tafter-t)/1000 + " s");
        }
        old = location;


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
}