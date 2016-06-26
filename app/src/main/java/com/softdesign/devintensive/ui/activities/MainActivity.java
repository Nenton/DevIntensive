package com.softdesign.devintensive.ui.activities;

import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.softdesign.devintensive.R;
import com.softdesign.devintensive.utils.ConstantManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG= ConstantManager.TAG_PREFIX+"Main Activity";
    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private DrawerLayout mNavigationDrawer;
    private int mCurrentEditMode=0;
    private FloatingActionButton mfab;
    private EditText mUserPhone, mUserEmail, mUserVk, mUserGit, mUserAbout;
    private List<EditText> mUserInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCoordinatorLayout = (CoordinatorLayout)findViewById(R.id.main_coordinator_layout);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.nav_drawer);
        mfab = (FloatingActionButton)findViewById(R.id.fab);

        mUserPhone = (EditText)findViewById(R.id.phone_text);
        mUserEmail = (EditText)findViewById(R.id.mail_text);
        mUserVk = (EditText)findViewById(R.id.vk_text);
        mUserGit = (EditText)findViewById(R.id.github_text);
        mUserAbout = (EditText)findViewById(R.id.about_text);

        mUserInfo = new ArrayList<>();

        mUserInfo.add(mUserPhone);
        mUserInfo.add(mUserEmail);
        mUserInfo.add(mUserVk);
        mUserInfo.add(mUserGit);
        mUserInfo.add(mUserAbout);

        mfab.setOnClickListener(this);
        setupToolbar();
        setupDrawer();
        Log.d(TAG,"onCreate");
        if (savedInstanceState==null){
            showProgress();
            runWithDelay();
            showSnackBar("Запускается впервые");//Если запускаем впервые
        } else {
            showSnackBar("Уже запущено");
            //Если уже имеет какие-то значения

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG,"onRestart");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void runWithDelay(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgress();
            }
        },5000);
    }

    private void showSnackBar(String message){
        Snackbar.make(mCoordinatorLayout,message,Snackbar.LENGTH_LONG).show();
    }

    private void setupToolbar (){
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setHomeAsUpIndicator(R.drawable.ic_list_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupDrawer(){
        final NavigationView navigationView =(NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                showSnackBar(item.getTitle().toString());
                item.setCheckable(true);
                mNavigationDrawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    private void changeEditMode (int mode){
        if (mode == 1) {
            for (EditText editText : mUserInfo) {
                editText.setFocusable(true);
                editText.setEnabled(true);
                editText.setFocusableInTouchMode(true);
            }
        } else {
            for (EditText editText : mUserInfo) {
                editText.setFocusable(false);
                editText.setEnabled(false);
                editText.setFocusableInTouchMode(false);
        }
        }

    }

    private void loadInfoUser() {

    }

    private void saveInfoUser() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                if (mCurrentEditMode == 1) {
                    changeEditMode(0);
                    mCurrentEditMode = 0;
                } else {
                    changeEditMode(1);
                    mCurrentEditMode = 1;
                }
                break;
        }

    }
}