package com.example.whereami;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    LocationManager locationManager;
    LocationListener locationListener;
    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1=(Button)findViewById(R.id.btnMap);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(MainActivity.this,ShowonMap.class);
                startActivity(in);
            }
        });
        locationManager=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
//                Log.i("LOCATION",location.toString());
                updateLocation(location);
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
        };
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else{
            TextView Latitude=(TextView)findViewById(R.id.txtLatitude);
            TextView Longitude=(TextView)findViewById(R.id.txtLongitude);
            TextView Altitude=(TextView)findViewById(R.id.txtAltitude);
            TextView Locality=(TextView)findViewById(R.id.txtLocality);
            Latitude.setText("Latitude: ");
            Longitude.setText("Longitude: ");
            Altitude.setText("Altitude: ");
            String address="Address: Searching... \n\nThis might take some time depending on your network";
            Locality.setText(address);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,0,locationListener);
            Location lastKnownLocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastKnownLocation!=null) {
                updateLocation(lastKnownLocation);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
                TextView Latitude=(TextView)findViewById(R.id.txtLatitude);
                TextView Longitude=(TextView)findViewById(R.id.txtLongitude);
                TextView Altitude=(TextView)findViewById(R.id.txtAltitude);
                TextView Locality=(TextView)findViewById(R.id.txtLocality);
                Latitude.setText("Latitude: ");
                Longitude.setText("Longitude: ");
                Altitude.setText("Altitude: ");
                String address="Address: Searching... \nThis might take some time depending on your network";
                Locality.setText(address);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, locationListener);
            }
        }
    }

    public void updateLocation(Location location) {
        Log.i("LOCATION UPDATED",location.toString());
        TextView Latitude=(TextView)findViewById(R.id.txtLatitude);
        TextView Longitude=(TextView)findViewById(R.id.txtLongitude);
        TextView Altitude=(TextView)findViewById(R.id.txtAltitude);
        TextView Locality=(TextView)findViewById(R.id.txtLocality);
        Latitude.setText("Latitude: "+Double.toString(location.getLatitude()));
        Longitude.setText("Longitude: "+Double.toString(location.getLongitude()));
        Altitude.setText("Altitude: "+Double.toString(location.getAltitude()));
        String address="Address: Could Not Find Address";
        Geocoder geocoder=new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            if(addressList !=null && addressList.size()>0){
                address="Address: \n";
                if(addressList.get(0).getThoroughfare()!=null)
                    address+=addressList.get(0).getThoroughfare()+"\n";
                if(addressList.get(0).getLocality()!=null)
                    address+=addressList.get(0).getLocality()+"\n";
                if(addressList.get(0).getAdminArea()!=null)
                    address+=addressList.get(0).getAdminArea()+"\n";
                if(addressList.get(0).getCountryName()!=null)
                    address+=addressList.get(0).getCountryName();
            }
            Locality.setText(address);

        } catch (Exception e) {
            e.printStackTrace();
            Locality.setText(address);
        }

    }
}