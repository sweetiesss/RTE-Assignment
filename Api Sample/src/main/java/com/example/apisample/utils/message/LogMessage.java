package com.example.apisample.utils.message;

public class LogMessage {

    // Auth Controller Logs
    public static final String AUTH_REFRESH_SUCCESS = "Token has been refreshed";
    public static final String AUTH_REFRESH_START = "Start to refresh token";
    public static final String AUTH_RETURNING_TOKEN = "Returning new token";
    public static final String AUTH_REGISTER_START = "Start to register user";
    public static final String AUTH_REGISTER_SUCCESS = "Register use successfully";
    public static final String AUTH_LOGIN_START = "Start to login";
    public static final String AUTH_LOGIN_SUCCESS = "User logged in successfully";
    public static final String AUTH_LOGOUT_START = "Start to log out";
    public static final String AUTH_LOGOUT_COOKIE_DELETE = "Start to delete cookie in logout";
    public static final String AUTH_LOGOUT_SUCCESS = "Log out successfully";
    public static final String AUTH_RESET_PASSWORD_START = "Reset password start!";
    public static final String AUTH_RESET_PASSWORD_SUCCESS = "Reset password successfully!";
    public static final String AUTH_TOKEN_GENERATED = "Token has been generated";


    // OTP Logs
    public static final String OTP_SCHEDULE_DELETE = "delete OTP schedule";
    public static final String OTP_RESET_PASSWORD_SENT = "Otp has been sent due to the lack of otp code in the request";
    public static final String OTP_RESET_PASSWORD_SENT_SUCCESS = "Otp has been sent while resetting password";
    public static final String OTP_VERIFY_START = "Verifying OTP";
    public static final String OTP_VERIFY_SUCCESS = "Otp has been verified successfully";

    // Role Controller Logs
    public static final String ROLE_ASSIGN_START = "Assigning role";
    public static final String ROLE_ASSIGN_SUCCESS = "Role assigned successfully";
    public static final String ROLE_GET_ALL_START = "Start to get all roles";
    public static final String ROLE_GET_ALL_SUCCESS = "Get all roles successfully";
    public static final String ROLE_GET_BY_ID_START = "Start to Get all roles by id";
    public static final String ROLE_GET_BY_ID_SUCCESS = "Get role by id successfully";

    // User Controller Logs
    public static final String USER_UPDATE_START = "Updating User";
    public static final String USER_UPDATE_SUCCESS = "User updated successfully!";
    public static final String USER_DELETE_START = "Deleting User";
    public static final String USER_DELETE_SUCCESS = "User deleted successfully!";
    public static final String USER_RESTORE_START = "Restoring user";
    public static final String USER_RESTORE_SUCCESS = "User restored successfully!";
    public static final String USER_GET_IMAGE_START = "Getting user profile image";
    public static final String USER_GET_IMAGE_SUCCESS = "User profile image get successfully!";
    public static final String USER_UPLOAD_IMAGE_START = "Uploading user profile image";
    public static final String USER_UPLOAD_IMAGE_SUCCESS = "User profile image upload successfully!";
    public static final String USER_GET_BY_EMAIL_START = "Getting user by email";
    public static final String USER_GET_BY_EMAIL_SUCCESS = "User get successfully!";
    public static final String USER_GET_BY_ID_START = "Getting user by id";
    public static final String USER_GET_BY_ID_SUCCESS = "User get successfully!";

    // Email Logs
    public static final String EMAIL_OTP_SEND_FAILED = "Email otp sending failed";
    public static final String EMAIL_PASSWORD_SEND_FAILED = "Email password sending failed";

    // Product Logs
    public static final String PRODUCT_CREATE_START = "Start creating product";
    public static final String PRODUCT_CREATE_SUCCESS = "Successfully created product";
    public static final String PRODUCT_UPDATE_START = "Start updating product";
    public static final String PRODUCT_UPDATE_SUCCESS = "Successfully updated product";
    public static final String PRODUCT_DELETE_START = "Start deleting product";
    public static final String PRODUCT_DELETE_SUCCESS = "Successfully deleted product";
    public static final String PRODUCT_RESTORE_START = "Start restoring product";
    public static final String PRODUCT_RESTORE_SUCCESS = "Successfully restored product";
    public static final String PRODUCT_GET_BY_ID_START = "Start fetching product by ID";
    public static final String PRODUCT_GET_BY_ID_SUCCESS = "Successfully fetched product by ID";

    // Rating Logs
    public static final String RATING_CREATE_START = "Start creating rating";
    public static final String RATING_CREATE_SUCCESS = "Successfully created rating";
    public static final String RATING_GET_BY_ID_START = "Start fetching rating by ID";
    public static final String RATING_GET_BY_ID_SUCCESS = "Successfully fetched rating by ID";
    public static final String RATING_UPDATE_START = "Start updating rating";
    public static final String RATING_UPDATE_SUCCESS = "Successfully updated rating";
    public static final String RATING_DELETE_START = "Start deleting rating";
    public static final String RATING_DELETE_SUCCESS = "Successfully deleted rating";
    public static final String RATING_RESTORE_START = "Start restoring rating";
    public static final String RATING_RESTORE_SUCCESS = "Successfully restored rating";

    // Category Logs
    public static final String CATEGORY_GET_BY_ID_START = "Start fetching category by ID";
    public static final String CATEGORY_GET_BY_ID_SUCCESS = "Successfully fetched category by ID";
    public static final String CATEGORY_CREATE_START = "Start creating new category";
    public static final String CATEGORY_CREATE_SUCCESS = "Successfully created new category";
    public static final String CATEGORY_UPDATE_START = "Start updating category";
    public static final String CATEGORY_UPDATE_SUCCESS = "Successfully updated category";
    public static final String CATEGORY_DELETE_START = "Start deleting category";
    public static final String CATEGORY_DELETE_SUCCESS = "Successfully deleted category";

    // Product-Category Logs
    public static final String PRODUCT_CATEGORY_CREATE_START = "Start creating a new product-category relation.";
    public static final String PRODUCT_CATEGORY_CREATE_SUCCESS = "Successfully created a product-category relation.";
    public static final String PRODUCT_CATEGORY_DELETE_START = "Start deleting product-category relation.";
    public static final String PRODUCT_CATEGORY_DELETE_SUCCESS = "Successfully deleted product-category relation.";
    public static final String PRODUCT_CATEGORY_GET_ALL_START = "Start fetching all product-category relations.";
    public static final String PRODUCT_CATEGORY_GET_ALL_SUCCESS = "Successfully fetched all product-category relations.";
    public static final String PRODUCT_CATEGORY_GET_BY_IDS_START = "Start fetching product-category relation by productId and categoryId.";
    public static final String PRODUCT_CATEGORY_GET_BY_IDS_SUCCESS = "Successfully fetched product-category relation by productId and categoryId.";

}
