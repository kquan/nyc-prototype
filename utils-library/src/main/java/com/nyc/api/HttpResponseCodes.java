package com.nyc.api;

public interface HttpResponseCodes {
    
    // Good reference: http://en.wikipedia.org/wiki/List_of_HTTP_status_codes
    
    public static final int OK = 200;
    public static final int CREATED = 201;
    public static final int NO_CONTENT = 204;
    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int CONFLICT = 409;
    public static final int UNPROCCESSABLE_ENTITY = 422;
    public static final int ERROR = 500;
    
    public static final int UPGRADE_REQUIRED = 412;
}