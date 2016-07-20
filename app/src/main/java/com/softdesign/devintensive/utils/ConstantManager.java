package com.softdesign.devintensive.utils;

/**
 * Hold constant
 */
public interface ConstantManager {
    String PREFIX_VK = "vk.com/";
    String PREFIX_GIT = "github.com/";
    String PREFIX_BASE_ACTIVITY = "Base Activity";
    String TAG_PREFIX = "DEV ";
    String TAG_JPG = "JPEG_";
    String FORMAT_JPG = ".jpg";
    String EDIT_CONSTANT_MODE = "EDIT_CONSTANT_MODE";
    String EDIT_CURRENT_MODE = "EDIT_CURRENT_MODE";
    String USER_PHONE_KEY = "USER_PHONE_KEY";
    String USER_EMAIL_KEY = "USER_EMAIL_KEY";
    String USER_VK_KEY = "USER_VK_KEY";
    String USER_GIT_KEY = "USER_GIT_KEY";
    String USER_ABOUT_KEY = "USER_ABOUT_KEY";
    String USER_PHOTO_KEY = "USER_PHOTO_KEY";
    String USER_ID_KEY = "USER_ID_KEY";
    String AUTH_TOKEN_KEY = "AUTH_TOKEN_KEY";
    String USER_RATING_VALUE = "USER_RATING_VALUE";
    String USER_CODE_LINES_VALUE = "USER_CODE_LINES_VALUE";
    String USER_PROJECT_VALUE = "USER_PROJECT_VALUE";
    String USER_AVATAR_KEY = "USER_AVATAR_KEY";
    String USER_FULL_NAME_KEY = "USER_FULL_NAME_KEY";
    String PARCELABLER_KEY = "PARCELABLER_KEY";

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

    String SIMPLE_FORMAT_DATE = "yyyyMMdd_HHmmss";
    String BOTTOM_SLASH = "_";
    String ILLEGAL_ARGUMENT_EXCEPTION = "Given view is not a EditText";
    String FIRST_FIELD_PHONE = "00000000000";
    String FIRST_FIELD_EMAIL = "xxx@xx.xx";
    String FIRST_FIELD_VK = "vk.com/";
    String FIRST_FIELD_GIT = "github.com/";
    String FIRST_FIELD_ABOUT = "";
    String FIRST_IMAGE_AVATAR = "android.resource://com.softdesign.devintensive/drawable/avatar";
    String FIRST_USER_PHOTO = "android.resource://com.softdesign.devintensive/drawable/profile_photo";
    String DEVINTENSIV_FOR_GOT_PASSWORD = "http://devintensive.softdesign-apps.ru/forgotpass";

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
    int EDIT_MODE_CAN = 1;
    int EDIT_MODE_NO_CAN = 0;
    int NUMBER_VIEW_IN_ARRAY_LOAD_FROM_GALLERY = 0;
    int NUMBER_VIEW_IN_ARRAY_LOAD_FROM_CAMERA = 1;
    int NUMBER_VIEW_IN_ARRAY_CANCEL = 2;
    int NUMBER_SYMBOL_BEFORE_EMAIL = 3;
    int NUMBER_SYMBOL_BEFORE_POINT = 2;
    int NUMBER_SYMBOL_AFTER_POINT = 2;

    float RADIUS_ROUND_AVATAR_DELITEL = 2.0f;
    double KOFF_PROPORTION_SIZE = 1.73;
    char EMAIL = '@';
    char POINT = '.';

    String NULL_STRING = "null";
    long DELAY_POST_AUTH = 4000;
    String ERROR_LOGIN_PASSWORD = "Неверный логин или пароль";

    int RESPONSE_CODE_ACCESS = 200;
    int RESPONSE_CODE_NOT_FOUND = 404;
    String ERROR_SAVE_DATA = "Ошибка сохранения данных";
    String STANDART_ERROR_MESSAGE = "Error";
    String MESSAGE_EMAIL = "Сообщение отправляется";
    String MESSAGE_GET_PERMISSION = "Для корректной работы необходимо дать требуемые разрешения";
    String MESSAGE_GET_AUTH_OFFLINE = "Зайти офлайн";
    String MESSAGE_CAN_USE_PERMISSION = "Разрешить";
    String MESSAGE_CAN_USE_AUTH_OFFLINE = "Войти";
    String NETWORK_NOT_FOUND = "Сеть не найдена";
    String NAME_BD = "devintensive-db";
    String LOAD_FROM_CACHE = " load from cache";
    String ERROR_LOAD_FROM_CACHE = " error load photo from cache";
    String LIST_USER_NOT_CAN_GET = "Список пользователей не может быть получен";
    CharSequence SEARCH_ON_FULL_NAME = "Поиск по имени-фамилии";
    String MENU_ITEM_MY_PROFILE = "Мой профиль";
    String MENU_ITEM_REFRESH = "Обновить";
    String MENU_ITEM_TEAM = "Команда";
    String MENU_ITEM_RATING = "Рейтинг";
    String MENU_ITEM_EXIT = "Выйти";
    int QUALITY_BITMAP_AVATAR = 100;
}
