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
}
