package com.example.apisample.model.dto.message;


public class LogMessage {
    //Auth Controller Log
    public static final String logSuccessRefresh = "Token has been refreshed";
    public static final String logStartRefresh = "Start to refresh token";
    public static final String logReturningToken = "Returning new token";
    public static final String logStartRegis = "Start to register user";
    public static final String logSuccessRegis = "Register use successfully";
    public static final String logStartAuthentication = "Start to authenticate user";
    public static final String logAuthenticated = "User is authenticated";
    public static final String logStartLogout = "Start to log out";
    public static final String logStartLogoutCookieDelete = "Start to delete cookie in logout";
    public static final String logSuccessLogout = "Log out successfully";
    public static final String logStartResetPassword = "Reset password start!";
    public static final String logSuccessResetPassword = "Reset password successfully!";
    public static final String logRefreshTokenUnauthorize = "Sending OTP";

    //Otp Log
    public static final String logOtpScheduleDelete = "delete OTP schedule";
    public static final String logOtpResetPasswordSent = "Otp has been sent due to the lack of otp code in the request";

    //Role Controller Log
    public static final String logStartAssignRole = "Assigning role";
    public static final String logSuccessAssignRole= "Role assigned successfully";

    //User Controller Log
    public static final String logStartUpdateUser = "Updating User";
    public static final String logSuccessUpdateUser = "User updated successfully!";
    public static final String getLogStartDeleteUser = "Deleting User";
    public static final String logSuccessDeleteUser = "User deleted successfully!";
    public static final String logStartRestoreUser = "Restoring user";
    public static final String logSuccessRestoreUser = "User restored successfully!";
    public static final String logStartGetImage = "Getting user profile image";
    public static final String logSuccessGetImage = "User profile image get successfully!";
    public static final String logStartUploadImage = "Uploading user profile image";
    public static final String logSuccessUploadImage = "User profile image upload successfully!";
    public static final String logStartGetUserByEmail = "Getting user by email";
    public static final String logSuccessGetUserByEmail = "User get successfully!";
    public static final String logStartGetUserById = "Getting user by id";
    public static final String logSuccessGetUserById = "User get successfully!";
    //Email Log
    public static final String logEmailSendFailed = "Email sending failed";

    //Role Log
    public static String logStartGetAllRoles = "Start to get all roles";
    public static String logSuccessGetAllRoles = "Get all roles successfully";
    public static String logStartGetAllRolesById = "Start to Get all roles by id";
    public static String logSuccessGetAllRolesById = "Get role by id successfully";

    //Product Log
    public static final String logStartCreateProduct = "Start creating product";
    public static final String logSuccessCreateProduct = "Successfully created product";
    public static final String logStartUpdateProduct = "Start updating product";
    public static final String logSuccessUpdateProduct = "Successfully updated product";
    public static final String logStartDeleteProduct = "Start deleting product";
    public static final String logSuccessDeleteProduct = "Successfully deleted product";
    public static final String logStartRestoreProduct = "Start restoring product";
    public static final String logSuccessRestoreProduct = "Successfully restored product";
    public static final String logStartGetProductById = "Start fetching product by ID";
    public static final String logSuccessGetProductById = "Successfully fetched product by ID";

    //Rating Log
    public static final String logStartCreateRating = "Start creating rating";
    public static final String logSuccessCreateRating = "Successfully created rating";
    public static final String logStartGetRatingById = "Start fetching rating by ID";
    public static final String logSuccessGetRatingById = "Successfully fetched rating by ID";
    public static final String logStartUpdateRating = "Start updating rating";
    public static final String logSuccessUpdateRating = "Successfully updated rating";
    public static final String logStartDeleteRating = "Start deleting rating";
    public static final String logSuccessDeleteRating = "Successfully deleted rating";
    public static final String logStartRestoreRating = "Start restoring rating";
    public static final String logSuccessRestoreRating = "Successfully restored rating";

    //Category Log
    public static final String logStartGetCategoryById = "Start fetching category by ID";
    public static final String logSuccessGetCategoryById = "Successfully fetched category by ID";
    public static final String logStartCreateCategory = "Start creating new category";
    public static final String logSuccessCreateCategory = "Successfully created new category";
    public static final String logStartUpdateCategory = "Start updating category";
    public static final String logSuccessUpdateCategory = "Successfully updated category";
    public static final String logStartDeleteCategory = "Start deleting category";
    public static final String logSuccessDeleteCategory = "Successfully deleted category";

    // Category Log
    public static final String logStartCreateProductCategory = "Start creating a new product-category relation.";
    public static final String logSuccessCreateProductCategory = "Successfully created a product-category relation.";
    public static final String logStartDeleteProductCategory = "Start deleting product-category relation.";
    public static final String logSuccessDeleteProductCategory = "Successfully deleted product-category relation.";
    public static final String logStartGetAllProductCategories = "Start fetching all product-category relations.";
    public static final String logSuccessGetAllProductCategories = "Successfully fetched all product-category relations.";
    public static final String logStartGetProductCategoryByIds = "Start fetching product-category relation by productId and categoryId.";
    public static final String logSuccessGetProductCategoryByIds = "Successfully fetched product-category relation by productId and categoryId.";


}
