package com.softdesign.devintensive.ui.activities;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.manager.DataManager;
import com.softdesign.devintensive.data.manager.validators.BaseValidator;
import com.softdesign.devintensive.data.manager.validators.EMailChecker;
import com.softdesign.devintensive.data.manager.validators.EditTextValidator;
import com.softdesign.devintensive.data.manager.validators.LengthChecker;
import com.softdesign.devintensive.data.manager.validators.UrlChecker;
import com.softdesign.devintensive.data.manager.validators.ValidationSummary;
import com.softdesign.devintensive.data.network.PhotoUploadService;
import com.softdesign.devintensive.data.network.ServiceGenerator;
import com.softdesign.devintensive.utils.AvatarRounded;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = ConstantManager.TAG_PREFIX + "Main Activity";
    @BindView(R.id.main_coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.nav_drawer)
    DrawerLayout mNavigationDrawer;
    @BindView(R.id.fab)
    FloatingActionButton mfab;
    @BindViews({R.id.phone_text, R.id.mail_text, R.id.vk_text, R.id.github_text, R.id.about_text})
    List<EditText> mUserInfo;
    @BindView(R.id.profile_placeholder)
    RelativeLayout mProfilePlaceholder;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.profile_user_photo)
    ImageView mProfileImage;
    @BindView(R.id.grey_splash)
    LinearLayout mGreySplash;
    @BindViews({R.id.phone_call, R.id.mail_send, R.id.vk_url, R.id.git_url})
    List<ImageView> mImageViewFields;
    @BindViews({R.id.phone_text_layout, R.id.mail_text_layout, R.id.vk_text_layout, R.id.github_text_layout})
    List<TextInputLayout> mTextInputLayout;
    @BindViews({R.id.rating_user, R.id.code_value_user, R.id.project_value_user})
    List<TextView> mTextValuesUser;
    private int mCurrentEditMode = ConstantManager.EDIT_MODE_CAN;
    private DataManager mDataManager;
    private AppBarLayout.LayoutParams mAppbarParams = null;
    private File mPhotoFile = null;
    private Uri mSelectedImage = null;
    private Boolean mIsEditTextCompleted;
    private ImageView mAvatarImage;


    /**
     * @param savedInstanceState save instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mDataManager = DataManager.getInstanse();
        for (int i = 0; i < mImageViewFields.size(); i++) {
            mImageViewFields.get(i).setOnClickListener(this);
        }
        mProfilePlaceholder.setOnClickListener(this);
        mfab.setOnClickListener(this);
        Picasso.with(this)
                .load(mDataManager.getPreferencesManager().loadUserPhoto())
                .placeholder(R.drawable.profile_photo)// TODO: 30.06.2016 Сделать пласехолдер transform + crop
                .into(mProfileImage);
        setupToolbar();
        setupDrawer();
        loadAvatar();
        initUserFields();
        initUserInfoValues();
        Log.d(TAG, ConstantManager.ON_CREATE);
        if (savedInstanceState == null) {
        } else {
            mCurrentEditMode = savedInstanceState.getInt(ConstantManager.EDIT_CONSTANT_MODE, ConstantManager.NULL);
            changeEditMode(mCurrentEditMode);
        }
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
                    mSelectedImage = data.getData();
                    insertProfileImage(mSelectedImage);
                }
                break;
            case ConstantManager.REQUEST_CAMERA_PICTURE:
                if (resultCode == RESULT_OK && mPhotoFile != null) {
                    mSelectedImage = Uri.fromFile(mPhotoFile);
                    insertProfileImage(mSelectedImage);
                }
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
     * Start activity
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, ConstantManager.ON_START);

    }

    /**
     * Resume activity
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, ConstantManager.ON_RESUME);

    }

    /**
     * Pause activity
     */
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, ConstantManager.ON_PAUSE);
    }

    /**
     * Stop activity
     */
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, ConstantManager.ON_STOP);
    }

    /**
     * Destroy activity
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, ConstantManager.ON_DESTROY);
    }

    /**
     * Restart activity
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, ConstantManager.ON_RESTART);
    }

    /**
     * @param outState save data for app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ConstantManager.EDIT_CONSTANT_MODE, mCurrentEditMode);
    }

    /**
     * Run action with delay
     */
    private void runWithDelay() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgress();
            }
        }, ConstantManager.DELAY_TIME_SPLASH);
    }

    /**
     * @param message message for snackbar
     */
    private void showSnackBar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
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
     * Setup Drawer
     */
    private void setupDrawer() {
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                showSnackBar(item.getTitle().toString());
                item.setCheckable(true);
                mNavigationDrawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
        mAvatarImage = (ImageView) navigationView.getHeaderView(ConstantManager.NULL).findViewById(R.id.avatar_image);
        TextView mUserName = (TextView) navigationView.getHeaderView(ConstantManager.NULL).findViewById(R.id.user_name_nav);
        TextView mUserEmail = (TextView) navigationView.getHeaderView(ConstantManager.NULL).findViewById(R.id.user_mail_nav);
        mUserEmail.setText(mDataManager.getPreferencesManager().loadUserProfileData().get(ConstantManager.NUMBER_VIEW_IN_ARRAY_EMAIL));
        mDataManager.getPreferencesManager().loadFirstSecondNameUser();
        mUserName.setText(String.format("%s %s", mDataManager.getPreferencesManager().loadFirstSecondNameUser().get(0), mDataManager.getPreferencesManager().loadFirstSecondNameUser().get(1)));
    }

    private void loadAvatar() {
        Picasso.with(this)
                .load(mDataManager.getPreferencesManager().loadAvatarImage())
                .placeholder(R.drawable.avatar)// TODO: 30.06.2016 Сделать пласехолдер transform + crop
                .transform(new AvatarRounded())
                .into(mAvatarImage);
//        Bitmap bitmap = mAvatarImage.getDrawingCache();
//        RoundedBitmapDrawable rBD = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
//        rBD.setCornerRadius(getResources().getDimension(R.dimen.height_width_avatar)/ConstantManager.RADIUS_ROUND_AVATAR_DELITEL);
//        mAvatarImage.setImageDrawable(rBD);
    }

    /**
     * @param mode Which mode select for edit
     */
    private void changeEditMode(int mode) {
        if (mode == ConstantManager.EDIT_MODE_CAN) {
            mfab.setImageResource(R.drawable.ic_done_black_24dp);
            for (EditText editText : mUserInfo) {
                editText.setFocusable(true);
                editText.setEnabled(true);
                editText.setFocusableInTouchMode(true);
            }
            mUserInfo.get(ConstantManager.NUMBER_VIEW_IN_ARRAY_PHONE).requestFocus();
            showProfilePlaceholder();
            lockToolbar();
            mCollapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);

        } else {
            mfab.setImageResource(R.drawable.ic_create_black_24dp);
            for (EditText editText : mUserInfo) {
                editText.setFocusable(false);
                editText.setEnabled(false);
                editText.setFocusableInTouchMode(false);
            }
            hideProfilePlaceholder();
            saveUserFields();
            unLockToolbar();
            mCollapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.white));
        }
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

        EditTextValidator fourthETV = new EditTextValidator();
        fourthETV.setViewToValidate(mUserInfo.get(ConstantManager.NUMBER_VIEW_IN_ARRAY_GIT), BaseValidator.ValidationMode.Manual);
        fourthETV.setExternalErrorView(mTextInputLayout.get(ConstantManager.NUMBER_VIEW_IN_ARRAY_GIT), this);
        fourthETV.addConditionChecker(new UrlChecker(ConstantManager.ERROR_MESSAGE_GIT_CHECKER, ConstantManager.PREFIX_GIT, ConstantManager.NULL, ConstantManager.PREFIX_GIT_LENGTH));
        validationSummary.addValidator(fourthETV);

        validationSummary.performCheck(mCoordinatorLayout);

        return validationSummary.isCorrect();
    }

    /**
     * Load user info in app
     */
    private void initUserFields() {
        List<String> userInfoFields = mDataManager.getPreferencesManager().loadUserProfileData();
        for (int i = 0; i < userInfoFields.size(); i++) {
            mUserInfo.get(i).setText(userInfoFields.get(i));
        }
    }

    /**
     * Save uset info from app
     */
    private void saveUserFields() {
        List<String> userInfoFields = new ArrayList<>();
        for (EditText userField : mUserInfo) {
            userInfoFields.add(userField.getText().toString());
        }
        mDataManager.getPreferencesManager().saveUserProfileData(userInfoFields);

    }

    private void InfoUser() {

    }

    /**
     * @param v View which is pressed
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                if (mCurrentEditMode == ConstantManager.EDIT_MODE_NO_CAN) {
                    mIsEditTextCompleted = verificationDataProfile();
                    if (mIsEditTextCompleted) {
                        changeEditMode(ConstantManager.EDIT_MODE_NO_CAN);
                        mCurrentEditMode = ConstantManager.EDIT_MODE_CAN;
                    }
                } else {
                    changeEditMode(ConstantManager.EDIT_MODE_CAN);
                    mCurrentEditMode = ConstantManager.EDIT_MODE_NO_CAN;
                }
                break;
            case R.id.profile_placeholder:
                // TODO: 29.06.2016 Сделать загрузку фото из галлереи или с камеры
                showDialog(ConstantManager.LOAD_PROFILE_PHOTO);
                break;
            case R.id.phone_call:
                callPhoneUser();
                break;
            case R.id.mail_send:
                sendMailToUser();
                break;
            case R.id.vk_url:
                openUrl(mUserInfo.get(ConstantManager.NUMBER_VIEW_IN_ARRAY_VK));
                break;
            case R.id.git_url:
                openUrl(mUserInfo.get(ConstantManager.NUMBER_VIEW_IN_ARRAY_GIT));
                break;
        }

    }

    /**
     * @param editText Get ref from current field
     */
    private void openUrl(EditText editText) {
        Uri uri = Uri.parse(ConstantManager.SCHEME_HTTPS + editText.getText());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    /**
     * Send message on e-mail
     */
    private void sendMailToUser() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(ConstantManager.TYPE_EMAIL_MESSAGE);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{mUserInfo.get(ConstantManager.NUMBER_VIEW_IN_ARRAY_EMAIL).getText().toString()});
        startActivity(Intent.createChooser(intent, ConstantManager.MESSAGE_EMAIL));
    }

    /**
     * Call user on phone number
     */
    private void callPhoneUser() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(ConstantManager.SCHEME_PHONE + mUserInfo.get(ConstantManager.NUMBER_VIEW_IN_ARRAY_PHONE).getText()));
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
    private void loadPhotoFromGallery() {
        Intent takeGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        takeGalleryIntent.setType(ConstantManager.TYPE_IMAGE_GALLERY);
        startActivityForResult(Intent.createChooser(takeGalleryIntent, getString(R.string.set_photo_from_gallery)), ConstantManager.REQUEST_GALLERY_PICTURE);
    }

    /**
     * Load photo from camera
     */
    private void loadPhotoFromCamera() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent takeCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                mPhotoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
                // TODO: 30.06.2016 Обработка ошибок связанных с чтением
            }

            if (mPhotoFile != null) {
                takeCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
                startActivityForResult(takeCaptureIntent, ConstantManager.REQUEST_CAMERA_PICTURE);
//                uploadPhoto(mPhotoFile.toURI());// TODO: 30.06.2016 Передать фото в интент
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, ConstantManager.CAMERA_REQUEST_PERMISSION_CODE);
        }

        Snackbar.make(mCoordinatorLayout, ConstantManager.MESSAGE_GET_PERMISSION, Snackbar.LENGTH_LONG).setAction(ConstantManager.MESSAGE_CAN_USE_PERMISSION, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openApplicationSetting();
            }
        }).show();
    }

    private void uploadPhoto(URI uri) {
        // create upload service client
        PhotoUploadService service = ServiceGenerator.createService(PhotoUploadService.class);
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        File file = new File(uri);
        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("picture", file.getName(), requestFile);
        // add another part within the multipart request
        String descriptionString = "hello, this is description speaking";
        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString);
        // finally, execute the request
        Call<ResponseBody> call = service.upload(description, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.v("Upload", "success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
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
                // TODO: 30.06.2016 Тут обрабатываем разрешения (разрешение получено) например вывести сообщение
            }
            if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // TODO: 30.06.2016 Тут обрабатываем разрешения (разрешение получено) например вывести сообщение
            }
        }
    }

    /**
     * Hide place for user photo
     */
    private void hideProfilePlaceholder() {
        mProfilePlaceholder.setVisibility(View.GONE);
    }

    /**
     * Show place for user photo
     */
    private void showProfilePlaceholder() {
        mProfilePlaceholder.setVisibility(View.VISIBLE);
    }

    /**
     * Lock toolbar
     */
    private void lockToolbar() {
        mAppBarLayout.setExpanded(true, true);
        mAppbarParams.setScrollFlags(ConstantManager.NULL);
        mCollapsingToolbar.setLayoutParams(mAppbarParams);

    }

    /**
     * Unlock toolbar
     */
    private void unLockToolbar() {
        mAppbarParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        mCollapsingToolbar.setLayoutParams(mAppbarParams);
    }

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

    /**
     * @return Created photo file
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        String timeNameImageFile = new SimpleDateFormat(ConstantManager.SIMPLE_FORMAT_DATE).format(new Date());
        String imageFileName = ConstantManager.TAG_JPG + timeNameImageFile + ConstantManager.BOTTOM_SLASH;
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ConstantManager.FORMAT_JPG, storageDir);
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, ConstantManager.MIME_TYPE_IMAGE);
        contentValues.put(MediaStore.MediaColumns.DATA, image.getAbsolutePath());
        this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        return image;
    }

    /**
     * @param selectedImage Selected image for load in user photo place
     */
    private void insertProfileImage(Uri selectedImage) {
        Picasso.with(this)
                .load(selectedImage)
                .into(mProfileImage);
        // TODO: 30.06.2016 Сделать пласехолдер transform + crop
        mDataManager.getPreferencesManager().saveUserPhoto(selectedImage);
    }

    /**
     * Open application setting
     */
    public void openApplicationSetting() {
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse(ConstantManager.SCHEME_PACKAGE + getPackageName()));
        startActivityForResult(appSettingsIntent, ConstantManager.PERMISSION_REQUEST_SETTINGS_CODE);
    }

    private void initUserInfoValues() {
        List<String> list = mDataManager.getPreferencesManager().loadUserProfileValues();
        for (int i = 0; i < list.size(); i++) {
            mTextValuesUser.get(i).setText(list.get(i));
        }

    }

}