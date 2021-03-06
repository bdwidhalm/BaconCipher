package com.woodystech.baconcipher;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import java.util.*;
import com.google.ads.*;
import com.google.analytics.tracking.android.EasyTracker;

public class BaconCipher extends Activity
{
    private static final int binarySize = 7;
    private static Map<Character, String> code = new HashMap<Character, String>();
    private static ShakesQuotes shakesQuote = new ShakesQuotes();
    
    private final String appLink = "\n\n Made with http://goo.gl/608nVA";
    
    private EditText etOrigMessage;
    private EditText etCloakMessage;
    private TextView tvCloakCount;
    private TextView tvBaconMessage;
    
    private final TextWatcher twCloakMessage = new TextWatcher()
    {
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {
        }
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            int needCount = etOrigMessage.getText().length() * (binarySize + 1);
            String message = s.toString().replaceAll(" ", "");
            int curCount = message.length();
            tvCloakCount.setText(curCount + "/" + needCount);
            if (curCount < needCount)
            {
                tvCloakCount.setTextColor(Color.BLUE);
            }
            else if (curCount == needCount)
            {
                tvCloakCount.setTextColor(Color.GREEN);
            }
            else if (curCount > needCount)
            {
                tvCloakCount.setTextColor(Color.RED);
            }
        }
        public void afterTextChanged(Editable s)
        {
        }
    };
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        etOrigMessage = (EditText) findViewById(R.id.orgMessage);
        etCloakMessage = (EditText) findViewById(R.id.cloakMessage);
        etCloakMessage.addTextChangedListener(twCloakMessage);
        tvCloakCount = (TextView) findViewById(R.id.tvCount);
        tvBaconMessage = (TextView) findViewById(R.id.baconMessage);
        
        // Look up the AdView and load request
        AdView adView = (AdView)this.findViewById(R.id.adViewMain);
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
        if (!etCloakMessage.getText().toString().equals("") && !etOrigMessage.getText().toString().equals(""))
        {
            int cloakCount = etCloakMessage.getText().toString().replaceAll(" ", "").length();
            int origCount = etOrigMessage.getText().length() * (binarySize + 1);
            if(cloakCount == origCount)
            {
                baconize();
            }
            else if (cloakCount < origCount)
            {
                //Do Nothing
            String informUser = "You don't have a big enough message to hide your secret!\n" + cloakCount + "/" + origCount;
            Toast toast = Toast.makeText(getApplicationContext(), informUser, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
            }
            else if (cloakCount > origCount)
            {
                //Do Nothing
            String informUser = "Your message is too big to hide your secret!\n" + cloakCount + "/" + origCount;
            Toast toast = Toast.makeText(getApplicationContext(), informUser, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
            }
            return;
        }
        else
        {
            //Do Nothing
            String informUser = "Please enter a message to hide secret message in!!";
            Toast toast = Toast.makeText(getApplicationContext(), informUser, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
    }
    
    public void shakes(View view)
    {
        if (!etOrigMessage.getText().toString().equals(""))
        {
            int lengthNeeded = etOrigMessage.getText().toString().length() * (binarySize + 1);
            String shakesText = shakesQuote.getQuote(lengthNeeded);
            
            etCloakMessage.setText(shakesText);
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
    
    public void share(View view)
    {
        if (!tvBaconMessage.getText().toString().equals(""))
        {
            //create new send intent
            Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
            //type
            shareIntent.setType("text/plain");
            
            //SEND subject
            shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Secret Message");
            //body of message
            String shareMessage = tvBaconMessage.getText().toString() + appLink;
            
            //add message
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareMessage);
            //start chooser for sharing
            startActivity(Intent.createChooser(shareIntent, "Share My Secret Message."));
        }
    }
    
    private void baconize()
    {
        loadCode();
        String codedMessage = encode(etOrigMessage.getText().toString());
        String baconMessage = etCloakMessage.getText().toString().toLowerCase();
        
        int j = 0;
        
        for (int i = 0; i < codedMessage.length(); i++)
        {
            while (baconMessage.charAt(j) == ' ')
            {
                j++;
            }
            
            if (codedMessage.charAt(i) == 'B')
            {
                baconMessage = baconMessage.substring(0, j) + Character.toString(baconMessage.charAt(j)).toUpperCase() + baconMessage.substring(j + 1);
                j++;
            }
            else if (codedMessage.charAt(i) == 'A')
            {
                //do nothing
                j++;
            }
        }
        
        TextView tvshareText = (TextView) findViewById(R.id.shareText);
        tvshareText.setVisibility(View.VISIBLE);
        tvBaconMessage.setText(baconMessage);
    }
    
    private String encode(String message)
    {
        String encMessage = "";
        
        for (char c : message.toCharArray())
        {
            String encPart = "";
            if (code.containsKey(c))
            {
                encPart = code.get(c);
            }
            else
            {
                encPart = "UNKNCHR ";
            }
            encMessage = encMessage + encPart + " ";
        }
        
        return encMessage.trim();
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
            code.put(c, binary);
            //decode.put(binary, c);
            binary = "";
            counter++;
        }
    }
}
