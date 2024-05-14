package com.hits.liid.forumx.errors;

public class TokenExpireException extends Exception{
    public TokenExpireException() {
        super();
    }

    public TokenExpireException(String message) {
        super(message);
    }
}