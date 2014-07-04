package org.osiam.addons.administration.exception;

/**
 * Our custom exception class.
 */
public class OsiamAdministrationException extends Exception {
    private static final long serialVersionUID = 6913346574943694371L;

    public OsiamAdministrationException() {
    }

    public OsiamAdministrationException(String message) {
        super(message);
    }

    public OsiamAdministrationException(Throwable cause) {
        super(cause);
    }

    public OsiamAdministrationException(String message, Throwable cause) {
        super(message, cause);
    }

    public OsiamAdministrationException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
