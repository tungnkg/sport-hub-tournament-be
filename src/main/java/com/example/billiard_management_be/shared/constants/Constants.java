package com.example.billiard_management_be.shared.constants;

public class Constants {
    public static final class RoleConstant {
        public static final String ADMIN = "ADMIN";
        public static final String ORGANIZER = "ORGANIZER";
        public static final String PLAYER = "PLAYER";
    }

    public static class VNPAYPaymentStatus {
        public static final int PENDING = 0;
        public static final int SUCCESS = 1;
        public static final int FAIL = 2;
        public static final int TIMEOUT = 3;
    }
}
