package com.example.a.myapplication;





import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignIn extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SignIn";

    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, MainActivity.class));
            return;
        }

        editTextEmail = (EditText) findViewById(R.id.editTextUsernameid);
        editTextPassword = (EditText) findViewById(R.id.editeTextEmailid);
        buttonLogin = (Button) findViewById(R.id.login);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        buttonLogin.setOnClickListener(this);

    }

    private void userLogin(){
        Log.d(TAG, "userLogin: starts");
        final String userEmail = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        progressDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: starts");
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.d(TAG, "onResponse: "+response);
                            Toast.makeText(getApplicationContext(),"response is "+response,Toast.LENGTH_LONG).show();
                            if(!obj.getBoolean("error")){
                                SharedPrefManager.getInstance(getApplicationContext())
                                        .userLogin(

                                                obj.getString("email"),
                                                obj.getString("password")
                                        );
                                SharedPrefManager.getInstance(getApplicationContext())
                                        .setUserInfo(
                                                obj.getInt("ID"),
                                                obj.getInt("carID")
                                        );
                                Intent intent=new Intent(getApplicationContext(), DriverMapActivity.class);
                                Driver driver=new Driver(obj.getInt("ID"),obj.getString("email"));
                                intent.putExtra("driver",driver);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(
                                        getApplicationContext(),
                                        obj.getString("message"),
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        } catch (JSONException e) {
                            Log.d(TAG, "onResponse: error"+response);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: starts");
                        progressDialog.dismiss();

                        Toast.makeText(
                                getApplicationContext(),
                                error.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.d(TAG, "getParams: starts with "+userEmail+" , "+password);
                Map<String, String> params = new HashMap<>();
                params.put("email", userEmail);
                params.put("password", password);
                params.put("token",String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getToken()));
                return params;
            }

        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onClick(View view) {
        if(view == buttonLogin){
            Log.d(TAG, "onClick: clicked login");
            userLogin();
        }
    }
}
