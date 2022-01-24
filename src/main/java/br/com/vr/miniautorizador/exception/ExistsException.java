package br.com.vr.miniautorizador.exception;

public class ExistsException extends RuntimeException{
    public ExistsException(String msg) {
        super(msg);
    }

}
