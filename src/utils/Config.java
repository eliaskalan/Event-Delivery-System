package utils;

import controller.Address;
import controller.Topic;
import controller.TopicZookeeper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.Random;
import java.util.Scanner;

public class Config {
    public static String SAVED_FILES_PATH = "C:/Users/elias/Documents/";

    public static String ZOOKEEPER_IP = "localhost";
    public static Address ZOOKEEPER_BROKERS = new Address(ZOOKEEPER_IP, 22345);
    public static Address ZOOKEEPER_CLIENTS = new Address(ZOOKEEPER_IP, 22346);
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

    public static final String EXIT_FROM_TOPIC = "EXIT";
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

    public static void sendAMessage(BufferedWriter bufferedWriter, String message){
        try {
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            System.out.println("Problem to send message ~ string");
            throw new RuntimeException(e);
        }

    }
    public static void sendAMessage(BufferedWriter bufferedWriter, int message){
        try {
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            System.out.println("Problem to send message ~ int");
            throw new RuntimeException(e);
        }

    }

    public static String readAMessage(BufferedReader bufferedReader){
        try {
           return bufferedReader.readLine();
        } catch (IOException e) {
            System.out.println("Problem to read message");
            throw new RuntimeException(e);
        }
    }

    public static String readFromUser(String message){
        Scanner scanner = new Scanner(System.in);
        System.out.println(message);
        return scanner.nextLine();
    }
    public static String getPublicIp() {
        String ip;
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                if (iface.isLoopback() || !iface.isUp())
                    continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();

                    if (addr instanceof Inet6Address) continue;

                    ip = addr.getHostAddress();
                    System.out.println(ip);
                    return ip;
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        return "localhost";
    }
}
