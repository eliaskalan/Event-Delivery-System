package controller;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

// Important https://docs.oracle.com/javase/tutorial/essential/concurrency/syncmeth.html
public class InfoTable {
    private HashMap<Address, ArrayList<String>> topicsAssociatedWithBrokers = new HashMap<>();
    private HashMap<Address, BigInteger> hashingIDAssociatedWithBrokers = new HashMap<>();
    private HashMap<Client, ArrayList<String>> availableClients = new HashMap<>();
    private ArrayList<String> availableTopics = new ArrayList<>();

    public synchronized HashMap<Address, ArrayList<String>> getTopicsAssociatedWithBrokers() {
        return topicsAssociatedWithBrokers;
    }

    public synchronized ArrayList<String> getAvailableTopics() {
        return availableTopics;
    }

    public synchronized HashMap<Address, BigInteger> getHashingIDAssociatedWithBrokers() {
        return hashingIDAssociatedWithBrokers;
    }

    public synchronized HashMap<Client, ArrayList<String>> getAvailableClients() {
        return availableClients;
    }

}
