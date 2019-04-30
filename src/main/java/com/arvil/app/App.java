package com.arvil.app;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.engines.RijndaelEngine;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class App {
    public static void main(String[] args) {
        System.out.println(Encrypt("520020202020",
                "C2TH1KWTLLK3ICLUCTYGLO3WILVOPYYRADUYCQSTT58WQBZIZBXEW6FVJ9OODMZJ36KUD3T6Y25HQOMOO0OLOONGVU1KN9IOA8XHWRWAFNOM7H6517BFZPGYRFYBQCEZ"));
    }

    public static String Encrypt(String plainText, String passPhrase) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            byte[] array = Generate256BitsOfRandomEntropy();
            byte[] array2 = Generate256BitsOfRandomEntropy();
            byte[] bytes = plainText.getBytes();
            byte[] bytes2 = GetRfc2898DerivedByes(passPhrase.toCharArray(), array);
            BufferedBlockCipher cipher = new BufferedBlockCipher(new RijndaelEngine(256));
            return "HAAHAHAH";
        } catch (Exception ex) {

            return "An error occured";
        }
    }

    private static byte[] GetRfc2898DerivedByes(char[] passPhrase, byte[] array)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(passPhrase, array, 1000, 256);
        SecretKey secretKey = factory.generateSecret(spec);
        byte[] key = secretKey.getEncoded();

        byte[] keyBytes = new byte[256 / 8];

        System.arraycopy(key, 0, keyBytes, 0, 256 / 8);
        return keyBytes;
    }

    private static byte[] Generate256BitsOfRandomEntropy() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[32];
        random.nextBytes(salt);
        return salt;
    }
}