package com.sinse.electroshop.exception;

public class MemberNotException extends RuntimeException {
    public MemberNotException(String msg) {
        super(msg);
    }
    public MemberNotException(String msg, Throwable cause) {
        super(msg, cause);
    }
    public MemberNotException(Throwable cause) {
        super(cause);
    }
}
