package com.softdesign.devintensive.ui.activities;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.manager.DataManager;
import com.softdesign.devintensive.data.manager.validators.BaseValidator;
import com.softdesign.devintensive.data.manager.validators.EMailChecker;
import com.softdesign.devintensive.data.manager.validators.EditTextValidator;
import com.softdesign.devintensive.data.manager.validators.LengthChecker;
import com.softdesign.devintensive.data.manager.validators.UrlChecker;
import com.softdesign.devintensive.data.manager.validators.ValidationSummary;
import com.softdesign.devintensive.data.network.res.UploadAvatarUser;
import com.softdesign.devintensive.data.network.res.UploadPhotoUser;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.storage.models.Repositories;
import com.softdesign.devintensive.data.storage.models.RepositoriesDao;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDao;
import com.softdesign.devintensive.ui.adapters.RepositoriesAdapter;
import com.softdesign.devintensive.utils.AvatarRounded;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    private static final String TAG = ConstantManager.TAG_PREFIX + "Main Activity";
    private static final int PIC_CROP = 2;

    @BindView(R.id.main_coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.nav_drawer)
    DrawerLayout mNavigationDrawer;
    @BindView(R.id.fab)
    FloatingActionButton mfab;
    @BindViews({R.id.phone_text, R.id.mail_text, R.id.vk_text, R.id.about_text})
    List<EditText> mUserInfo;
    @BindView(R.id.profile_placeholder)
    RelativeLayout mProfilePlaceholder;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.profile_user_photo)
    ImageView mProfileImage;
    @BindViews({R.id.phone_call, R.id.mail_send, R.id.vk_url})
    List<ImageView> mImageViewFields;
    @BindViews({R.id.phone_text_layout, R.id.mail_text_layout, R.id.vk_text_layout})
    List<TextInputLayout> mTextInputLayout;
    @BindViews({R.id.rating_user, R.id.code_value_user, R.id.project_value_user})
    List<TextView> mTextValuesUser;
    @BindView(R.id.repositories_list_user)
    ListView mRepositoriesView;
    private int mChancedEditMode = ConstantManager.EDIT_MODE_NO_CAN;
    private int mCurrentEditMode = ConstantManager.EDIT_MODE_CAN;
    private DataManager mDataManager;
    private AppBarLayout.LayoutParams mAppbarParams = null;
    private File mPhotoFile = null;
    private ImageView mAvatarImage;
    private TextView mUserName;
    private TextView mUserEmail;
    private Float aFloat;
    private RepositoriesDao mRepositoriesDao;
    private UserDao mUserDao;
    private static int mWidthWindows;

    public static int getmWidthWindows() {
        return mWidthWindows;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * @param savedInstanceState save instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mDataManager = DataManager.getInstanse();
        aFloat = getResources().getDisplayMetrics().density;

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metricsB = new DisplayMetrics();
        display.getMetrics(metricsB);
        mWidthWindows = metricsB.widthPixels;

        setupToolbar();
        initDrawer();
        initUserFields();
        if (savedInstanceState != null) {
            mCurrentEditMode = savedInstanceState.getInt(ConstantManager.EDIT_CURRENT_MODE, ConstantManager.NULL);
            mChancedEditMode = savedInstanceState.getInt(ConstantManager.EDIT_CONSTANT_MODE, ConstantManager.NULL);
            changeEditMode(mCurrentEditMode);
        }
    }

    /**
     * @param outState save data for app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ConstantManager.EDIT_CURRENT_MODE, mCurrentEditMode);
        outState.putInt(ConstantManager.EDIT_CONSTANT_MODE, mChancedEditMode);
    }

    /**
     * Setup toolbar on place actionbar
     */
    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        mAppbarParams = (AppBarLayout.LayoutParams) mCollapsingToolbar.getLayoutParams();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_list_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    /**
     * @param item item dialog menu for download on screen
     * @return true if not null
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Setup Drawer
     */
    private void initDrawer() {
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getTitle().toString()) {
                    case ConstantManager.MENU_ITEM_MY_PROFILE:
                        break;
                    case ConstantManager.MENU_ITEM_TEAM:
                        Intent intentCustom = new Intent(MainActivity.this, CustomSortListActivity.class);
                        startActivity(intentCustom);
                        break;
                    case ConstantManager.MENU_ITEM_RATING:
                        Intent intent = new Intent(MainActivity.this, UserListActivity.class);
                        startActivity(intent);
                        break;
                    case ConstantManager.MENU_ITEM_REFRESH:
                        refreshBd();
                        break;
                    case ConstantManager.MENU_ITEM_EXIT:
                        Intent startActivity = new Intent(MainActivity.this,AuthActivity.class);
                        mDataManager.getPreferencesManager().saveAuthToken(null);
                        mDataManager.getPreferencesManager().saveUserId(null);
                        startActivity(startActivity);
                        break;
                }
                item.setCheckable(true);
                mNavigationDrawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
        mAvatarImage = (ImageView) navigationView.getHeaderView(ConstantManager.NULL).findViewById(R.id.avatar_image);
        mUserName = (TextView) navigationView.getHeaderView(ConstantManager.NULL).findViewById(R.id.user_name_nav);
        mUserEmail = (TextView) navigationView.getHeaderView(ConstantManager.NULL).findViewById(R.id.user_mail_nav);
    }

    private void refreshBd(){
        Call<UserListRes> call = mDataManager.getListUserFromNetwork();
        call.enqueue(new Callback<UserListRes>() {
            @Override
            public void onResponse(Call<UserListRes> call, Response<UserListRes> response) {
                try {
                    if (response.code() == 200) {
                        List<Repositories> allRepositories = new ArrayList<Repositories>();
                        List<User> allUsers = new ArrayList<User>();
                        mUserDao = mDataManager.getDaoSession().getUserDao();
                        mRepositoriesDao = mDataManager.getDaoSession().getRepositoriesDao();
                        for (UserListRes.Datum userRes : response.body().getData()) {
                            allRepositories.addAll(getRepoListFromUserRes(userRes));
                            allUsers.add(new User(userRes));
                        }
                        mRepositoriesDao.insertOrReplaceInTx(allRepositories);
                        mUserDao.insertOrReplaceInTx(allUsers);
                    } else {
                        Snackbar.make(mNavigationDrawer, ConstantManager.LIST_USER_NOT_CAN_GET, Snackbar.LENGTH_LONG).show();
                    }

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Snackbar.make(mNavigationDrawer, ConstantManager.STANDART_ERROR_MESSAGE, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserListRes> call, Throwable t) {
                Snackbar.make(mNavigationDrawer, ConstantManager.STANDART_ERROR_MESSAGE, Snackbar.LENGTH_LONG).show();
            }

        });
    }

    private List<Repositories> getRepoListFromUserRes(UserListRes.Datum userData) {
        final String userId = userData.getId();
        List<Repositories> repositories = new ArrayList<>();
        for (UserListRes.Repo repositoryRes : userData.getRepositories().getRepo()) {
            repositories.add(new Repositories(repositoryRes, userId));
        }
        return repositories;
    }

    @OnClick(R.id.fab)
    void editMode() {
        if (mChancedEditMode == ConstantManager.EDIT_MODE_NO_CAN) {
            changeEditMode(ConstantManager.EDIT_MODE_NO_CAN);
            mChancedEditMode = ConstantManager.EDIT_MODE_CAN;
        } else {
            Boolean isEditTextCompleted = verificationDataProfile();
            if (isEditTextCompleted) {
                changeEditMode(ConstantManager.EDIT_MODE_CAN);
                mChancedEditMode = ConstantManager.EDIT_MODE_NO_CAN;
            }
        }
    }

    /**
     * @param mode Which mode select for edit
     */
    private void changeEditMode(int mode) {
        if (mode == ConstantManager.EDIT_MODE_NO_CAN) {
            mCurrentEditMode = ConstantManager.EDIT_MODE_NO_CAN;
            mfab.setImageResource(R.drawable.ic_done_black_24dp);
            for (EditText editText : mUserInfo) {
                editText.setFocusable(true);
                editText.setEnabled(true);
                editText.setFocusableInTouchMode(true);
            }
            mUserInfo.get(ConstantManager.NUMBER_VIEW_IN_ARRAY_PHONE).requestFocus();
            mProfilePlaceholder.setVisibility(View.VISIBLE);
//            lockToolbar();
            mCollapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);

        } else {
            mCurrentEditMode = ConstantManager.EDIT_MODE_CAN;
            mfab.setImageResource(R.drawable.ic_create_black_24dp);
            for (EditText editText : mUserInfo) {
                editText.setFocusable(false);
                editText.setEnabled(false);
                editText.setFocusableInTouchMode(false);
            }
            mProfilePlaceholder.setVisibility(View.GONE);
            saveUserFields();
//            unLockToolbar();
            mCollapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.white));
        }
    }


    /**
     * Load user info in app
     */
    private void initUserFields() {
        List<String> userInfoFields = mDataManager.getPreferencesManager().loadUserProfileData();
        for (int i = 0; i < 4; i++) {
            mUserInfo.get(i).setText(userInfoFields.get(i));
        }
        for (int i = 0; i < 3; i++) {
            mTextValuesUser.get(i).setText(userInfoFields.get(i + 4));
        }
        mUserName.setText(userInfoFields.get(9));
        mUserEmail.setText(userInfoFields.get(1));
        insertProfileImage(mDataManager.getPreferencesManager().loadUserPhoto());
        loadAvatar(mDataManager.getPreferencesManager().loadAvatarImage());
        initGit();
    }

    private void initGit() {
        final List<String> repositories = new ArrayList<>();
        for (int i = 0; i < mDataManager.getRepositoriesByUser().size(); i++) {
            repositories.add(mDataManager.getRepositoriesByUser().get(i).getRepositoryName());
        }
        final RepositoriesAdapter repositoriesAdapter = new RepositoriesAdapter(this, repositories);
        mRepositoriesView.setAdapter(repositoriesAdapter);
        RepositoriesAdapter.setListViewHeightBasedOnChildren(mRepositoriesView);
        mRepositoriesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri uri = Uri.parse(ConstantManager.SCHEME_HTTPS + repositories.get(position));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    /**
     * Save uset info from app
     */
    private void saveUserFields() {
        List<String> userInfoFields = new ArrayList<>();
        for (EditText userField : mUserInfo) {
            userInfoFields.add(userField.getText().toString());
        }
        for (TextView userValues : mTextValuesUser) {
            userInfoFields.add(userValues.getText().toString());
        }
        mDataManager.getPreferencesManager().saveUserProfileData(userInfoFields);
    }


    /**
     * @return true if all fields is correct
     */
    private boolean verificationDataProfile() {
        ValidationSummary validationSummary = new ValidationSummary(ConstantManager.ERROR_SAVE_DATA);

        EditTextValidator firstETV = new EditTextValidator();
        firstETV.setViewToValidate(mUserInfo.get(ConstantManager.NUMBER_VIEW_IN_ARRAY_PHONE), BaseValidator.ValidationMode.Manual);
        firstETV.setExternalErrorView(mTextInputLayout.get(ConstantManager.NUMBER_VIEW_IN_ARRAY_PHONE), this);
        firstETV.addConditionChecker(new LengthChecker(ConstantManager.LENGTH_STRING_PHONE_NUMBER_MIN, ConstantManager.LENGTH_STRING_PHONE_NUMBER_MAX, ConstantManager.ERROR_MESSAGE_PHONE_CHECKER));
        validationSummary.addValidator(firstETV);

        EditTextValidator secondETV = new EditTextValidator();
        secondETV.setViewToValidate(mUserInfo.get(ConstantManager.NUMBER_VIEW_IN_ARRAY_EMAIL), BaseValidator.ValidationMode.Manual);
        secondETV.setExternalErrorView(mTextInputLayout.get(ConstantManager.NUMBER_VIEW_IN_ARRAY_EMAIL), this);
        secondETV.addConditionChecker(new EMailChecker(ConstantManager.ERROR_MESSAGE_EMAIL_CHECKER));
        validationSummary.addValidator(secondETV);

        EditTextValidator thirdETV = new EditTextValidator();
        thirdETV.setViewToValidate(mUserInfo.get(ConstantManager.NUMBER_VIEW_IN_ARRAY_VK), BaseValidator.ValidationMode.Manual);
        thirdETV.setExternalErrorView(mTextInputLayout.get(ConstantManager.NUMBER_VIEW_IN_ARRAY_VK), this);
        thirdETV.addConditionChecker(new UrlChecker(ConstantManager.ERROR_MESSAGE_VK_CHECKER, ConstantManager.PREFIX_VK, ConstantManager.NULL, ConstantManager.PREFIX_VK_LENGTH));
        validationSummary.addValidator(thirdETV);

//        EditTextValidator fourthETV = new EditTextValidator();
//        fourthETV.setViewToValidate(mUserInfo.get(ConstantManager.NUMBER_VIEW_IN_ARRAY_GIT), BaseValidator.ValidationMode.Manual);
//        fourthETV.setExternalErrorView(mTextInputLayout.get(ConstantManager.NUMBER_VIEW_IN_ARRAY_GIT), this);
//        fourthETV.addConditionChecker(new UrlChecker(ConstantManager.ERROR_MESSAGE_GIT_CHECKER, ConstantManager.PREFIX_GIT, ConstantManager.NULL, ConstantManager.PREFIX_GIT_LENGTH));
//        validationSummary.addValidator(fourthETV);

        validationSummary.performCheck(mCoordinatorLayout);

        return validationSummary.isCorrect();
    }


    @OnClick(R.id.profile_placeholder)
    void loadPhoto() {
        showDialog(ConstantManager.LOAD_PROFILE_PHOTO);
    }

    @OnClick(R.id.phone_call)
    void callPhoneUser() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(ConstantManager.SCHEME_PHONE + mUserInfo.get(ConstantManager.NUMBER_VIEW_IN_ARRAY_PHONE).getText()));
        startActivity(intent);
    }

    @OnClick(R.id.mail_send)
    void sendMailToUser() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(ConstantManager.TYPE_EMAIL_MESSAGE);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{mUserInfo.get(ConstantManager.NUMBER_VIEW_IN_ARRAY_EMAIL).getText().toString()});
        startActivity(Intent.createChooser(intent, ConstantManager.MESSAGE_EMAIL));
    }

    @OnClick(R.id.vk_url)
    void openVkUri() {
        Uri uri = Uri.parse(ConstantManager.SCHEME_HTTPS + mUserInfo.get(ConstantManager.NUMBER_VIEW_IN_ARRAY_VK).getText());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    /**
     * Exit from navbar and from app
     */
    @Override
    public void onBackPressed() {
        if (mNavigationDrawer != null && mNavigationDrawer.isDrawerVisible(GravityCompat.START)) {
            mNavigationDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Load photo from gallery
     */


//    /**
//     * Lock toolbar
//     */
//    private void lockToolbar() {
//        mAppBarLayout.setExpanded(true, true);
//        mAppbarParams.setScrollFlags(ConstantManager.NULL);
//        mCollapsingToolbar.setLayoutParams(mAppbarParams);
//
//    }
//
//    /**
//     * Unlock toolbar
//     */
//    private void unLockToolbar() {
//        mAppbarParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
//        mCollapsingToolbar.setLayoutParams(mAppbarParams);
//    }

    /**
     * @param id Id item from dialog for load user photo
     * @return Dialog with all item
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case ConstantManager.LOAD_PROFILE_PHOTO:
                String[] selectItem = {getString(R.string.user_dialog_gallery), getString(R.string.user_dialog_camera), getString(R.string.user_dialog_cancel)};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.user_dialog_title));
                builder.setItems(selectItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case ConstantManager.NUMBER_VIEW_IN_ARRAY_LOAD_FROM_GALLERY:
                                loadPhotoFromGallery();
                                break;
                            case ConstantManager.NUMBER_VIEW_IN_ARRAY_LOAD_FROM_CAMERA:
                                loadPhotoFromCamera();
                                break;
                            case ConstantManager.NUMBER_VIEW_IN_ARRAY_CANCEL:
                                dialog.cancel();
                                break;
                        }
                    }
                });
                return builder.create();
            default:
                return null;
        }
    }

    protected void loadPhotoFromGallery() {
        Intent takeGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        takeGalleryIntent.setType(ConstantManager.TYPE_IMAGE_GALLERY);
        startActivityForResult(Intent.createChooser(takeGalleryIntent, getString(R.string.set_photo_from_gallery)), ConstantManager.REQUEST_GALLERY_PICTURE);
    }

    /**
     * Load photo from camera
     */
    protected void loadPhotoFromCamera() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent intentStartCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                mPhotoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (mPhotoFile != null) {
                intentStartCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
                startActivityForResult(intentStartCamera, ConstantManager.REQUEST_CAMERA_PICTURE);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, ConstantManager.CAMERA_REQUEST_PERMISSION_CODE);

            Snackbar.make(mCoordinatorLayout, ConstantManager.MESSAGE_GET_PERMISSION, Snackbar.LENGTH_LONG).setAction(ConstantManager.MESSAGE_CAN_USE_PERMISSION, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse(ConstantManager.SCHEME_PACKAGE + getPackageName()));
                    startActivityForResult(appSettingsIntent, ConstantManager.PERMISSION_REQUEST_SETTINGS_CODE);
                }
            }).show();
        }
    }


    private void uploadPhoto(Uri uri) {
        File file = new File(getFilePathCorrect(uri));
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);
        Call<UploadPhotoUser> call = mDataManager.setPhotoUser(mDataManager.getPreferencesManager().getUserId(), body);
        call.enqueue(new Callback<UploadPhotoUser>() {
            @Override
            public void onResponse(Call<UploadPhotoUser> call, Response<UploadPhotoUser> response) {
                Log.e("Загрузка фото успешна", "");
            }

            @Override
            public void onFailure(Call<UploadPhotoUser> call, Throwable t) {
                Log.e(ConstantManager.STANDART_ERROR_MESSAGE, "");
            }
        });
    }

    private String getFilePathCorrect(Uri uri) {
        String filePath;
        if ("content".equals(uri.getScheme())) {
            Cursor cursor = this.getContentResolver().query(uri, new String[]{android.provider.MediaStore.Images.ImageColumns.DATA}, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();
            filePath = cursor.getString(0);
            cursor.close();
        } else {
            filePath = uri.getPath();
        }
        return filePath;
    }

    /**
     * @param requestCode  Code request
     * @param permissions  Permissions
     * @param grantResults Result permission granted/non granted
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ConstantManager.CAMERA_REQUEST_PERMISSION_CODE && grantResults.length == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
            if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }

    /**
     * @return Created photo file
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        String timeNameImageFile = new SimpleDateFormat(ConstantManager.SIMPLE_FORMAT_DATE).format(new Date());
        String imageFileName = ConstantManager.TAG_JPG + timeNameImageFile + ConstantManager.BOTTOM_SLASH;
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        storageDir.mkdirs();
        File image = File.createTempFile(imageFileName, ConstantManager.FORMAT_JPG, storageDir);
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, ConstantManager.MIME_TYPE_IMAGE);
        contentValues.put(MediaStore.MediaColumns.DATA, image.getAbsolutePath());
        this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        return image;
    }

    /**
     * @param UriImage Selected image for load in user photo place
     */
    private void insertProfileImage(final Uri UriImage) {
        mDataManager.getPicasso()
                .load(UriImage)
                .centerCrop()
                .resize((int) (getResources().getDimension(R.dimen.profile_image_size) * aFloat * ConstantManager.KOFF_PROPORTION_SIZE), (int) (getResources().getDimension(R.dimen.profile_image_size) * aFloat))
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(mProfileImage, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, ConstantManager.LOAD_FROM_CACHE + " photo");
                    }

                    @Override
                    public void onError() {
                        Picasso.with(MainActivity.this)
                                .load(UriImage)
                                .centerCrop()
                                .resize((int) (getResources().getDimension(R.dimen.profile_image_size) * aFloat * ConstantManager.KOFF_PROPORTION_SIZE), (int) (getResources().getDimension(R.dimen.profile_image_size) * aFloat))
                                .into(mProfileImage, new com.squareup.picasso.Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Log.d(TAG, ConstantManager.ERROR_LOAD_FROM_CACHE + " photo");
                                    }
                                });
                    }
                });
        mDataManager.getPreferencesManager().saveUserPhoto(UriImage);
    }

    /**
     * @param requestCode code request
     * @param resultCode  code result
     * @param data        data
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ConstantManager.REQUEST_GALLERY_PICTURE:
                if (resultCode == RESULT_OK && data != null) {
                    uploadPhoto(data.getData());
                    performCrop(data.getData());
                    insertProfileImage(data.getData());
                }
                break;
            case ConstantManager.REQUEST_CAMERA_PICTURE:
                if (resultCode == RESULT_OK && mPhotoFile != null) {
                    uploadPhoto(Uri.fromFile(mPhotoFile));
                    performCrop(Uri.fromFile(mPhotoFile));
                    insertProfileImage(Uri.fromFile(mPhotoFile));
                }
                break;
            case PIC_CROP:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    Bitmap thePic = extras.getParcelable("data");
                    getImageUri(this, thePic);
                } else {
                    // TODO: 18.07.2016 обработка ошибок связанных с незагрузкой аватара
                }
                break;
        }
    }

    private void uploadAvatar(Uri uri) {
        File file = new File(getFilePathCorrect(uri));
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);
        Call<UploadAvatarUser> call = mDataManager.setAvatarUser(mDataManager.getPreferencesManager().getUserId(), body);
        call.enqueue(new Callback<UploadAvatarUser>() {
            @Override
            public void onResponse(Call<UploadAvatarUser> call, Response<UploadAvatarUser> response) {
                Log.e("Загрузка фото успешна", "");
            }

            @Override
            public void onFailure(Call<UploadAvatarUser> call, Throwable t) {
                Log.e("Неудача", "");
            }
        });
    }

    private void getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, ConstantManager.QUALITY_BITMAP_AVATAR, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        Uri mUri = Uri.parse(path);
        loadAvatar(mUri);
        uploadAvatar(mUri);
    }

    private void loadAvatar(final Uri picUri) {
        mDataManager.getPicasso()
                .load(picUri)
                .placeholder(R.drawable.avatar)
                .transform(new AvatarRounded())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(mAvatarImage, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, ConstantManager.LOAD_FROM_CACHE + " avatar");
                    }

                    @Override
                    public void onError() {
                        Picasso.with(MainActivity.this)
                                .load(picUri)
                                .placeholder(R.drawable.avatar)
                                .transform(new AvatarRounded())
                                .into(mAvatarImage, new com.squareup.picasso.Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Log.d(TAG, ConstantManager.ERROR_LOAD_FROM_CACHE + " avatar");
                                    }
                                });
                    }
                });
        mDataManager.getPreferencesManager().saveAvatarImage(picUri);
    }

    private void performCrop(Uri uri) {
        try {
            String filePath;
            if ("content".equals(uri.getScheme())) {
                Cursor cursor = this.getContentResolver().query(uri, new String[]{android.provider.MediaStore.Images.ImageColumns.DATA}, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();
                filePath = "file://" + cursor.getString(0);
                cursor.close();
            } else {
                filePath = "file://" + uri.getPath();
            }
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(Uri.parse(filePath), "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 120);
            cropIntent.putExtra("outputY", 120);
            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, PIC_CROP);
        } catch (ActivityNotFoundException anfe) {
            String errorMessage = "Не поддерживает crop";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * @param message message for snackbar
     */
    private void showSnackBar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }
}