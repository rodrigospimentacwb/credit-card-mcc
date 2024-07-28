package br.com.pepper.credit_card_mcc.exception;

public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}
