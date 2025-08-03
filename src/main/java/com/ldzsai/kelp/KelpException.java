package com.ldzsai.kelp;

public class KelpException extends RuntimeException {

    public KelpException(String error) {
        super(error);
    }
    
    public KelpException(String error, Throwable cause) {
        super(error, cause);
    }
}