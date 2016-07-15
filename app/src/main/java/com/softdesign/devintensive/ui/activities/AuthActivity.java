package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.manager.DataManager;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.NetworkStatusChecker;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{
    private DataManager mDataManager;

    @Nullable
    @BindView(R.id.btn_auth)
    Button mSignIn;
    @BindView(R.id.remember_password)
    TextView mRememberPassword;
    @BindView(R.id.auth_login_email)
    EditText mLogin;
    @BindView(R.id.auth_login_password)
    EditText mPassword;
    @BindView(R.id.auth_coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.switch_save_me)
    SwitchCompat mSwitch;

    /**
     * Create activity
     *
     * @param savedInstanceState Saved instance data
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mDataManager = DataManager.getInstanse();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_activity);
        ButterKnife.bind(this);
        if (!mDataManager.getPreferencesManager().loadEmailAuthActivity().equals("")){
            mLogin.setText(mDataManager.getPreferencesManager().loadEmailAuthActivity());
            mSwitch.setChecked(true);
        }

        mSwitch.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked){
            mSwitch.setTextColor(getResources().getColor(R.color.color_accent));
        } else ;
    }

    /**
     * Action on click button login
     */
    @Optional
    @OnClick(R.id.btn_auth)
    void signIn() {
        if (NetworkStatusChecker.isNetworkAvailable(this)) {
            Call<UserModelRes> call = mDataManager.loginUser(new UserLoginReq(mLogin.getText().toString(), mPassword.getText().toString()));
            call.enqueue(new Callback<UserModelRes>() {
                @Override
                public void onResponse(Call<UserModelRes> call, Response<UserModelRes> response) {
                    if (response.code() == 200) {
                        loginSuccess(response.body());
                    } else if (response.code() == 404) {
                        showSnackbar("Неверный логин или пароль");
                    } else {
                        showSnackbar("Все пропало, Чипчилинка");
                    }
                }

                @Override
                public void onFailure(Call<UserModelRes> call, Throwable t) {
                    // TODO: 10.07.2016 обработка ошибки ретрофита
                }
            });
        } else {
            showSnackbar("Сеть не найдена, попробуйте позже");
        }

    }

    @OnClick(R.id.remember_password)
    void rememberPassword() {
        Intent rememberIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://devintensive.softdesign-apps.ru/forgotpass"));
        startActivity(rememberIntent);
    }

    private void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void loginSuccess(UserModelRes userModel) {
        userModel.getData().getToken();
        mDataManager.getPreferencesManager().saveAuthToken(userModel.getData().getToken());
        mDataManager.getPreferencesManager().saveUserId(userModel.getData().getUser().getId());
        if (mSwitch.isChecked()) {
            mDataManager.getPreferencesManager().saveEmailAuthActivity(mLogin.getText().toString());
        } else if (!mDataManager.getPreferencesManager().loadEmailAuthActivity().isEmpty()){
            mDataManager.getPreferencesManager().saveEmailAuthActivity("");
        }
        saveUserValues(userModel);
        saveUserPhotoAndAvatar(userModel);
        saveUserProfileFields(userModel);
        saveFirstSecondNameUser(userModel);
        Intent intent = new Intent(AuthActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void saveUserValues(UserModelRes userModel) {
        int[] userValues = {
                userModel.getData().getUser().getProfileValues().getRating(),
                userModel.getData().getUser().getProfileValues().getLinesCode(),
                userModel.getData().getUser().getProfileValues().getProjects(),
        };
        mDataManager.getPreferencesManager().saveUserProfileValues(userValues);

    }

    private void saveUserProfileFields(UserModelRes userModel) {
        ArrayList<String> userProfileFields = new ArrayList<>();
        userProfileFields.add(userModel.getData().getUser().getContacts().getPhone());
        userProfileFields.add(userModel.getData().getUser().getContacts().getEmail());
        userProfileFields.add(userModel.getData().getUser().getContacts().getVk());
        userProfileFields.add(userModel.getData().getUser().getRepositories().getRepo().get(ConstantManager.NULL).getGit());
        userProfileFields.add(userModel.getData().getUser().getPublicInfo().getBio());
        mDataManager.getPreferencesManager().saveUserProfileData(userProfileFields);
    }

    private void saveUserPhotoAndAvatar(UserModelRes userModel) {
        Uri photo = Uri.parse(userModel.getData().getUser().getPublicInfo().getPhoto());
        Uri avatar = Uri.parse(userModel.getData().getUser().getPublicInfo().getAvatar());
        mDataManager.getPreferencesManager().saveUserPhoto(photo);
        mDataManager.getPreferencesManager().saveAvatarImage(avatar);
    }

    private void saveFirstSecondNameUser(UserModelRes userModel) {
        String firstName = userModel.getData().getUser().getFirstName();
        String secondName = userModel.getData().getUser().getSecondName();
        mDataManager.getPreferencesManager().saveFirstSecondNameUser(firstName, secondName);
    }



}
