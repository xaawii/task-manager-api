package com.xmartin.notification_service.domain.enums;

import lombok.Getter;

@Getter
public enum Status {

    PENDING("pending"),

    IN_PROGRESS("in progress"),

    COMPLETED("completed");

    private final String text;

    Status(String text) {
        this.text = text;
    }

}
