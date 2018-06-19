package com.yantramicrosystems.www.projectd;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@SuppressLint("Registered")
public class LocationService extends Service implements




        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final long INTERVAL = 1000 * 2;
    private static final long FASTEST_INTERVAL = 1000 * 1;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation, lStart, lEnd;
    static float distance = 0;
    float speed;
    double latitude,longitude;
    final String Tag     = "TagProD";


    private final IBinder mBinder = new LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
        return mBinder;
    }

    protected void createLocationRequest() {
        Log.e(Tag, "LM-createlocaton request");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onConnected(Bundle bundle) {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        } catch (SecurityException e) {
        }
    }


    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        distance = 0;
    }


    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onLocationChanged(Location location) {
     //   Log.e(Tag, "LM-Location changed");
      //  MainScreen.locate.dismiss();
        mCurrentLocation = location;
        if (lStart == null) {
            lStart = mCurrentLocation;
            lEnd = mCurrentLocation;
        } else
            lEnd = mCurrentLocation;

        latitude = location.getLatitude();
        longitude = location.getLongitude();

        //Calling the method below updates the  live values of distance and speed to the TextViews.
        updateUI();
        //calculating the speed with getSpeed method it returns speed in m/s so we are converting it into kmph
        speed = location.getSpeed() * 18 / 5;

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public class LocalBinder extends Binder {

        public LocationService getService() {
            return LocationService.this;
        }
    }

    //The live feed of Distance and Speed are being set in the method below .
    @SuppressLint("SetTextI18n")
    private void updateUI() {

        Log.e(Tag, "LM-updateUI");


        if (MainScreen.GPRSp == 0) {
            distance = distance + (float) (lStart.distanceTo(lEnd) / 1000.00);
            MainScreen.GPRSendTime = System.currentTimeMillis();
            long diff = MainScreen.GPRSendTime - MainScreen.GPRSstartTime;
            diff = TimeUnit.MILLISECONDS.toMinutes(diff);
         //   MainScreen.time.setText("Time: " + diff + " minutes");
            if (speed > 0.0)
                MainScreen.fVal_SPEED_GPS=speed;//  MainScreen.speed.setText("" + new DecimalFormat("#.##").format(speed) + " km/hr");
            else
                MainScreen.fVal_SPEED_GPS=0;

            float fTemp = distance;
            fTemp = fTemp*10;
            fTemp = (int)fTemp;
            fTemp = fTemp/10;
            MainScreen.fValGPSDistance=fTemp;//dist.setText(new DecimalFormat("#.###").format(distance) + " Km's.");

            fTemp = (int)speed;
            MainScreen.fVal_SPEED_GPS=fTemp;

            MainScreen.sValLocation=(getLocalityName(latitude,longitude));

          //  Log.e(Tag, "LM-updateUI Speed : " + speed);
         //   MainScreen.tvCurrentLocation.setText(String.valueOf(speed));
            lStart = lEnd;

        }

    }


    @Override
    public boolean onUnbind(Intent intent) {
        stopLocationUpdates();
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
        lStart = null;
        lEnd = null;
        distance = 0;
        return super.onUnbind(intent);
    }

    public String getLocalityName(double latitude, double longitude) {
        String locality = null;
        Geocoder gcd = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null && addresses.size() > 0) {
            locality = addresses.get(0).getSubLocality();

        }

        return locality;
    }


}