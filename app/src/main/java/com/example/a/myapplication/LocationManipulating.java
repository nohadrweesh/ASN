package com.example.a.myapplication;

/**
 * Created by Speed on 22/03/2018.
 */

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

/**
 * Created by Speed on 22/03/2018.
 */

/**
 * Created by Speed on 13/10/2017.
 */

class LocationManipulating {
    private static final String TAG = "LocationManipulating";
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static LocationObject retObject=new LocationObject();
    //private  LocationObject locationObject;;
    private Context mContext;
    //private Activity mActivity;



    public LocationManipulating(Context context) {
        mContext = context;
    }


    public LocationObject getLocation(){


        mFusedLocationProviderClient=new FusedLocationProviderClient(mContext);
        if(mFusedLocationProviderClient==null)
            Log.d(TAG, "getLocation: is null");
        else
            Log.d(TAG, "getLocation: not null");


        int hasLocationPermission= ContextCompat.checkSelfPermission(mContext,ACCESS_COARSE_LOCATION);
        if(hasLocationPermission== PackageManager.PERMISSION_GRANTED){
            mFusedLocationProviderClient.getLastLocation().addOnSuccessListener((Activity) mContext, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location != null) {
                        Toast.makeText(mContext.getApplicationContext(), "your location is set to "+location.toString(), Toast.LENGTH_LONG).show();

                        double latitude=location.getLatitude();
                        double longitude=location.getLongitude();
                        double altitude=location.getAltitude();
                        retObject.setLongitude(longitude);
                        retObject.setLatitude(latitude);
                        retObject.setAltitude(altitude);
                        Log.d(TAG, "addItem:before return place long @ "+retObject.getLongitude()+" lat @"+retObject.getLatitude());

                    }

                }
            });
            Log.d(TAG, "addItem:before else place long @ "+retObject.getLongitude()+" lat @"+retObject.getLatitude());

        }else{
            Toast.makeText(mContext,"Accept Permission",Toast.LENGTH_LONG).show();

        }
        Log.d(TAG, "addItem:return place long @ "+retObject.getLongitude()+" lat @"+retObject.getLatitude());

        return  retObject;
    }
//    private boolean checkGettingLocationPermission(){
//        Log.d(TAG, "checkGettingLocationPermission: starts");
//        String permission ="android.permission.ACCESS_COARSE_LOCATION";
//        int res=mContext.getApplicationContext().checkCallingOrSelfPermission(permission);
//
//        if (res== PackageManager.PERMISSION_GRANTED){
//            Log.d(TAG, "checkGettingLocationPermission: ends with res :"+res);
//            return true;
//        }else{
//            ActivityCompat
//                    .requestPermissions((Activity) mContext, new String[]{permission}, REQUEST_CODE_ASK_PERMISSIONS);
//            res=mContext.getApplicationContext().checkCallingOrSelfPermission(permission);
//            Log.d(TAG, "checkGettingLocationPermission: ends with res :"+res);
//            return (res== PackageManager.PERMISSION_GRANTED);
//        }
//    }
}
