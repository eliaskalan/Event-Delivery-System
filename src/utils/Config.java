package utils;

import controller.Address;
import controller.Topic;
import controller.TopicZookeeper;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Config {
    public static String SAVED_FILES_PATH = "C:/Users/elias/Documents/";
    public static Address ZOOKEEPER_BROKERS = new Address(22345);
    public static Address ZOOKEEPER_CLIENTS = new Address(22346);
    public static Address BROKER_1 = new Address(22351);
    public static Address BROKER_2 = new Address(22352);
    public static Address BROKER_3 = new Address(22353);

    public static TopicZookeeper TOPIC_1 = new TopicZookeeper("Food");
    public static TopicZookeeper TOPIC_2 = new TopicZookeeper("Distributed Systems");
    public static TopicZookeeper TOPIC_3 = new TopicZookeeper("Databases");
    public static TopicZookeeper TOPIC_4 = new TopicZookeeper("Sports");
    public static TopicZookeeper TOPIC_5 = new TopicZookeeper("Music");
    public static TopicZookeeper TOPIC_6 = new TopicZookeeper("Exams");
    public static final int NUMBER_OF_BROKERS = 3;
    public static String calculateKeys(Address address) {
        try {
            String hashtext;
            String name = address.getIp() + address.getPort();
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

    public static String generateRandomPassword(int len) {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijk"
                +"lmnopqrstuvwxyz!@#$%&";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        return sb.toString();
    }
}
