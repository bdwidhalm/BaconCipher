package com.woodystech.baconcipher;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.*;
import com.google.ads.*;
import com.google.analytics.tracking.android.EasyTracker;

public class DeCipher extends Activity
{
    private static final int binarySize = 7;
    private final String appLink = "\n\n Made with http://www.woodystech.com";
    private static Map<String, Character> decode = new HashMap<String, Character>();
    
    private EditText etOrigMessage;
    private TextView tvBaconMessage;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.decipher);
        
        etOrigMessage = (EditText) findViewById(R.id.orgMessage);
        tvBaconMessage = (TextView) findViewById(R.id.baconMessage);
        
        // Look up the AdView and load request
        AdView adView = (AdView)this.findViewById(R.id.adViewDeCipher);
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
    
    public void decrypt(View view)
    {
        if(!etOrigMessage.getText().toString().equals(""))
        {
            loadCode();
            String unBacon = unBaconize(etOrigMessage.getText().toString());
            decode(unBacon);
        }
        else
        {
            //do nothing - inform user to enter secret message first
            String informUser = "Please enter a secret message first!";
            Toast toast = Toast.makeText(getApplicationContext(), informUser, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
        
    }
    
    private void decode(String message)
    {
        String decMessage = "";
        
        for (String part : message.split(" "))
        {
            char decPart = ' ';
            if (decode.containsKey(part))
            {
                decPart = decode.get(part);
            }
            decMessage = decMessage + String.valueOf(decPart);
        }
        
        tvBaconMessage.setText(decMessage);
    }
    
    private String unBaconize(String baconMessage)
    {
        String codedMessage = "";
        int counter = 0;
        
        for (char c : baconMessage.toCharArray())
        {
            if (c != ' ')
            {
                if (Character.isUpperCase(c))
                {
                    codedMessage = codedMessage + "B";
                    counter++;
                }
                else
                {
                    codedMessage = codedMessage + "A";
                    counter++;
                }
                
                if (counter == binarySize)
                {
                    codedMessage = codedMessage + " ";
                    counter = 0;
                }
            }
        }
        
        return codedMessage;
    }
    
    private void loadCode()
    {
        String a = "A";
        String b = "B";
        String alpha = " abcdefghijklmnopqrstuvwxyz";
        alpha = alpha + "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        alpha = alpha + "`1234567890-=~!@#$%^&*()_+[];\\,./{}|:<>?";
        int counter = 0;
        String binary;
        for (char c : alpha.toCharArray())
        {
            binary = Integer.toBinaryString(counter);
            while (binary.length() < binarySize)
            {
                binary = "0" + binary;
            }
            binary = binary.replaceAll("0", "A").replaceAll("1", "B");
            //System.out.println(c + " = " + binary);
            //code.put(c, binary);
            decode.put(binary, c);
            binary = "";
            counter++;
        }
    }
}
