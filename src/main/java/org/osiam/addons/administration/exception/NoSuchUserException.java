package org.osiam.addons.administration.exception;

/**
 * This exception will be thrown when a user was not found.
 */
public class NoSuchUserException extends OsiamAdministrationException {
    private static final long serialVersionUID = 4161807002327727966L;

    public NoSuchUserException() {
    }

    public NoSuchUserException(String message) {
        super(message);
    }

    public NoSuchUserException(Throwable cause) {
        super(cause);
    }

    public NoSuchUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchUserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
