package com.springboot.exception;

import lombok.Getter;

public enum ExceptionCode {
    MEMBER_NOT_FOUND(404, "Member Not Found"),
    MEMBER_EXISTS(409, "Member Exists"),
    POST_NOT_EXISTS(404, "Post Not Exists"),
    POST_EXISTS(404, "Post Exists");

    @Getter
    private int statusCode;

    @Getter
    private String statusDescription;

    ExceptionCode(int statusCode, String statusDescription) {
        this.statusCode = statusCode;
        this.statusDescription = statusDescription;
    }
}
