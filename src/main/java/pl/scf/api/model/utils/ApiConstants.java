package pl.scf.api.model.utils;

public final class ApiConstants {
    public static final String NULLABLE_MESSAGE = "{} is null";
    public static final String FETCH_BY_ID = "Fetching {} with id: {}";
    public static final String FETCHING_ALL_MESSAGE = "Fetching all {}";
    public static final String SAVING = "Saving new {} in the database";
    public static final String UPDATE_MESSAGE = "Updating {} with id: {}";
    public final static String DELETING_MESSAGE = "Deleting {} with id: {}";
    public final static String FETCHING_BY_STH_MESSAGE = "Fetching {} with {}: {}";
    public final static String EXCEPTION_MESSAGE = "Error while {} {} | Message: {}";
    public static final String NOT_FOUND_BY_ID = "{} with id: {} not found in the database";
    public static final String NOT_FOUND_BY_STH = "{} with {}: {} not found in the database";

    public static final String SUCCESS_SAVING = "Successful saving";
    public static final String FAIL_SAVING = "Fail while saving";
    public static final String SUCCESS_UPDATE = "Successful updating";
    public static final String FAIL_UPDATE = "Fail while updating";
    public static final String SUCCESS_DELETE = "Successful deleting";
    public static final String FAIL_DELETE = "Fail while deleting";
    public static final String SUCCESS_ACTIVATION_EMAIL = "Successful email activation";
    public static final String FAIL_ACTIVATION_EMAIL = "Fail while email activation";

    public static final String SUCCESS_SEND_EMAIL_AGAIN = "Email sent again successfully";
    public static final String FAIL_SEND_EMAIL_AGAIN = "Email not send again, fail";

    public static final String SUCCESS_SEND_EMAIL = "User created successfully, sending activation email";
    public static final String FAIL_SEND_EMAIL = "User not created, problem with sending activation email";

    public static final String ADMIN_ACCOUNT_EXISTS = "Administrator account exists";
    public static final String ADMIN_ACCOUNT_CREATE = "Administrator account has created successfully";
    public static final String USER_ROLES_ADDING = "Adding User titles";
    public static final String USER_ROLES_EXISTS = "All User titles exists";
    public static final String USER_TITLES_ADDING = "Adding user roles";
    public static final String USER_TITLES_EXISTS = "All User Roles exists";

    public static final String NOT_VALID_ELEMENT_MESSAGE = "%s is null or empty";
    public static final String NOT_FOUND_MESSAGE = "Not found %s with %s: %s";
    public static final String ID_ERROR_MESSAGE = "Id must be greater than 0";
    public static final String ELEMENT_EXISTS = "%s exists in the database";
    public static final String SUCCESS_FETCHING = "Successfully fetching";
    public static final String FAIL_FETCHING = "Failure fetching";
    public static final String DELETE_ALL = "Delete all {}";
}
