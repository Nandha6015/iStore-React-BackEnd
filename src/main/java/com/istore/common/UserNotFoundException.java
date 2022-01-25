package com.istore.common;

import java.util.List;

public class UserNotFoundException extends RuntimeException {

    private List<Error> errors;

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

    public UserNotFoundException(String message, List<Error> errors) {
        super(message);
        this.errors = errors;
    }

}
