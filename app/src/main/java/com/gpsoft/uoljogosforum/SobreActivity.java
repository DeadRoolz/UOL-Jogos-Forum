/*
* Activity que exibe informações
* sobre o aplicativo
*/


package com.gpsoft.uoljogosforum;

//android imports
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;


//Glide imports
import com.bumptech.glide.Glide;

public class SobreActivity extends AppCompatActivity {
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);
        
        Toolbar myToolbar = (Toolbar) findViewById(R.id.sobre_toolbar);
        setSupportActionBar(myToolbar);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        
    }
    
}