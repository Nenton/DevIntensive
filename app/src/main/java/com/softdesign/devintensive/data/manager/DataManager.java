package com.softdesign.devintensive.data.manager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;
import com.softdesign.devintensive.data.network.PicassoCache;
import com.softdesign.devintensive.data.network.RestService;
import com.softdesign.devintensive.data.network.ServiceGenerator;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UploadAvatarUser;
import com.softdesign.devintensive.data.network.res.UploadPhotoUser;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.data.storage.models.DaoSession;
import com.softdesign.devintensive.data.storage.models.Repositories;
import com.softdesign.devintensive.data.storage.models.RepositoriesDao;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDao;
import com.softdesign.devintensive.utils.DevIntensiveApplication;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;

public class DataManager {
    private static DataManager INSTANCE = null;
    private Context mContext;
    private PreferencesManager mPreferencesManager;
    private RestService mRestService;
    private Picasso mPicasso;
    private DaoSession mDaoSession;

    /**
     * Create preferencesmanager
     */
    public DataManager() {
        this.mPreferencesManager = new PreferencesManager();
        this.mContext = DevIntensiveApplication.getContext();
        this.mRestService = ServiceGenerator.createService(RestService.class);
        this.mPicasso = new PicassoCache(mContext).getPicassoInstance();
        this.mDaoSession = DevIntensiveApplication.getDaoSession();
    }

    public Picasso getPicasso() {
        return mPicasso;
    }

    /**
     * @return New datamanager or this
     */
    public static DataManager getInstanse() {
        if (INSTANCE == null) {
            INSTANCE = new DataManager();
        }
        return INSTANCE;
    }

    /**
     * @return Current preferences manager
     */
    public PreferencesManager getPreferencesManager() {
        return mPreferencesManager;
    }

    public Context getContext() {
        return mContext;
    }

    // region =============Network===============

    public Call<UserModelRes> loginUser(UserLoginReq userLoginReq) {
        return mRestService.loginUser(userLoginReq);
    }

    public Call<UserListRes> getListUserFromNetwork() {
        return mRestService.getUserList();
    }

    public Call<UploadPhotoUser> setPhotoUser(String userId, MultipartBody.Part file) {
        return mRestService.uploadPhoto(userId, file);
    }

    public Call<UploadAvatarUser> setAvatarUser(String userId, MultipartBody.Part file) {
        return mRestService.uploadAvatar(userId, file);
    }

    //end region

    // region =============Database===============

//    public Call<UserModelRes> loginUser(UserLoginReq userLoginReq){
//        return mRestService.loginUser(userLoginReq);
//    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    //end region
    public List<User> getUserListFromDb() {
        List<User> userList = new ArrayList<>();
        try {
            userList = mDaoSession.queryBuilder(User.class)
                    .where(UserDao.Properties.Rating.gt(0))
                    .orderDesc(UserDao.Properties.Rating)
                    .build()
                    .list();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }

    public void deleteUser(String query) {
        try {
            mDaoSession.queryBuilder(User.class)
                    .where(UserDao.Properties.RemoteId.eq(query))
                    .buildDelete()
                    .executeDeleteWithoutDetachingEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<User> getUserListByName(String query) {
        List<User> userList = new ArrayList<>();
        try {
            userList = mDaoSession.queryBuilder(User.class)
                    .where(UserDao.Properties.Rating.gt(0), UserDao.Properties.SearchName.like("%" + query.toUpperCase() + "%"))
                    .orderDesc(UserDao.Properties.Rating)
                    .build()
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }

    public List<Repositories> getRepositoriesByUser() {
        List<Repositories> userProfileValues = null;
        try {
            userProfileValues = mDaoSession.queryBuilder(Repositories.class)
                    .where(RepositoriesDao.Properties.UserRemoteId.eq(getPreferencesManager().getUserId()))
                    .build()
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userProfileValues;
    }
}
