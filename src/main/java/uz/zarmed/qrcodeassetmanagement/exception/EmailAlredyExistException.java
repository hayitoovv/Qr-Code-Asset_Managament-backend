package uz.zarmed.qrcodeassetmanagement.exception;

public class EmailAlredyExistException extends RuntimeException {
    public EmailAlredyExistException(String email) {
        super("Email alredy exists "+email);
    }
}
