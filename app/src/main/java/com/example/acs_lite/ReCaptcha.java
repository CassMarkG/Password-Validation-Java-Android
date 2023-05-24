package com.example.acs_lite;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ReCaptcha extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks {
    CheckBox verifyIt;

    GoogleApiClient googleApiClient;
    final String SiteKey = "6LeDICkmAAAAAMtVST33yVGD4AnqMJI1zkqOuV8O";
    final String SecretKey = "6LeDICkmAAAAAAZKLH1ZkLWvnbD7HVTPBsD0LGs_";
    String TAG = "ReCaptcha";
    RequestQueue queue;
    //String userResponseToken;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_captcha);

        verifyIt = findViewById(R.id.verify);
//        verifyIt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                connect();
//            }
//        });
        verifyIt.setOnClickListener(view -> SafetyNet.SafetyNetApi.verifyWithRecaptcha(googleApiClient,SiteKey)
                .setResultCallback(recaptchaTokenResult -> {
                    Status status = recaptchaTokenResult.getStatus();
                    if ((status !=null) && status.isSuccess()){
                        Toast.makeText(ReCaptcha.this,"Successfully Verified",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(ReCaptcha.this,"Unsuccessfully Verified",Toast.LENGTH_LONG).show();
                    }
                }));

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(SafetyNet.API)
                .addConnectionCallbacks(ReCaptcha.this)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public void connect(){
        SafetyNet.getClient(this).verifyWithRecaptcha(SiteKey)
                .addOnSuccessListener(this, recaptchaTokenResponse -> {
                    String userResponseToken = recaptchaTokenResponse.getTokenResult();
                    if (!userResponseToken.isEmpty()) {
                        handleSiteVerify(recaptchaTokenResponse.getTokenResult());
                    }
                })
                .addOnFailureListener(this, e -> {
                    if (e instanceof ApiException) {
                        ApiException apiException = (ApiException) e;
                        Log.d(TAG, "Error message: " +
                                CommonStatusCodes.getStatusCodeString(apiException.getStatusCode()));
                    } else {
                        Log.d(TAG, "Unknown type of error: " + e.getMessage());
                    }
                });
    }

    protected  void handleSiteVerify(final String responseToken){
        //it is google recaptcha siteverify server
        //you can place your server url
        String url = "https://www.google.com/recaptcha/api/siteverify";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if(jsonObject.getBoolean("success")){
                            Toast.makeText(ReCaptcha.this,"Successfully Verified",Toast.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(),String.valueOf(jsonObject.getBoolean("success")),Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(ReCaptcha.this,"Unsuccessfully Verified",Toast.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(), jsonObject.getString("error-codes"),Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception ex) {
                        Log.d(TAG, "JSON exception: " + ex.getMessage());

                    }
                },
                error -> Log.d(TAG, "Error message: " + error.getMessage())) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("secret", SecretKey);
                params.put("response", responseToken);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}