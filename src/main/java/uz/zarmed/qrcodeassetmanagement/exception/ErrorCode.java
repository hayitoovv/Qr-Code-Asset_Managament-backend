package uz.zarmed.qrcodeassetmanagement.exception;

public class ErrorCode {
    public static final String DEPARTMENT_HAS_EMPLOYEES = "DEPT_001";
    public static final String DEPARTMENT_HAS_ASSETS = "DEPT_002";
    public static final String ASSET_ASSIGNED_TO_EMPLOYEE = "ASSET_001";
    public static final String DEPARTMENT_CODE_ALREADY_EXISTS = "DEPTC_1";
    public static final String ASSET_HAS_HISTORY = "ASSET_002";
    public static final String EMPLOYEE_EMAIL_ALREDY_EXSIST = "EMPE_001";
    public static final String EMPLOYEE_HAS_ASSET = "EMPA_002";
    public static final String CATEGORY_NAME_ALLREDY_EXSIST = "CATA_001";
    public static final String ASSETS_ASSIGNED_CATEGORY = "AAET_002";

    // Asset
    public static final String SERIAL_EXISTS = "SERIAL_EXISTS";
    public static final String ASSET_ASSIGNED = "ASSET_ASSIGNED";
    public static final String ASSET_ALREADY_ASSIGNED = "ASSET_ALREADY_ASSIGNED";
    public static final String ASSET_NOT_ASSIGNED = "ASSET_NOT_ASSIGNED";
}
