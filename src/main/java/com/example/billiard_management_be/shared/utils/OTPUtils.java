package com.example.billiard_management_be.shared.utils;
import com.amazonaws.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.SecureRandom;
import java.time.Instant;

public class OTPUtils {
    private static final String SECRET_KEY = "JBSWY3DPEHPK3PXP";
    private static final int OTP_LENGTH = 6;
    private static final int TIME_STEP_SECONDS = 30;
    private static final String HMAC_ALGORITHM = "HmacSHA1";

    public static String generateTOTP() {
        try {
            // generate secret key
            byte[] keyBytes = Base64.decode(SECRET_KEY);
            Key secretKey = new SecretKeySpec(keyBytes, HMAC_ALGORITHM);

            // get time at present
            long timeIndex = Instant.now().getEpochSecond() / TIME_STEP_SECONDS;

            // add random number
            SecureRandom random = new SecureRandom();
            int randomFactor = random.nextInt(1000);
            timeIndex += randomFactor;

            ByteBuffer buffer = ByteBuffer.allocate(8);
            buffer.putLong(timeIndex);
            byte[] timeBytes = buffer.array();

            // generate otp
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(secretKey);
            byte[] hash = mac.doFinal(timeBytes);

            // get the end part of hash to generate otp
            int offset = hash[hash.length - 1] & 0xF;
            int binary = ((hash[offset] & 0x7F) << 24) |
                    ((hash[offset + 1] & 0xFF) << 16) |
                    ((hash[offset + 2] & 0xFF) << 8) |
                    (hash[offset + 3] & 0xFF);

            // get 6 last digits
            int otp = binary % (int) Math.pow(10, OTP_LENGTH);

            return String.format("%06d", otp);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("Lỗi khi tạo TOTP: ", e);
        }
    }
}
