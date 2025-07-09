//package com.personalApp.personal_service.constants;
//
//import lombok.Getter;
//
//@Getter
//public enum Seniority {
//    JUNIOR(0),
//    MID(1),
//    SENIOR(2),
//    LEAD(3),
//    MANAGER(4);
//
//    private final int value;
//
//    Seniority(int value) {
//        this.value = value;
//    }
//
//    public static Seniority fromValue(int value) {
//        for (Seniority s : Seniority.values()) {
//            if (s.getValue() == value) {
//                return s;
//            }
//        }
//        throw new IllegalArgumentException("Invalid seniority value: " + value);
//    }
//}
