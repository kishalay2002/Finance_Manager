package com.example.financemanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import android.os.Bundle;

import android.os.StrictMode;
import android.util.AttributeSet;

import android.provider.Telephony;

import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import org.jetbrains.annotations.NotNull;

//ADMob
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity
{
    private AdView mAdView;

    private static final int READ_SMS_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder(StrictMode.getVmPolicy())
                .detectLeakedClosableObjects()
                .build());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();



        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        BottomNavigationView btNav2 = findViewById(R.id.bottomnav2);
        BottomNavigationView btNav1 = findViewById(R.id.bottomnav1);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED)
        {
            Log.d("Function","Not Called");
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_SMS}, READ_SMS_PERMISSION_CODE);
        }
        else
        {
            //readMessages();
            Log.d("Function","Called");
        }

        FragmentManager fragmentManager = getSupportFragmentManager();

        //TextView tx = findViewById(R.id.textView);

        //Unchecking BottomNav2 by default



        uncheckNav(btNav2.getMenu());
        btNav1.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NotNull MenuItem item)
            {
                uncheckNav(btNav2.getMenu());

                return onClickBottomNav1(item.getItemId(),fragmentManager);
            }
        });

        btNav2.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NotNull MenuItem item) {
                uncheckNav(btNav1.getMenu());

                return onClickBottomNav2(item.getItemId(),fragmentManager);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == READ_SMS_PERMISSION_CODE)
        {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "Permission denied. Cannot read SMS.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();

        BottomNavigationView btNav2 = findViewById(R.id.bottomnav2);
        BottomNavigationView btNav1 = findViewById(R.id.bottomnav1);

        FragmentManager fragmentManager = getSupportFragmentManager();

        btNav1.setSelectedItemId(R.id.home);

        btNav1.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NotNull MenuItem item)
            {
                uncheckNav(btNav2.getMenu());

                return onClickBottomNav1(item.getItemId(),fragmentManager);
            }
        });
    }

    public boolean onClickBottomNav1(int id,FragmentManager fragmentManager)
    {
        switch(id)
        {
            case R.id.home:
                overridePendingTransition(0,0);
                fragmentManager.beginTransaction().replace(R.id.fragmentContainerMain, Home.class,null).setReorderingAllowed(true).addToBackStack("name").commit();
                return true;

            case R.id.analysis:
                overridePendingTransition(0,0);
                fragmentManager.beginTransaction().replace(R.id.fragmentContainerMain, Analysis.class,null).setReorderingAllowed(true).addToBackStack("name").commit();
                return true;
        }
        return false;
    }

    public boolean onClickBottomNav2(int id,FragmentManager fragmentManager)
    {
        switch(id)
        {
            case R.id.accounts:
                overridePendingTransition(0, 0);
                fragmentManager.beginTransaction().replace(R.id.fragmentContainerMain, Account.class,null).setReorderingAllowed(true).addToBackStack("name").commit();
                return true;

            case R.id.settings:
                overridePendingTransition(0, 0);
                fragmentManager.beginTransaction().replace(R.id.fragmentContainerMain, Settings.class,null).setReorderingAllowed(true).addToBackStack("name").commit();
                return true;
        }

        return false;
    }

    public void uncheckNav(Menu menu)
    {
        menu.setGroupCheckable(0,true,false);
        for(int i=0;i<menu.size();i++)
            menu.getItem(i).setChecked(false) ;

        menu.setGroupCheckable(0,true,true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        BottomNavigationView btNav2 = findViewById(R.id.bottomnav2);
        BottomNavigationView btNav1 = findViewById(R.id.bottomnav1);
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            // your code
            btNav1.setSelectedItemId(R.id.home);

            btNav1.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
            {
                @Override
                public boolean onNavigationItemSelected(@NotNull MenuItem item)
                {
                    uncheckNav(btNav2.getMenu());

                    return onClickBottomNav1(item.getItemId(),fragmentManager);
                }
            });
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}