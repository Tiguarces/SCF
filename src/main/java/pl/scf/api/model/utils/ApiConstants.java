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
    public static final String  NOT_FOUND_MESSAGE = "%s with %s: %s not found in the database";

    public static final String SUCCESS_SAVING = "Successful saving";
    public static final String FAIL_SAVING = "Fail while saving";
    public static final String SUCCESS_UPDATE = "Successful updating";
    public static final String FAIL_UPDATE = "Fail while updating";
    public static final String SUCCESS_DELETE = "Successful deleting";
    public static final String FAIL_DELETE = "Fail while deleting";
    public static final String SUCCESS_ACTIVATION_EMAIL = "Successful email activation";
    public static final String FAIL_ACTIVATION_EMAIL = "Fail while email activation";

    public static final String SUCCESS_SEND_EMAIL_AGAIN = "Email sent again successfully";

    public static final String SUCCESS_SEND_EMAIL = "User created successfully, sending activation email";
    public static final String FAIL_SEND_EMAIL = "User not created, problem with sending activation email";

    public static final String ADMIN_ACCOUNT_EXISTS = "Administrator account exists";
    public static final String ADMIN_ACCOUNT_CREATE = "Administrator account has created successfully";
    public static final String USER_ROLES_ADDING = "Adding User titles";
    public static final String USER_ROLES_EXISTS = "All User titles exists";
    public static final String USER_TITLES_ADDING = "Adding user roles";
    public static final String USER_TITLES_EXISTS = "All User Roles exists";

    public static final String NOT_VALID_ELEMENT_MESSAGE = "%s is null or empty";
    public static final String ID_ERROR_MESSAGE = "ID must be greater than 0 and not null";
    public static final String ELEMENT_EXISTS = "%s exists in the database";
    public static final String SUCCESS_FETCHING = "Successfully fetching";
    public static final String FAIL_FETCHING = "Failure fetching";
    public static final String DELETE_ALL = "Delete all {}";

    public static final String LOGIN_SUCCESS = "Successfully logging user";
    public static final String LOGOUT_USER = "Successfully logout user";

    public static final String USER_EXISTS = "User already exists";
    public static final String EMAIL_IS_USED = "Email has already been used";
    public static final String NICKNAME_IS_USED = "Nickname is already used";

    public static final String AUTHENTICATE_USER_SUCCESS = "Successfully authenticated user";
    public static final String AUTHENTICATE_USER_FAIL = "Failure authenticated user";

    public static final String REFRESH_SUCCESS = "Successfully refresh JWT token";
    public static final String REFRESH_FAIL = "Failure refresh JWT token";
    public static final String TOKEN_EXPIRED = "JWT token expired";
    public static final String TOKEN_NOT_VALID = "JWT token is not valid";
}
