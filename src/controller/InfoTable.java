package controller;

import model.ProfileName;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

// Important https://docs.oracle.com/javase/tutorial/essential/concurrency/syncmeth.html
public class InfoTable {

    private ArrayList<Broker> availableBrokers = new ArrayList<Broker>();

    private HashMap<Broker, ArrayList<Topic>> brokersConnectionWithTopics = new HashMap<>();
    private HashMap<ProfileName, ArrayList<Topic>> availableClients = new HashMap<>();
    private ArrayList<Topic> availableTopics = new ArrayList<>();


    public synchronized ArrayList<Topic> getAvailableTopics() {
        return availableTopics;
    }

    public synchronized HashMap<ProfileName, ArrayList<Topic>> getAvailableClients() {
        return availableClients;
    }

    public void addAvailableClients(ProfileName profile) {
        availableClients.put(profile, new ArrayList<Topic>());
    }

    public ArrayList<Broker> getAvailableBrokers(){
        return this.availableBrokers;
    }

    public void addAvailableClients(ProfileName profile, Topic topic) {
        ArrayList<Topic> topics = this.availableClients.get(profile);
        topics.add(topic);
        availableClients.put(profile, topics);
    }

    public void availableTopics(Topic topic) {
        this.availableTopics.add(topic);
    }

    public void addBroker(Broker broker){
        this.availableBrokers.add(broker);
        this.brokersConnectionWithTopics.put(broker, null);
    }
    public void addTopicOnBroker(Broker broker, Topic topic){
        ArrayList<Topic> topics = this.brokersConnectionWithTopics.get(broker);
        this.brokersConnectionWithTopics.put(broker, topics);
        broker.addTopic(topic);
    }

    public void addTopics(Topic topic){
        this.availableTopics.add(topic);
       addTopicOnBroker(getBrokerWithTheMinTopics(), topic);
    }

    public Broker getBrokerWithTheMinTopics(){
        //ToDo maybe later we must have hashing for topics.
        Broker min = this.availableBrokers.get(0);
        for(int i = 1; i < availableBrokers.size(); i++){
            if(min.getTopicsLength() > this.availableBrokers.get(i).getTopicsLength()){
                min = this.availableBrokers.get(i);
            }
        }
        return min;
    }
}
