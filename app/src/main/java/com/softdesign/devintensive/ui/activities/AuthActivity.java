package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.manager.DataManager;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.data.storage.models.Repositories;
import com.softdesign.devintensive.data.storage.models.RepositoriesDao;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDao;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.NetworkStatusChecker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private static final String TAG = ConstantManager.TAG_PREFIX + "Auth Activity";
    private static final String RESPONSE = "OnResponse ";


    private DataManager mDataManager;
    private RepositoriesDao mRepositoriesDao;
    private UserDao mUserDao;
    private Handler handler;

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


    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    protected void onStart() {
        if (mDataManager.getPreferencesManager().getAuthToken() != null
                && !mDataManager.getPreferencesManager().getAuthToken().equals(ConstantManager.NULL_STRING)
                && mDataManager.getPreferencesManager().getUserId() != null
                && !mDataManager.getPreferencesManager().getUserId().equals(ConstantManager.NULL_STRING)) {
            showProgress();
            Intent intent = new Intent(AuthActivity.this, MainActivity.class);
            startActivity(intent);
        }
        super.onStart();
        EventBus.getDefault().register(this);
    }

    /**
     * Create activity
     *
     * @param savedInstanceState Saved instance data
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_activity);
        ButterKnife.bind(this);
        handler = new Handler();
        mDataManager = DataManager.getInstanse();
        mUserDao = mDataManager.getDaoSession().getUserDao();
        mRepositoriesDao = mDataManager.getDaoSession().getRepositoriesDao();
        mSwitch.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            mSwitch.setTextColor(getResources().getColor(R.color.color_accent));
        } else
            mSwitch.setTextColor(getResources().getColor(R.color.grey_light));
    }

    /**
     * Action on click button login
     */
    @Optional
    @OnClick(R.id.btn_auth)
    void signIn() {
        if (NetworkStatusChecker.isNetworkAvailable(this)) {
            showProgress();
            EventBus.getDefault().postSticky(mDataManager.loginUser(new UserLoginReq(mLogin.getText().toString(), mPassword.getText().toString())));

        } else {
            hideProgress();
            showSnackbar(ConstantManager.NETWORK_NOT_FOUND);
            if (mDataManager.getPreferencesManager().getAuthToken() != null
                    && !mDataManager.getPreferencesManager().getAuthToken().equals(ConstantManager.NULL_STRING)
                    && mDataManager.getPreferencesManager().getUserId() != null
                    && !mDataManager.getPreferencesManager().getUserId().equals(ConstantManager.NULL_STRING)) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        Snackbar.make(mCoordinatorLayout, ConstantManager.MESSAGE_GET_AUTH_OFFLINE, Snackbar.LENGTH_LONG).setAction(ConstantManager.MESSAGE_CAN_USE_AUTH_OFFLINE, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }).show();
                    }
                };
                handler.postDelayed(runnable, ConstantManager.DELAY_POST_AUTH);
            }
        }
    }

    @Subscribe(sticky = false, threadMode = ThreadMode.ASYNC)
    public void sign(Call<UserModelRes> call) {
        call.enqueue(new Callback<UserModelRes>() {
            @Override
            public void onResponse(Call<UserModelRes> call, Response<UserModelRes> response) {
                if (response.code() == ConstantManager.RESPONSE_CODE_ACCESS) {
                    loginSuccess(response.body());
                } else if (response.code() == ConstantManager.RESPONSE_CODE_NOT_FOUND) {
                    hideProgress();
                    showSnackbar(ConstantManager.ERROR_LOGIN_PASSWORD);
                    if (mDataManager.getPreferencesManager().getAuthToken() != null
                            && !mDataManager.getPreferencesManager().getAuthToken().equals(ConstantManager.NULL_STRING)
                            && mDataManager.getPreferencesManager().getUserId() != null
                            && !mDataManager.getPreferencesManager().getUserId().equals(ConstantManager.NULL_STRING)) {
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                Snackbar.make(mCoordinatorLayout, ConstantManager.MESSAGE_GET_AUTH_OFFLINE, Snackbar.LENGTH_LONG).setAction(ConstantManager.MESSAGE_CAN_USE_AUTH_OFFLINE, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                }).show();
                            }
                        };
                        handler.postDelayed(runnable, ConstantManager.DELAY_POST_AUTH);
                    }
                } else {
                    hideProgress();
                    showSnackbar(ConstantManager.STANDART_ERROR_MESSAGE);
                }
            }

            @Override
            public void onFailure(Call<UserModelRes> call, Throwable t) {
                hideProgress();
                showSnackbar(ConstantManager.STANDART_ERROR_MESSAGE);
                if (mDataManager.getPreferencesManager().getAuthToken() != null
                        && !mDataManager.getPreferencesManager().getAuthToken().equals(ConstantManager.NULL_STRING)
                        && mDataManager.getPreferencesManager().getUserId() != null
                        && !mDataManager.getPreferencesManager().getUserId().equals(ConstantManager.NULL_STRING)) {
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            Snackbar.make(mCoordinatorLayout, ConstantManager.MESSAGE_GET_AUTH_OFFLINE, Snackbar.LENGTH_LONG).setAction(ConstantManager.MESSAGE_CAN_USE_AUTH_OFFLINE, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            }).show();
                        }
                    };
                    handler.postDelayed(runnable, ConstantManager.DELAY_POST_AUTH);
                }
            }
        });
    }

    @OnClick(R.id.remember_password)
    void rememberPassword() {
        Intent rememberIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ConstantManager.DEVINTENSIV_FOR_GOT_PASSWORD));
        startActivity(rememberIntent);
    }

    private void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void loginSuccess(UserModelRes userModel) {
        mDataManager.getPreferencesManager().saveAuthToken(userModel.getData().getToken());
        mDataManager.getPreferencesManager().saveUserId(userModel.getData().getUser().getId());
        saveUserInBd(callback());
        EventBus.getDefault().postSticky(userModel);
    }

    @Subscribe(sticky = false, threadMode = ThreadMode.ASYNC)
    public void saveUserProfileFields(UserModelRes userModel) {
        ArrayList<String> userProfileFields = new ArrayList<>();
        userProfileFields.add(userModel.getData().getUser().getContacts().getPhone());
        userProfileFields.add(userModel.getData().getUser().getContacts().getEmail());
        userProfileFields.add(userModel.getData().getUser().getContacts().getVk());
        userProfileFields.add(userModel.getData().getUser().getPublicInfo().getBio());
        userProfileFields.add(String.valueOf(userModel.getData().getUser().getProfileValues().getRating()));
        userProfileFields.add(String.valueOf(userModel.getData().getUser().getProfileValues().getLinesCode()));
        userProfileFields.add(String.valueOf(userModel.getData().getUser().getProfileValues().getProjects()));
        userProfileFields.add(userModel.getData().getUser().getPublicInfo().getPhoto());
        userProfileFields.add(userModel.getData().getUser().getPublicInfo().getAvatar());
        userProfileFields.add(userModel.getData().getUser().getFullName());
        mDataManager.getPreferencesManager().saveUserProfileData(userProfileFields);
    }

    @Subscribe(sticky = false, threadMode = ThreadMode.ASYNC)
    public void saveUserInBd(Callback<UserListRes> mCallback) {
        if (mDataManager.getUser() == null || mDataManager.getUser() < 1) {
            Call<UserListRes> call = mDataManager.getListUserFromNetwork();
            call.enqueue(mCallback);
        } else {
            Intent intent = new Intent(AuthActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    private List<Repositories> getRepoListFromUserRes(UserListRes.Datum userData) {
        final String userId = userData.getId();
        List<Repositories> repositories = new ArrayList<>();
        for (UserListRes.Repo repositoryRes : userData.getRepositories().getRepo()) {
            repositories.add(new Repositories(repositoryRes, userId));
        }
        return repositories;
    }

    private Callback<UserListRes> callback() {
        return new Callback<UserListRes>() {
            @Override
            public void onResponse(Call<UserListRes> call, Response<UserListRes> response) {
                try {
                    if (response.code() == ConstantManager.RESPONSE_CODE_ACCESS) {
                        List<Repositories> allRepositories = new ArrayList<Repositories>();
                        List<User> allUsers = new ArrayList<User>();

                        for (UserListRes.Datum userRes : response.body().getData()) {
                            allRepositories.addAll(getRepoListFromUserRes(userRes));
                            allUsers.add(new User(userRes));
                        }
                        mRepositoriesDao.insertOrReplaceInTx(allRepositories);
                        mUserDao.insertOrReplaceInTx(allUsers);
                        EventBus.getDefault().removeAllStickyEvents();
                        Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                        startActivity(intent);

                    } else {
                        hideProgress();
                        showSnackbar(ConstantManager.LIST_USER_NOT_CAN_GET);
                        Log.e(TAG, RESPONSE + String.valueOf(response.errorBody().source()));
                    }

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    hideProgress();
                    showSnackbar(ConstantManager.STANDART_ERROR_MESSAGE);
                }
            }

            @Override
            public void onFailure(Call<UserListRes> call, Throwable t) {
                hideProgress();
                showSnackbar(ConstantManager.STANDART_ERROR_MESSAGE);
                if (mDataManager.getPreferencesManager().getAuthToken() != null
                        && !mDataManager.getPreferencesManager().getAuthToken().equals(ConstantManager.NULL_STRING)
                        && mDataManager.getPreferencesManager().getUserId() != null
                        && !mDataManager.getPreferencesManager().getUserId().equals(ConstantManager.NULL_STRING)) {
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            Snackbar.make(mCoordinatorLayout, ConstantManager.MESSAGE_GET_AUTH_OFFLINE, Snackbar.LENGTH_LONG).setAction(ConstantManager.MESSAGE_CAN_USE_AUTH_OFFLINE, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            }).show();
                        }
                    };
                    handler.postDelayed(runnable, ConstantManager.DELAY_POST_AUTH);
                }
            }
        };
    }
}
