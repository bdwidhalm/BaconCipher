package com.woodystech.baconcipher;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.os.Bundle;
import android.widget.LinearLayout;
import com.google.ads.*;
import com.google.analytics.tracking.android.EasyTracker;

public class Splash extends Activity
{
    private static final String AD_UNIT_ID = "ca-app-pub-7773256712936597/9920471426";
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        
        // Look up the AdView and load request
        AdView adView = (AdView)this.findViewById(R.id.adViewSplash);
        AdRequest adRequest = new AdRequest();
        adView.loadAd(adRequest);
        
    }
    
    @Override
    public void onStart() 
    {
        super.onStart();
        
        EasyTracker.getInstance(this).activityStart(this);  // Add this method.
    }

    @Override
    public void onStop() 
    {
        super.onStop();
        
        EasyTracker.getInstance(this).activityStop(this);  // Add this method.
    }
    
    public void encrypt(View view)
    {
        Intent intent = new Intent(this, BaconCipher.class);
        startActivity(intent);
    }
    
    public void decrypt(View view)
    {
        Intent intent = new Intent(this, DeCipher.class);
        startActivity(intent);
    }
}
