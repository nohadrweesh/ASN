package com.example.a.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    GoogleMap mGoogleMap;
    LocationManager locationManager;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    LocationObject driverLocation;
    Car driverCar;
    Driver driver;
    Marker mCurrLocationMarker;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MapsActivity: "," entered onCreate ");

        super.onCreate(savedInstanceState);
        driver = (Driver)getIntent().getSerializableExtra("driver");
        driverCar=new Car(driver.getCarID(),driver.getDriverID());


        Log.d("MapsActivity:","driver id "+driver.getDriverID()+" driver name "+driver.getDriverName()+" driver email "+driver.getDriverEmail());
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);




    }

    //when map is ready it executes onMapReady()
    //it calls google api to be able to use some of its services  we execute buildGoogleApiClient()
    //when google api client is connected to google it executes onConnected()
    // it starts to check for location updates

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                Log.d("MapsActivity: ","entered onMapReady 2nd if");
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                Log.d("MapsActivity: ","entered onMapReady 1st else");
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            Log.d("MapsActivity: ","entered onMapReady 2nd else");
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        Log.d("MapsActivity: ","entered buildGoogleApiClient ");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void onConnected( Bundle bundle)
    {
        Log.d("MapsActivity: ","entered onConnected ");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            Log.d("MapsActivity: ","entered onConnected  entered if condition ");
            //code for checking location updates
//            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//            Location lastKnownDriverLocation=locationManager.(LocationManager.NETWORK_PROVIDER);
//            displayCars(lastKnownDriverLocation);
            checkForUpdates();
        }
    }

    private void checkForUpdates()
    {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {
            Log.d("MapsActivity: "," entered checkForUpdates entered 1s if ");
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d("MapsActivity: "," entered onCreate entered 2nd if ");
                return;
            }
            Log.d("MapsActivity: "," entered checkForUpdates before locationManger.requestLocationUpdates");
            // check updates each 1000 millisecond, and min distance = 1 meter
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, new LocationListener() {
                @Override
                public void onLocationChanged(Location location)
                {
                    Log.d("MapsActivity","entered onLocationChanged NETWORK_PROVIDER");
                    executeOnLocationChanged(location);
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {}
                @Override
                public void onProviderEnabled(String s) {}
                @Override
                public void onProviderDisabled(String s) {}
            });
        }
        else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            Log.d("MapsActivity: ","entered GPS_PROVIDER ");
            // check updates each 1000 millisecond, and min distance = 1 meter
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, new LocationListener() {
                @Override
                public void onLocationChanged(Location location)
                {
                    Log.d("MapsActivity","entered onLocationChanged GPS_PROVIDER");
                    executeOnLocationChanged(location);}

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {}
                @Override
                public void onProviderEnabled(String s) {}
                @Override
                public void onProviderDisabled(String s) {}
            });
        }
    }

    private void executeOnLocationChanged(Location location)
    {
        Log.d("MapsActivity: ","entered executeOnLocationChanged ");
        displayCars(location);

        Log.d("MapsActivity: ","entered onLocationChanged ");
    }

    public void displayCars(Location location)
    {
        Log.d("MapsActivity","entered displayCars"+"Long "+location.getLongitude()+"lat "+location.getLatitude());
        driverLocation = new LocationObject(location.getLongitude(),location.getLatitude(),location.getAltitude());
//        driverCar.setLocation(this,driverLocation,driver);
        //TODO: uncomment to start storing locations into database
        //driverCar.setLocation(this,driverLocation,driver);

        mLastLocation = location;
        if (mCurrLocationMarker != null)
        {mCurrLocationMarker.remove();}

        //display driver's marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        //move map camera
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Me");
        markerOptions.snippet("@"+driver.getDriverName()+driver.getCarID());
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

        //display neighbouring cars

        ProfileActivity pa=new ProfileActivity();
        List<Car> neighbouringCars=driverCar.getNeighbours(this,driverCar.getDriverID(),driverLocation);
        if (!neighbouringCars.isEmpty())
        {
            for (Car neighbour :neighbouringCars)
            {
                //display driver's marker
                LatLng latLng1 = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions1 = new MarkerOptions();
                markerOptions1.position(latLng1);
                markerOptions1.title("Neighbour");
//                markerOptions1.snippet("@"+neighbour.getDriverName()+driver.getCarID());
                markerOptions1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                mCurrLocationMarker = mGoogleMap.addMarker(markerOptions1);
            }
        }
        else
        {
            Log.d("MapsActivity","entered displayCars: no neighbours found");
        }

    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.d("MapsActivity: ","entered checkLocationPermission 2nd if ");
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                Log.d("MapsActivity: ","entered checkLocationPermission else ");
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                Log.d("MapsActivity: ","entered onRequestPermissionsResult ");
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }




    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
