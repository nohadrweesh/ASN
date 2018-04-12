package com.example.a.myapplication;

//import com.google.android.gms.maps.model.LatLng;

/**
 * Created by user on 05/04/2018.
 */

public class Car {

    LocationObject position;
    private String carID;
    private int driverID;

    public Car(LocationObject pos, int dID) {
        this.position = pos;
        this.driverID = dID;
    }

    public Car(LocationObject pos, String cID, int dID) {
        this.position = pos;
        this.carID = cID;
        this.driverID = dID;
    }

    public LocationObject getPosition() {return this.getPosition();}


    public String getCarID() {return carID;}
    public void setCarID(String carID) {this.carID = carID;}

    public int getDriverID() {return driverID;}
    public void setDriverID(int driverID) {this.driverID = driverID;}

    //    public void setInformation()
//    {
//        int userID=SharedPrefManager.getInstance(this).getUserId();
//        driver.setID(userID);
//        carID=SharedPrefManager.getInstance(getApplicationContext()).getCarId();
//    }
//
//    private void setLocation(final LocationObject locationObject){
//
//        latitude=locationObject.getLatitude();
//        longitude=locationObject.getLongitude();
//        altitude=locationObject.getAltitude();
//
//        StringRequest stringRequest = new StringRequest(
//                Request.Method.POST,
//                Constants.LOCATION_SET,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject obj = new JSONObject(response);
//                            if(!obj.getBoolean("error"))
//                            {
//                            }
//                            else
//                            {
//
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error)
//                    {
//
//                    }
//                }
//        ){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
//
//                Map<String, String> params = new HashMap<>();
//                params.put("latitude", String.valueOf(latitude));
//                params.put("longitude", String.valueOf(longitude));
//                params.put("altitude", String.valueOf(altitude));
//                params.put("userID", String.valueOf(driver.getID()));
//                params.put("carID", String.valueOf(carID));
//                params.put("locationTime", timeStamp);
//
//                return params;
//            }
//
//        };
//
//        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
//    }
//
//
//    public  List<Car> getNeighbours(View view)
//    {
//        List<Car> cars=new ArrayList<>();
//
//        LocationManipulating locationManipulating=new LocationManipulating(getApplicationContext());
//        LocationObject currentLocation=locationManipulating.getLocation();
//        setLocation(currentLocation);
//        cars = getNeighboursFromDb(driver.getID(),currentLocation);
//
//        return cars;
//    }
//
//    public List<Car> getNeighboursFromDb(int userID, final LocationObject curr)
//    {
//        //final JSONObject retJSON;
//        List<Car> cars=new ArrayList<>();
//        StringRequest stringRequest = new StringRequest(
//                Request.Method.POST,
//                Constants.URL_NEIGBOURS,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                        try {
//                            JSONObject obj = new JSONObject(response);
//                            //retJSON=new JSONObject(response);
//                            if(!obj.getBoolean("error"))
//                            {
//
//                                // finish();
//                            }
//                            else{
//
//                            }
//                        } catch (JSONException e) {
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//
//                    }
//                }
//        ){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
//
//                Map<String, String> params = new HashMap<>();
//                params.put("latitude", String.valueOf(curr.getLatitude()));
//                params.put("longitude", String.valueOf(curr.getLongitude()));
//                params.put("altitude", String.valueOf(curr.getAltitude()));
//                params.put("userID", String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getUserId()));
//                //params.put("carID", String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getCarId()));
//                //params.put("locationTime", timeStamp);
//
//                return params;
//            }
//
//        };
//
//        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
//        return cars;
//    }

}
