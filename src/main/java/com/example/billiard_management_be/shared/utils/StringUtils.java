package com.example.billiard_management_be.shared.utils;

public class StringUtils {
    public static String truncateToken(String token) {
        if (token.length() > 72) {
            return token.substring(0, 72);
        }
        return token;
    }
}
