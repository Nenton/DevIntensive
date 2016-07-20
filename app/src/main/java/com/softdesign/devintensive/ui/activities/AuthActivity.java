package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
    private DataManager mDataManager;
    private RepositoriesDao mRepositoriesDao;
    private UserDao mUserDao;

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
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onStart() {
        EventBus.getDefault().register(this);
        if (mDataManager.getPreferencesManager().getAuthToken() != null
                && !mDataManager.getPreferencesManager().getAuthToken().equals(ConstantManager.NULL_STRING)
                && mDataManager.getPreferencesManager().getUserId() != null
                && !mDataManager.getPreferencesManager().getUserId().equals(ConstantManager.NULL_STRING)) {
            showProgress();

            EventBus.getDefault().postSticky(Callback());
        }
        super.onStart();
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
            showSnackbar("Сеть не найдена, попробуйте позже");
            hideProgress();
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.ASYNC)
    public void sign(Call<UserModelRes> call) {
        call.enqueue(new Callback<UserModelRes>() {
            @Override
            public void onResponse(Call<UserModelRes> call, Response<UserModelRes> response) {
                if (response.code() == 200) {
                    loginSuccess(response.body());
                } else if (response.code() == 404) {
                    hideProgress();
                    showSnackbar("Неверный логин или пароль");
                } else {
                    hideProgress();
                    showSnackbar("Что-то пошло не так");
                }
            }

            @Override
            public void onFailure(Call<UserModelRes> call, Throwable t) {
                // TODO: 10.07.2016 обработка ошибки ретрофита
            }
        });
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
        mDataManager.getPreferencesManager().saveAuthToken(userModel.getData().getToken());
        mDataManager.getPreferencesManager().saveUserId(userModel.getData().getUser().getId());
        saveUserInBd(Callback());
        EventBus.getDefault().postSticky(userModel);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.ASYNC)
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
    @Subscribe(sticky = true, threadMode = ThreadMode.ASYNC)
    public void saveUserInBd(Callback<UserListRes> mCallback) {
        Call<UserListRes> call = mDataManager.getListUserFromNetwork();
        call.enqueue(mCallback);
    }

    private List<Repositories> getRepoListFromUserRes(UserListRes.Datum userData) {
        final String userId = userData.getId();
        List<Repositories> repositories = new ArrayList<>();
        for (UserListRes.Repo repositoryRes : userData.getRepositories().getRepo()) {
            repositories.add(new Repositories(repositoryRes, userId));
        }
        return repositories;
    }

    private Callback<UserListRes> Callback(){
        return new Callback<UserListRes>() {
            @Override
            public void onResponse(Call<UserListRes> call, Response<UserListRes> response) {
                try {
                    if (response.code() == 200) {
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
                        showSnackbar("Список пользователей не может быть получен");
                        Log.e(TAG, "OnResponse " + String.valueOf(response.errorBody().source()));
                    }

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    hideProgress();
                    showSnackbar("Чтото пошло не так");
                }
            }

            @Override
            public void onFailure(Call<UserListRes> call, Throwable t) {
                hideProgress();
                // TODO: 14.07.2016 Обработка ошибок
            }
        };
    }
}
