package com.softdesign.devintensive.ui.activities;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
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
import android.widget.ImageView;


import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.manager.DataManager;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.RoundedAvatarDrawable;

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
    private DataManager mDataManager;
    private ImageView mImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDataManager = DataManager.getInstanse();

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
        loadInfoUser();
        Log.d(TAG,"onCreate");
        if (savedInstanceState==null){
//            showProgress();
//            runWithDelay();
//            showSnackBar("Запускается впервые");//Если запускаем впервые
        } else {
            mCurrentEditMode = savedInstanceState.getInt(ConstantManager.EDIT_CONSTANT_MODE,0);
            changeEditMode(mCurrentEditMode);
//            showSnackBar("Уже запущено");
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
        outState.putInt(ConstantManager.EDIT_CONSTANT_MODE,mCurrentEditMode);
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

        mImageView = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.avatar);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.avatar);
        RoundedBitmapDrawable rBD = RoundedBitmapDrawableFactory.create(getResources(),bitmap);
        rBD.setCornerRadius(Math.max(bitmap.getHeight(),bitmap.getWidth())/2.0f);
        mImageView.setImageDrawable(rBD);
    }

    private void changeEditMode (int mode){
        if (mode == 1) {
            mfab.setImageResource(R.drawable.ic_done_black_24dp);
            for (EditText editText : mUserInfo) {
                editText.setFocusable(true);
                editText.setEnabled(true);
                editText.setFocusableInTouchMode(true);
            }
        } else {
            mfab.setImageResource(R.drawable.ic_create_black_24dp);
            for (EditText editText : mUserInfo) {
                editText.setFocusable(false);
                editText.setEnabled(false);
                editText.setFocusableInTouchMode(false);
                saveInfoUser();
        }
        }

    }

    private void loadInfoUser() {
        List<String> userInfoFields = mDataManager.getPreferencesManager().loadUserProfileData();
        for (int i = 0; i < userInfoFields.size(); i++) {
            mUserInfo.get(i).setText(userInfoFields.get(i));
        }
    }

    private void saveInfoUser() {
        List<String> userInfoFields = new ArrayList<>();
        for (EditText userField : mUserInfo) {
            userInfoFields.add(userField.getText().toString());
        }
        mDataManager.getPreferencesManager().saveUserProfileData(userInfoFields);

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

    @Override
    public void onBackPressed() {

        if (mNavigationDrawer!=null && mNavigationDrawer.isDrawerVisible(GravityCompat.START)){
            mNavigationDrawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }

    }

    public static Bitmap getRoundedBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }
}