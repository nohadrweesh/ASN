package com.example.a.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class DriverMapActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLocation;
    LocationRequest mLocationRequest;
    Marker marker;
    List<Marker> neighboursMarkers;

    BitmapDescriptor myCarDescriptor;
    BitmapDescriptor otherCarsDescriptors;
    BitmapDescriptor pbCarsDescriptors;


    LocationUtils mLocationUtils;
    NeighborsUtils mNeighborsUtils;

    HelpUtils mHelpUtils;

    private static final String TAG = "DriverMapActivity";
    double pbLat=-1,pbLong=-1;
    String pbName;

    int currentDriverID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mLocationUtils=LocationUtils.getInstance(this);
        neighboursMarkers=new ArrayList<Marker>();

        mNeighborsUtils=NeighborsUtils.getInstance(this);
        neighboursMarkers=new ArrayList<Marker>();

        myCarDescriptor=bitmapDescriptorFromVector(this, R.drawable.my_car);
        otherCarsDescriptors=bitmapDescriptorFromVector(this, R.drawable.other_car);
        pbCarsDescriptors=bitmapDescriptorFromVector(this, R.drawable.pb_car);

        Intent i=getIntent();
        if(i!=null && i.getExtras()!=null){
            Toast.makeText(this,"i received with data ",Toast.LENGTH_LONG).show();
            pbLat=i.getDoubleExtra("lat",-1);
            pbLong=i.getDoubleExtra("long",-1);
            if(i.hasExtra("name"))
                pbName=i.getStringExtra("name");
            if(i.hasExtra("driverID"))
                currentDriverID = i.getIntExtra("driverID",0);
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
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            Toast.makeText(getApplicationContext(),"Accept permssions ",Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(TAG, "onMapReady: starts ");
        buildGoogleApiClient();
        mGoogleApiClient.connect();
        mMap.setMyLocationEnabled(true);

        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
        if(pbLong!=-1&&pbLat!=-1){
            Toast.makeText(getApplicationContext(),"pbLat !=-1 ",Toast.LENGTH_SHORT).show();
            LatLng pbPerson = new LatLng(pbLat,pbLong);
            mMap.addMarker(new MarkerOptions().position(pbPerson).title(pbName).snippet("I have a problemb")
                    .icon(pbCarsDescriptors)
                    .anchor(0.5f,0.5f));

        }else{
            Toast.makeText(getApplicationContext(),"pbLat ===-1 ",Toast.LENGTH_SHORT).show();
        }
    }

    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient=new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
    @Override
    public void onLocationChanged(Location location) {
        // Log.d(TAG, "onLocationChanged: starts with "+location.toString());
        mMap.clear();


        mLocation=location;
        mLocation.setLatitude(round(location.getLatitude(),3));
        mLocation.setLongitude(round(location.getLongitude(),3));

        LatLng latLng=new LatLng(round(location.getLatitude(),3),round(location.getLongitude(),3));
        //LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
        Log.d(TAG, "onLocationChanged: starts with  latLang "+latLng.toString());




        mMap.addMarker(new MarkerOptions().position(latLng).title("My Car ^__^").snippet("I'm Noha ,a software engineer from Egypt")
                .icon(myCarDescriptor)
                .anchor(0.5f,0.5f));
        if(pbLong!=-1&&pbLat!=-1){
            Toast.makeText(getApplicationContext(),"pbLat !=-1 ",Toast.LENGTH_SHORT).show();
            LatLng pbPerson = new LatLng(pbLat,pbLong);
            mMap.addMarker(new MarkerOptions().position(pbPerson).title(pbName).snippet("I have a problem")
                    .icon(pbCarsDescriptors)
                    .anchor(0.5f,0.5f));

        }else{
            Toast.makeText(getApplicationContext(),"pbLat ===-1 ",Toast.LENGTH_SHORT).show();
        }

        /*if(marker==null) {
            marker = mMap.addMarker(new MarkerOptions().position(latLng).title("My Car ^__^").snippet("I'm Noha ,a software engineer from Egypt")
                    .icon(myCarDescriptor)
                    .anchor(0.5f,0.5f));
        }else{
            marker.setPosition(latLng);
        }*/

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));



        mLocationUtils.setLocation(mLocation);
        mNeighborsUtils.getNeighbours(mLocation);
        neighboursMarkers.clear();
        int markersSize=neighboursMarkers.size();
        for(int i=0;i<mNeighborsUtils.locations.size();i++){

            LatLng latLng1=new LatLng(mNeighborsUtils.locations.get(i).getLatitude(),mNeighborsUtils.locations.get(i).getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .position(latLng1)
                    .title(mNeighborsUtils.Names.get(i))
                    .snippet("other cars with id " + String.valueOf(mNeighborsUtils.IDs.get(i)))
                    .icon(otherCarsDescriptors));
            /*if(i<markersSize){
                if(neighboursMarkers.get(i)==null) {
                    neighboursMarkers.add(mMap.addMarker(new MarkerOptions()
                            .position(latLng1)
                            .title(mNeighborsUtils.Names.get(i))
                            .snippet("other cars with id " + String.valueOf(mNeighborsUtils.IDs.get(i)))
                            .icon(otherCarsDescriptors)));
                }else{
                    neighboursMarkers.get(i).setPosition(latLng1);
                }
            }else{
                neighboursMarkers.add(mMap.addMarker(new MarkerOptions()
                        .position(latLng1)
                        .title(mNeighborsUtils.Names.get(i))
                        .snippet("other cars with id " + String.valueOf(mNeighborsUtils.IDs.get(i)))
                        .icon(otherCarsDescriptors)));
            }*/

        }


    }




    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected: starts");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            Toast.makeText(getApplicationContext(),"Accept permssions ",Toast.LENGTH_SHORT).show();
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended: starts");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.d(TAG, "onConnectionFailed: starts");
    }


    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId){
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(40, 20, vectorDrawable.getIntrinsicWidth() , vectorDrawable.getIntrinsicHeight() );
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
    private  double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: ");
        getMenuInflater().inflate(R.menu.map_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.map_logout) {
            Log.d(TAG, "onOptionsItemSelected: ");
            LogoutUtils mLogoutUtils = LogoutUtils.getInstance(this);
            mLogoutUtils.logout();
            startActivity(new Intent(this, MainActivity.class));

            return true;
        } else if (id == R.id.map_settings){

            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            return true;
        }else if(id==R.id.map_OBD){
            startActivity(new Intent(this,ObdActivity.class));
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void help(View view){
        startActivity(new Intent(this,HelpActivity.class));

    }

    public void urgentHelp(View view){
        mHelpUtils=HelpUtils.getInstance(this);
        mHelpUtils.help("URGENT","The driver has a serious problem which we have not configure yet" +
                ",please help him/her if you can...","not specified");
        Toast.makeText(this,"We have sent your request ,Your neighbours will help you ASAP ,please don't panic and stop the car" +
                "if you can.If you coud provide us with more info that'll be great  ",Toast.LENGTH_LONG).show();



    }
    public  Location getLocation(){
        return  mLocation;
    }

    public void openOBDPanel(View view) {
        startActivity(new Intent(this,ObdActivity.class));
    }

    public void displayConnectionRequests(View v)
    {
        Intent i= new Intent(getApplicationContext(),ConnectionRequestsActivity.class);
        Log.d("DriverMapActivity","displayConnectionRequests current user is "+ currentDriverID);
        Log.d("DriverMapActivity","displayConnectionRequests current user is "+ SharedPrefManager.getInstance(getApplicationContext()).getUserId() );
//        i.putExtra("currentDriver",currentDriver);
        i.putExtra("currentDriverID",SharedPrefManager.getInstance(getApplicationContext()).getUserId());
        startActivity(i);
    }

    public void goToTrackersActivity(View v)
    {
        Intent i = new Intent(getApplicationContext(),TrackersListActivity.class);
        Log.d("DriverMapActivity","goToTrackersActivity current user id is "+ SharedPrefManager.getInstance(getApplicationContext()).getUserId());
        i.putExtra("currentUserID",SharedPrefManager.getInstance(getApplicationContext()).getUserId());
        startActivity(i);
    }
}
