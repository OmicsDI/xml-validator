package uk.ac.ebi.ddi.xml.validator.exception;

public class DDIException extends Exception {

    public DDIException() {
    }

    public DDIException(String message) {
        super(message);
    }

    public DDIException(String message, Throwable cause) {
        super(message, cause);
    }

    public DDIException(Throwable cause) {
        super(cause);
    }

    public DDIException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
