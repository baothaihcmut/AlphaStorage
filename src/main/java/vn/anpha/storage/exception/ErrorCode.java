package vn.anpha.storage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public enum ErrorCode {

    // FOR USER
    USER_EXISTED("User already exists", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED("User not exists", HttpStatus.NOT_FOUND),
    USER_PASSWORD_NOT_EXACTLY("User and password are not exactly", HttpStatus.BAD_REQUEST),
    PASSWORD_NOT_EXACTLY("Password are not exactly", HttpStatus.BAD_REQUEST),
    Token_Not_Valid("Token is not valid", HttpStatus.BAD_REQUEST),
    RefreshToken_Not_Valid("RefreshToken is not valid", HttpStatus.BAD_REQUEST),
    Role_Is_EXIST("Role is exists", HttpStatus.NOT_FOUND),
    UNAUTHORIZED("User do not have permission", HttpStatus.FORBIDDEN),
    UNAUTHOTICATED("Unautheticated", HttpStatus.UNAUTHORIZED),

    // FOR Company
    COMPANY_EXISTED("Company already exists", HttpStatus.BAD_REQUEST),
    COMPANY_NOT_EXISTED("Company not exists", HttpStatus.BAD_REQUEST),
    USER_NOT_OWNCOMPANY("User does not have permission", HttpStatus.BAD_REQUEST),

    COMPANY_EXEED_LIMIT_SIZE("Company doesn't have  enoungh space to save this file", HttpStatus.CONFLICT),
    PERSONAL_EXCEED_LIMIT_SIZE("You don't have enoungh space to save this file", HttpStatus.CONFLICT),
    // FOR DEPARTMENT
    DEPARTMENT_EXISTED("Department already exists", HttpStatus.BAD_REQUEST),
    DEPARTMENT_NOT_EXISTED("Department not exists", HttpStatus.BAD_REQUEST),
    USER_NOT_OWNDEPARTMENT("User does not manager department", HttpStatus.BAD_REQUEST),
    // FOR User_of_Department
    USER_OF_DEPARTMENT_EXISTED("User of department already exists", HttpStatus.BAD_REQUEST),
    USER_OF_DEPARTMENT_NOT_EXISTED("User of department not exists", HttpStatus.BAD_REQUEST),
    CREATE_USER_OF_DEPARTMENT_ERROR("Create user of department error", HttpStatus.BAD_REQUEST),
    USER_OF_DEPARTMENT_NOT_YOURS("User of department not yours", HttpStatus.BAD_REQUEST),

    // FOR User_of_Department
    USER_NOT_IN_COMPANY("User does not in company", HttpStatus.BAD_REQUEST),
    // FOR FOLDER
    PARENT_FOLDER_NOT_EXIST("Folder not exist", HttpStatus.NOT_FOUND),
    FOLDER_NOT_EXIST("Folder not Exist", HttpStatus.NOT_FOUND),
    // For File
    FILE_NOT_EXIST("File not Exist", HttpStatus.NOT_FOUND),
    DEPARTMENT_ID_REQUIRED("Department id is required", HttpStatus.BAD_REQUEST),
    FOLDER_ID_REQUIRED("Folder id is required", HttpStatus.BAD_REQUEST),
    FILE_PERMISSION_NOT_ALLOWED("You don't have permission for this operation", HttpStatus.FORBIDDEN),
    PARENT_FILE_NOT_EXIST("Parent file not exist", HttpStatus.NOT_FOUND),
    DIRECTORY_UNVALID("The parent file is not directory", HttpStatus.BAD_REQUEST),
    NEW_DIRECTORY_NOT_IN_DEPARTMENT("New directory is not in the same department", HttpStatus.CONFLICT),
    FILE_IS_DIRECTORY("File is directory", HttpStatus.BAD_REQUEST),
    FILE_NOT_IN_TRASH("File is not in trash", HttpStatus.NOT_FOUND),
    FILE_NAME_EXIST_NAME("File name exist in directory", HttpStatus.CONFLICT),
    FILE_NOT_EXIST_OR_NOT_FILE("File not exist or file is directory", HttpStatus.BAD_REQUEST),
    FILE_NOT_UPLOAD("File is not uploaded", HttpStatus.NOT_FOUND),
    // For Version
    VERSION_NOT_EXIST("Version not found or file not versioning", HttpStatus.NOT_FOUND),

    // FOR Buy Data
    THIS_TYPE_DOES_NOT_EXIST("This type does not exist", HttpStatus.BAD_REQUEST),

    // FOR TAG
    TAG_NOT_EXIST("Tag not Exist", HttpStatus.NOT_FOUND),
    TAG_COMPANY_MISMATCH("Tag company mismatch", HttpStatus.BAD_REQUEST),
    // FOR SERVER
    METHOD_NOT_ALLOW("Method not Exist", HttpStatus.METHOD_NOT_ALLOWED),
    API_NOT_EXIST("API not Exist", HttpStatus.NOT_FOUND),
    // ELSE
    SERVER_ERROR("Server error", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_ALREADY_IN_COMPANY("user already in company", HttpStatus.BAD_REQUEST);

    // FOR FOL

    ;

    private String message;
    private HttpStatusCode statusCode;

    private ErrorCode(String message, HttpStatusCode statusCode) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpStatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
