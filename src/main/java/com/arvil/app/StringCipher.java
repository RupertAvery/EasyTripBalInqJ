package com.arvil.app;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Arrays;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.RijndaelEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PKCS7Padding;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import static java.nio.charset.StandardCharsets.UTF_8;

public class StringCipher {
    private static int Keysize = 256;

    private static int DerivationIterations = 1000;

    public static String Encrypt(String plainText, String passPhrase) throws InvalidKeyException,
            NoSuchAlgorithmException, UnsupportedEncodingException, InvalidCipherTextException {
        byte[] array = StringCipher.Generate256BitsOfRandomEntropy();
        byte[] array2 = StringCipher.Generate256BitsOfRandomEntropy();
        byte[] bytes = plainText.getBytes(UTF_8);

        Rfc2898DeriveBytes rfc2898DeriveBytes = new Rfc2898DeriveBytes(passPhrase, array, DerivationIterations);

        byte[] bytes2 = rfc2898DeriveBytes.getBytes(32);

        PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(
                new CBCBlockCipher(new RijndaelEngine(Keysize)), new PKCS7Padding());
        CipherParameters ivAndKey = new ParametersWithIV(new KeyParameter(bytes2), array2);
        cipher.init(true, ivAndKey);

        byte[] data = cipherData(cipher, bytes);

        byte[] result = Arrays.copyOf(array, array.length + array2.length + data.length);

        System.arraycopy(array2, 0, result, array.length, array2.length);
        System.arraycopy(data, 0, result, array.length + array2.length, data.length);

        return new String(Base64.getEncoder().encode(result));
    }

    public static String Decrypt(String cipherText, String passPhrase) throws InvalidKeyException,
            NoSuchAlgorithmException, UnsupportedEncodingException, InvalidCipherTextException {

        byte[] array = Base64.getDecoder().decode(cipherText.getBytes());
        byte[] salt = Arrays.copyOfRange(array, 0, 32);
        byte[] rgbIV = Arrays.copyOfRange(array, 32, 64);
        byte[] array2 = Arrays.copyOfRange(array, 64, array.length);

        Rfc2898DeriveBytes rfc2898DeriveBytes = new Rfc2898DeriveBytes(passPhrase, salt, DerivationIterations);

        byte[] bytes = rfc2898DeriveBytes.getBytes(32);

        PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(
                new CBCBlockCipher(new RijndaelEngine(Keysize)), new PKCS7Padding());

        CipherParameters ivAndKey = new ParametersWithIV(new KeyParameter(bytes), rgbIV);
        cipher.init(false, ivAndKey);

        return new String(cipherData(cipher, array2));
    }

    private static byte[] Generate256BitsOfRandomEntropy() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[32];
        random.nextBytes(salt);
        return salt;
    }

    private static byte[] cipherData(PaddedBufferedBlockCipher cipher, byte[] data) throws InvalidCipherTextException {
        int minSize = cipher.getOutputSize(data.length);
        byte[] outBuf = new byte[minSize];
        int length1 = cipher.processBytes(data, 0, data.length, outBuf, 0);
        int length2 = cipher.doFinal(outBuf, length1);
        int actualLength = length1 + length2;
        byte[] cipherArray = new byte[actualLength];
        for (int x = 0; x < actualLength; x++) {
            cipherArray[x] = outBuf[x];
        }
        return cipherArray;
    }

    private String md5(String string) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(string.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
