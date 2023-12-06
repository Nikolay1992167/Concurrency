package by.clevertec.exception;

public class ServerException extends RuntimeException {

    public ServerException(Throwable cause) {
        super(cause.getMessage(), cause);
    }
}
