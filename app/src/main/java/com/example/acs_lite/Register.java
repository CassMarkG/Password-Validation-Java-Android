package com.example.acs_lite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    TextInputEditText email,password,Cpassword;
    CheckBox cheki;
    CardView frameOne,frameTwo,frameThree,frameFour;
    Button signup;
    ImageView avatar,check1,check2,check3,check4,catchIt;
    TextView t1,t2,t3,signin,checkOne,checkTwo,checkThree,checkFour,passError,CpassError,emailErr,CpassError2;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    FirebaseUser mFirebaseUser;

    private boolean isAtLeast8 = false;
    private boolean isRegistrationClickable = false;
    private boolean hasUpperCase = false;
    private boolean hasNumber = false;
    private boolean hasSymbol = false;
    private boolean isClickable = false;
    final String SiteKey = "6LeDICkmAAAAAMtVST33yVGD4AnqMJI1zkqOuV8O";
    final String SecretKey = "6LeDICkmAAAAAAZKLH1ZkLWvnbD7HVTPBsD0LGs_";
    String TAG = "Main Activity";
    RequestQueue queue;
    String userResponseToken;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_screen);
        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.editText_register);
        password = findViewById(R.id.editText_password);
        Cpassword = findViewById(R.id.editText_confirmpassword);
        frameOne = findViewById(R.id.frame1);
        frameTwo = findViewById(R.id.frame2);
        frameThree = findViewById(R.id.frame3);
        frameFour = findViewById(R.id.frame4);
        signup = findViewById(R.id.signupbutton);
        signin = findViewById(R.id.signinTextview);
        avatar = findViewById(R.id.avatar);
        cheki = findViewById(R.id.checkBox);
        check1 = findViewById(R.id.imageView5);
        check2 = findViewById(R.id.imageView6);
        check3 = findViewById(R.id.imageView7);
        check4 = findViewById(R.id.imageView8);
        checkOne = findViewById(R.id.textView4);
        checkTwo = findViewById(R.id.textView11);
        checkThree = findViewById(R.id.textView12);
        checkFour = findViewById(R.id.textView13);
        catchIt = findViewById(R.id.imageView4);
        t1 = findViewById(R.id.textView);
        t2 = findViewById(R.id.textView2);
        t3 = findViewById(R.id.textView3);
        emailErr = findViewById(R.id.emailError);
        passError = findViewById(R.id.passwordError);
        CpassError = findViewById(R.id.cPasswordError);
        CpassError2 = findViewById(R.id.cPasswordError2);
        progressBar = findViewById(R.id.progressBar);

        queue = Volley.newRequestQueue(getApplicationContext());

        signup.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {

                //Obtain the entered data
                String Email = email.getText().toString().trim();
                String Password = password.getText().toString().trim();
                String Confirmpassword = Cpassword.getText().toString().trim();

                //proper logic
                if(Email.length() > 0 && Password.length() > 0 && Confirmpassword.length() > 0){
                    if (isRegistrationClickable){
                        Toast.makeText(Register.this, "Registered Successfully", Toast.LENGTH_LONG).show();
                        connect();
                    }
                }else{
                    if (Email.length() == 0){
                        emailErr.setVisibility(View.VISIBLE);
                    }
                    if (Password.length() == 0){
                        passError.setVisibility(View.VISIBLE);
                    }
                    if (Confirmpassword.length() == 0){
                        CpassError.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        inputChange();

    }

    @SuppressLint("ResourceType")
    private void registrationDataCheck(){
        //Obtain the entered data
        String Email = email.getText().toString();
        String Password = password.getText().toString();
        String Confirmpassword = Cpassword.getText().toString();

        checkEmpty(Email,Password,Confirmpassword);

        //Here we go with password algorithm
        //Number
        if(Password.matches("(.*[0-9].*)")){
            hasNumber = true;
            frameOne.setCardBackgroundColor(Color.parseColor(getString(R.color.Green)));
        }else{
            hasNumber = false;
            frameOne.setCardBackgroundColor(Color.parseColor(getString(R.color.White)));
        }
        //Uppercase
        if(Password.matches("(.*[A-Z].*)")){
            hasUpperCase = true;
            frameTwo.setCardBackgroundColor(Color.parseColor(getString(R.color.Green)));
        }else{
            hasUpperCase = false;
            frameTwo.setCardBackgroundColor(Color.parseColor(getString(R.color.White)));
        }
        //Length
        if(Password.length()>=8){
            isAtLeast8 = true;
            frameThree.setCardBackgroundColor(Color.parseColor(getString(R.color.Green)));
        }else{
            isAtLeast8 = false;
            frameThree.setCardBackgroundColor(Color.parseColor(getString(R.color.White)));
        }
        //Special Character
        if(Password.matches("^(?=.*[_.()$#%*?+=@]).*$")){
            hasSymbol = true;
            frameFour.setCardBackgroundColor(Color.parseColor(getString(R.color.Green)));
        }else{
            hasSymbol = false;
            frameFour.setCardBackgroundColor(Color.parseColor(getString(R.color.White)));
        }
        if (!Password.equals(Confirmpassword)){
            CpassError2.setVisibility(View.VISIBLE);
        }else{
            checkAllData(Email);
        }

    }

    @SuppressLint("ResourceType")
    private void checkAllData(String Email3) {
        if (cheki.isChecked() && hasNumber && hasUpperCase && isAtLeast8 && hasSymbol && Email3.length() > 0){
            isRegistrationClickable = true;
            signup.setBackgroundColor(Color.parseColor(getString(R.color.purple1)));
        }else{
            isRegistrationClickable = false;
            signup.setBackgroundColor(Color.parseColor(getString(R.color.black)));
        }
    }

    private void checkEmpty(String email2, String password2, String confirmpassword2) {
        if (email2.length() > 0 && emailErr.getVisibility() == View.VISIBLE){
            emailErr.setVisibility(View.GONE);
        }
        if (password2.length() > 0 && passError.getVisibility() == View.VISIBLE){
            passError.setVisibility(View.GONE);
        }
        if (confirmpassword2.length() > 0 && CpassError2.getVisibility() == View.VISIBLE){
            CpassError2.setVisibility(View.GONE);
        }
    }

    private void inputChange() {
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                registrationDataCheck();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                registrationDataCheck();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Cpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                registrationDataCheck();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void connect(){
        SafetyNet.getClient(this).verifyWithRecaptcha(SiteKey)
                .addOnSuccessListener(this, new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                    @Override
                    public void onSuccess(SafetyNetApi.RecaptchaTokenResponse recaptchaTokenResponse) {
                        if (!recaptchaTokenResponse.getTokenResult().isEmpty()) {
                            handleSiteVerify(recaptchaTokenResponse.getTokenResult());
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof ApiException) {
                            ApiException apiException = (ApiException) e;
                            Log.d(TAG, "Error message: " +
                                    CommonStatusCodes.getStatusCodeString(apiException.getStatusCode()));
                        } else {
                            Log.d(TAG, "Unknown type of error: " + e.getMessage());
                        }
                    }
                });
    }

    protected  void handleSiteVerify(final String responseToken){
        //it is google recaptcha siteverify server
        //you can place your server url
        String url = "https://www.google.com/recaptcha/api/siteverify";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getBoolean("success")){
                                Toast.makeText(getApplicationContext(),String.valueOf(jsonObject.getBoolean("success")),Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),String.valueOf(jsonObject.getString("error-codes")),Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception ex) {
                            Log.d(TAG, "JSON exception: " + ex.getMessage());

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error message: " + error.getMessage());
                    }
                }) {
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

//    private void coonectClient(){
//        SafetyNet.getClient(this).verifyWithRecaptcha(SiteKey)
//                .addOnSuccessListener(this,
//                        response -> {
//                            signup.setClickable(true);
//                            userResponseToken = response.getTokenResult();
//                            Log.d(TAG,"response" + userResponseToken);
//                            if (!userResponseToken.isEmpty()){
//                               Check(response.getTokenResult());
//                            }
//                        })
//                .addOnFailureListener(this, e -> {
//                        if (e instanceof ApiException) {
//                        ApiException apiException = (ApiException) e;
//                        Log.d(TAG, "Error message: " +
//                                CommonStatusCodes.getStatusCodeString(apiException.getStatusCode()));
//                    } else {
//                        Log.d(TAG, "Unknown type of error: " + e.getMessage());
//                    }
//                });
//    }

//    protected String Check(final String responseToken){
//        String isSuccess="";
//        InputStream is =null;
//        String API = "https://www.google.com/recaptcha/api/siteverify";
//        String newAPI = API + "secret" + SecretKey + "&response=" + userResponseToken;
//        Log.d(TAG,"API" + newAPI);
//        try {
//            URL url = new URL(newAPI);
//            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//            httpURLConnection.setReadTimeout(800000000 /* milliseconds */);
//            httpURLConnection.setConnectTimeout(400000000 /* milliseconds */);
//            httpURLConnection.setRequestMethod("GET");
//            httpURLConnection.setDoInput(true);
//            // Starts the query
//            httpURLConnection.connect();
//            int response = httpURLConnection.getResponseCode();
//            System.out.println(response);
//            is = httpURLConnection.getInputStream();
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
//            StringBuilder stringBuilder = new StringBuilder();
//            String line;
//            while ((line = bufferedReader.readLine()) != null) {
//                stringBuilder.append(line).append("\n");
//            }
//            String result = stringBuilder.toString();
//            Log.d("Api", result);
//
//            try {
//                JSONObject jsonObject = new JSONObject(result);
//                System.out.println("Result Object :  " + jsonObject);
//                isSuccess = jsonObject.getString("success");
//                System.out.println("obj "+isSuccess);
//
//            } catch (Exception e) {
//                Log.d("Exception: ", e.getMessage());
//                e.printStackTrace();
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }
//        return isSuccess;
//    }


    //@SuppressLint("ResourceType")
//    private void checkAllFields() {
//        if(cheki.isChecked() && hasNumber && hasUpperCase && isAtLeast8 && hasSymbol && password.length() > 0){
//            signup.setClickable(true);
//            signup.setBackgroundColor(Color.parseColor(getString(R.color.purple1)));
//        }else{
//            signup.setClickable(false);
//            signup.setBackgroundColor(Color.parseColor(getString(R.color.Gray)));
//        }
//    }
}