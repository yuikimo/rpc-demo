package org.example.filter;

public class FilterResponse {

    private final Boolean result;
    private final Exception exception;

    public FilterResponse(Boolean result, Exception exception) {
        this.result = result;
        this.exception = exception;
    }

    public Boolean getResult() {
        return result;
    }

    public Exception getException() {
        return exception;
    }
}
