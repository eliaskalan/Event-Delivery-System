package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Broker extends Node implements Runnable{
    List<Client> registerClient = null;
    String hash;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    Broker(String ip, int port){
        super(ip, port);
        calculateKeys();
    }
    public void acceptConection(Client client) {
        registerClient.add(client);
    }

    public void calculateKeys() {
        //Todo we want to return the hash key, compare variables or update an array?
        try {
            //  hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Todo we want also to add ip
            byte[] messageDigest = md.digest((Integer.toString(this.getPort()) + this.getIp()).getBytes());

            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            this.hash =  hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public void filterConsumers(String consumer) {

    }

    public void notifyBrokersOnChangers() {

    }

    public void notifyPublisher(String notify) {

    }

    public void pull(String pullfile) {

    }

    public void broadcastMessage(String messageToSend, int userId) {
        for (Client client : registerClient) {
            try {
                if (userId != client.publisher.profileName.getUserId()) {
                    this.bufferedWriter.write(messageToSend);
                    this.bufferedWriter.newLine();
                    this.bufferedWriter.flush();
                }
            } catch (IOException e) {
                // Gracefully close everything.
               this.disconnect();
            }
        }
    }

    @Override
    public void run() {
        String messageFromClient;
        // Continue to listen for messages while a connection with the client is still established.
        while (true) {
            try {
                // Read what the client sent and then send it to every other client.
                messageFromClient = bufferedReader.readLine();
                broadcastMessage(messageFromClient, -1);
            } catch (IOException e) {
                // Close everything gracefully.
                break;
            }
        }
    }
    public static void main(String[] args) throws IOException {
        Broker broker = new Broker("localhost", 12345);
        broker.connect("A new client has connected!");
        broker.run();
    }

}
