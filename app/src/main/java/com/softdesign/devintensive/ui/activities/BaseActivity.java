package com.softdesign.devintensive.ui.activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.utils.ConstantManager;

public class BaseActivity extends AppCompatActivity{

    static final String TAG = ConstantManager.TAG_PREFIX + ConstantManager.PREFIX_BASE_ACTIVITY;
    protected ProgressDialog mProgressDialog;

    /**
     * Show progress dialog
     */
    public void showProgress() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this, R.style.custom_progress_dialog);
            mProgressDialog.setCancelable(false);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mProgressDialog.show();
            mProgressDialog.setContentView(R.layout.progress_splash);
        } else {
            mProgressDialog.show();
            mProgressDialog.setContentView(R.layout.progress_splash);
        }
    }

    /**
     * Hide progress dialog
     */
    public void hideProgress(){
        if (mProgressDialog!=null){
            if (mProgressDialog.isShowing()){
                mProgressDialog.hide();
            }
        }
    }

    /**
     * @param message String error message
     * @param error Exception error
     */
    public void showErrors(String message,Exception error){
        showToast(message);
        Log.e(TAG,String.valueOf(error));
    }

    /**
     * Show toast with message
     * @param message Message
     */
    public void showToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }
}
