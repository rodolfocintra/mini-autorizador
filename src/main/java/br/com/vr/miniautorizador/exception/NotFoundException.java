package br.com.vr.miniautorizador.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(){}

    public NotFoundException(String msg) {
        super(msg);
    }

    public NotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
