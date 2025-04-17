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
    public static final String logStartLogout = "Start to sign out";
    public static final String logSuccessLogout = "Sign out successfully";
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
    public static final String logStartActivateUser = "Activating user";
    public static final String logSuccessActivateUser = "User activated successfully!";
    public static final String logStartDeactivateUser = "Deactivating user";
    public static final String logSuccessDeactivateUser = "User deactivated successfully!";
    public static final String logStartGetImage = "Getting user profile image";
    public static final String logSuccessGetImage = "User profile image get successfully!";
    public static final String logStartUploadImage = "Uploading user profile image";
    public static final String logSuccessUploadImage = "User profile image upload successfully!";
    public static final String logStartDeleteUser = "Deleting User";
    public static final String logSuccessDeleteUser = "User deleted successfully!";


}
