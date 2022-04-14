package controller;

import java.util.List;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Broker extends Node{
    List<Consumer> registerPublishers = null;
    List<Publisher> registeredPublishers=null;
    String hash;
    Broker(int port){
        super(port);
        calculateKeys();
    }
    public void acceptConection(Consumer cons) {
        registerPublishers.add(cons);
    }

    public void acceptConection(Publisher publ) {
        registeredPublishers.add(publ);
    }

    public void calculateKeys() {
        //Todo we want to return the hash key, compare variables or update an array?
        try {
            //  hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Todo we want also to add ip
            byte[] messageDigest = md.digest(Integer.toString(this.port).getBytes());

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

    public void filterConsumers(String comsumer) {

    }

    public void notifyBrokersOnChangers() {

    }

    public void notifyPublisher(String notify) {

    }

    public void pull(String pullfile) {

    }
}
