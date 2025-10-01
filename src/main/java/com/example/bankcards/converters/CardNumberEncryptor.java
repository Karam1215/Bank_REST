package com.example.bankcards.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.util.Base64;

@Converter
public class CardNumberEncryptor implements AttributeConverter<String, String> {

    private static final String ENCRYPT_ALGO = "AES/GCM/NoPadding";
    private static final int IV_SIZE = 12;
    private static final int TAG_LENGTH_BIT = 128;

    private static final String SECRET = "0123456789abcdef0123456789abcdef";
    private static final SecretKey SECRET_KEY = new SecretKeySpec(SECRET.getBytes(), "AES");

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) return null;
        try {
            Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);
            byte[] iv = SecureRandomHolder.randomBytes(IV_SIZE);
            GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
            cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY, spec);
            byte[] encrypted = cipher.doFinal(attribute.getBytes());

            ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + encrypted.length);
            byteBuffer.put(iv);
            byteBuffer.put(encrypted);
            return Base64.getEncoder().encodeToString(byteBuffer.array());
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting card number", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            byte[] decoded = Base64.getDecoder().decode(dbData);
            ByteBuffer byteBuffer = ByteBuffer.wrap(decoded);

            byte[] iv = new byte[IV_SIZE];
            byteBuffer.get(iv);

            byte[] encrypted = new byte[byteBuffer.remaining()];
            byteBuffer.get(encrypted);

            Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);
            GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
            cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY, spec);
            byte[] decrypted = cipher.doFinal(encrypted);
            return new String(decrypted);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting card number", e);
        }
    }

    private static class SecureRandomHolder {
        private static final java.security.SecureRandom RANDOM = new java.security.SecureRandom();

        static byte[] randomBytes(int size) {
            byte[] bytes = new byte[size];
            RANDOM.nextBytes(bytes);
            return bytes;
        }
    }
}
