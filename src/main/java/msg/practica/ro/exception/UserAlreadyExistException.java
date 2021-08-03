package msg.practica.ro.exception;

public class UserAlreadyExistException extends RuntimeException{
    public UserAlreadyExistException() {
        super("user already exists");
    }
}
