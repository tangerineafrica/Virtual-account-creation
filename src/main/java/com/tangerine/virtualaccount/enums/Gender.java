package com.tangerine.virtualaccount.enums;

import lombok.Getter;

@Getter
public enum Gender {
    MALE ("1"), FEMALE ("2");

    Gender(String gender) {
        this.gender = gender;
    }

    private final String gender;


    public String value() {
        return gender;
    }
}
