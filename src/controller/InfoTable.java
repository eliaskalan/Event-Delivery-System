package controller;

import model.ProfileName;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

// Important https://docs.oracle.com/javase/tutorial/essential/concurrency/syncmeth.html
public class InfoTable {
    private HashMap<Address, ArrayList<String>> topicsAssociatedWithBrokers = new HashMap<>();
    private HashMap<Address, BigInteger> hashingIDAssociatedWithBrokers = new HashMap<>();
    private HashMap<ProfileName, ArrayList<Topic>> availableClients = new HashMap<>();
    private ArrayList<Topic> availableTopics = new ArrayList<>();

    public synchronized HashMap<Address, ArrayList<String>> getTopicsAssociatedWithBrokers() {
        return topicsAssociatedWithBrokers;
    }

    public synchronized ArrayList<Topic> getAvailableTopics() {
        return availableTopics;
    }

    public synchronized HashMap<Address, BigInteger> getHashingIDAssociatedWithBrokers() {
        return hashingIDAssociatedWithBrokers;
    }

    public synchronized HashMap<ProfileName, ArrayList<Topic>> getAvailableClients() {
        return availableClients;
    }

    public void addAvailableClients(ProfileName profile) {
        availableClients.put(profile, new ArrayList<Topic>());
    }

    public void addAvailableClients(ProfileName profile, Topic topic) {
        ArrayList<Topic> topics = this.availableClients.get(profile);
        topics.add(topic);
        availableClients.put(profile, topics);
    }

    public void availableTopics(Topic topic) {
        this.availableTopics.add(topic);
    }

}
