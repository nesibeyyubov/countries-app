package com.nesib.countriesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class WelcomeActivity extends AppCompatActivity {
    private Button nextButton;
    private EditText nameEditText;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        preferences = getSharedPreferences("shared_prefs",MODE_PRIVATE);
        String name =preferences.getString("name","");
        if(!name.isEmpty()){
            openMainActivity(name);
        }

        nextButton = findViewById(R.id.nextButton);
        nameEditText = findViewById(R.id.nameEditText);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString().trim();
                if(!name.isEmpty()){
                    openMainActivity(name);
                }
            }
        });
    }


    // Add to splash screen
    public boolean checkPlayServices(){
        GoogleApiAvailability googleApis = GoogleApiAvailability.getInstance();
        int resultCode = googleApis.isGooglePlayServicesAvailable(getApplicationContext());
        if(resultCode != ConnectionResult.SUCCESS){
            if(googleApis.isUserResolvableError(resultCode)){
                googleApis.getErrorDialog(this,resultCode,2404).show();
            }
            return false;
        }
        return true;
    }

    private void openMainActivity(String name){
        Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
        intent.putExtra("name",name);
        preferences.edit().putString("name",name).apply();
        startActivity(intent);
        finish();
    }
}