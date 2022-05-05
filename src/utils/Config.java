package utils;

import controller.Address;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Config {
    public static String SAVED_FILES_PATH = "C:/Users/elias/Documents/";
    public static Address ZOOKEEPER = new Address(22345);
    public static Address BROKER_1 = new Address(22351);
    public static Address BROKER_2 = new Address(22352);
    public static Address BROKER_3 = new Address(22353);

    public static String calculateKeys(Address address) {
        try {
            String hashtext;
            String name = address.getIp() + Integer.toString(address.getPort());
            //  hashing MD5
            MessageDigest md1 = MessageDigest.getInstance("MD5");

            byte[] messageDigest1 = md1.digest(name.getBytes());

            BigInteger no1 = new BigInteger(1, messageDigest1);
            hashtext = no1.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
