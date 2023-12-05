package by.clevertec.exception;

public class ClientException extends RuntimeException {

    public ClientException(InterruptedException message) {
        super(message);
    }
}
