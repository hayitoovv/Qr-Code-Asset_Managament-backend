package uz.zarmed.qrcodeassetmanagement.exception;

public class DepartmentCodeAlredyExistException extends RuntimeException {
    public DepartmentCodeAlredyExistException(String message) {
        super("Department code alredy exist "+ message);
    }
}
