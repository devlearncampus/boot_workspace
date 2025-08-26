package com.sinse.electroshop.advice;

import com.sinse.electroshop.exception.MemberNotException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MemberNotException.class)
    public ResponseEntity<String> handleMemberException(MemberNotException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)  // 401
                .body(ex.getMessage());           // 단순 문자열 반환
    }
}
