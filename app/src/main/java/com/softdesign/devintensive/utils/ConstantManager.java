package com.softdesign.devintensive.utils;

/**
 * Hold constant
 */
public interface ConstantManager {
    String PREFIX_VK = "vk.com/";
    String PREFIX_GIT = "github.com/";
    String PREFIX_BASE_ACTIVITY = "BaseActivity";
    String TAG_PREFIX="DEV ";
    String TAG_JPG= "JPEG_";
    String FORMAT_JPG= ".jpg";
    String COLOR_MODE_KEY="COLOR_KEY";
    String EDIT_CONSTANT_MODE="EDIT_CONSTANT_MODE";
    String USER_PHONE_KEY = "USER_PHONE_KEY" ;
    String USER_EMAIL_KEY = "USER_EMAIL_KEY";
    String USER_VK_KEY = "USER_VK_KEY";
    String USER_GIT_KEY = "USER_GIT_KEY";
    String USER_ABOUT_KEY = "USER_ABOUT_KEY";
    String USER_PHOTO_KEY = "USER_PHOTO_KEY";
    String USER_ID_KEY = "USER_ID_KEY";
    String AUTH_TOKEN_KEY = "AUTH_TOKEN_KEY";
    String ON_CREATE = "onCreate";
    String ON_START = "onStart";
    String ON_RESUME = "onResume";
    String ON_PAUSE = "onPause";
    String ON_STOP = "onStop";
    String ON_DESTROY = "onDestroy";
    String ON_RESTART = "onRestart";
    String ERROR_SAVE_DATA = "Ошибка сохранения данных";
    String STANDART_ERROR_MESSAGE = "Error";
    String ERROR_MESSAGE_PHONE_CHECKER = "Количество символов от 11 до 20";
    String ERROR_MESSAGE_EMAIL_CHECKER = "xxx@xx.xx";
    String ERROR_MESSAGE_VK_CHECKER = "vk.com/xxx";
    String ERROR_MESSAGE_GIT_CHECKER = "github.com/xxx";
    String SCHEME_HTTPS = "https://";
    String SCHEME_PHONE = "tel:";
    String SCHEME_PACKAGE = "package:";
    String TYPE_EMAIL_MESSAGE = "plain/text";
    String TYPE_IMAGE_GALLERY = "image/*";
    String MIME_TYPE_IMAGE = "image/jpeg";
    String MESSAGE_EMAIL = "Сообщение отправляется";
    String MESSAGE_GET_PERMISSION = "Для корректной работы необходимо дать требуемые разрешения";
    String MESSAGE_CAN_USE_PERMISSION = "Разрешить";
    String SIMPLE_FORMAT_DATE = "yyyyMMdd_HHmmss";
    String BOTTOM_SLASH = "_";
    String ILLEGAL_ARGUMENT_EXCEPTION ="Given view is not a EditText";
    String FIRST_FIELD_PHONE = "00000000000";
    String FIRST_FIELD_EMAIL = "xxx@xx.xx";
    String FIRST_FIELD_VK = "vk.com/";
    String FIRST_FIELD_GIT = "github.com/";
    String FIRST_FIELD_ABOUT = "";
    String FIRST_IMAGE_AVATAR = "android.resource://com.softdesign.devintensive/drawable/avatar";
    String FIRST_USER_PHOTO = "android.resource://com.softdesign.devintensive/drawable/profile_photo";

    int LOAD_PROFILE_PHOTO = 1;
    int REQUEST_CAMERA_PICTURE = 99;
    int REQUEST_GALLERY_PICTURE = 88;
    int LENGTH_STRING_PHONE_NUMBER_MIN = 11;
    int LENGTH_STRING_PHONE_NUMBER_MAX = 20;
    int PERMISSION_REQUEST_SETTINGS_CODE = 101;
    int CAMERA_REQUEST_PERMISSION_CODE = 102;
    int PREFIX_VK_LENGTH = 7;
    int PREFIX_GIT_LENGTH = 11;
    int NULL = 0;
    int NUMBER_VIEW_IN_ARRAY_PHONE = 0;
    int NUMBER_VIEW_IN_ARRAY_EMAIL = 1;
    int NUMBER_VIEW_IN_ARRAY_VK = 2;
    int NUMBER_VIEW_IN_ARRAY_GIT = 3;
    int DELAY_TIME_SPLASH = 5000;
    int EDIT_MODE_CAN = 1;
    int EDIT_MODE_NO_CAN = 0;
    int NUMBER_VIEW_IN_ARRAY_LOAD_FROM_GALLERY = 0;
    int NUMBER_VIEW_IN_ARRAY_LOAD_FROM_CAMERA = 1;
    int NUMBER_VIEW_IN_ARRAY_CANCEL = 2;
    int NUMBER_SYMBOL_BEFORE_EMAIL = 3;
    int NUMBER_SYMBOL_BEFORE_POINT = 2;
    int NUMBER_SYMBOL_AFTER_POINT = 2;

    float RADIUS_ROUND_AVATAR_DELITEL = 2.0f;
    char EMAIL = '@';
    char POINT = '.';
    String USER_RATING_VALUE = "USER_RATING_VALUE";
    String USER_CODE_LINES_VALUE = "USER_CODE_LINES_VALUE";
    String USER_PROJECT_VALUE = "USER_PROJECT_VALUE";
    String USER_AVATAR_KEY = "USER_AVATAR_KEY";
    String USER_FIRST_NAME_KEY = "USER_FIRST_NAME_KEY";
    String USER_SECOND_NAME_KEY = "USER_SECOND_NAME_KEY";
}
