package com.example.a.myapplication;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        displayCars();
    }

    public void displayCars()
    {
        ProfileActivity pa=new ProfileActivity();

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        //display driver car

        LocationManipulating lm = new LocationManipulating(getApplicationContext());
        LocationObject driverPosition=lm.getLocation();

        Log.d("MapsActivity: ","driver position is "+driverPosition.getLatitude()+" "+driverPosition.getLongitude());
        LatLng driverxyPosition = new LatLng(30,10);
        Toast.makeText(this,String.valueOf(driverxyPosition==null),Toast.LENGTH_SHORT).show();
        mMap.addMarker(new MarkerOptions().position(driverxyPosition));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(driverxyPosition));

        //display neighbour cars
//        List<Car> neighbourCars=pa.getNeighbours();
//
//        for (Car car:neighbourCars)
//        {
//            LocationObject neighbourCarPosition = car.getPosition();
//            LatLng xyPosition=new LatLng(neighbourCarPosition.getLongitude(),neighbourCarPosition.getLatitude());
//            mMap.addMarker(new MarkerOptions().position(xyPosition));
//        }

    }

}
