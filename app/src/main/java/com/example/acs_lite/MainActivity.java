package com.example.acs_lite;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button signup,verify;
    Button signin;
    TextView logo;
    ImageView needat;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signup=findViewById(R.id.registerHbutton);
        signin=findViewById(R.id.loginHbutton);
        logo = findViewById(R.id.textView10);
        needat = findViewById(R.id.imageView);
        verify = findViewById(R.id.verifyH);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIt = new Intent(MainActivity.this,ReCaptcha.class);
                startActivity(sendIt);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signU = new Intent(MainActivity.this,Register.class);
                startActivity(signU);
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signI = new Intent(MainActivity.this,Login.class);
                startActivity(signI);
            }
        });
    }
}