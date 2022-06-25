package controller;

import model.ProfileName;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

// Important https://docs.oracle.com/javase/tutorial/essential/concurrency/syncmeth.html

public class InfoTable {

    private ArrayList<BrokerInZookeeper> availableBrokers = new ArrayList<BrokerInZookeeper>();

    private HashMap<BrokerInZookeeper, ArrayList<TopicZookeeper>> brokersConnectionWithTopics = new HashMap<>();
    private HashMap<ProfileName, ArrayList<TopicZookeeper>> availableClients = new HashMap<>();

    private ArrayList<UserZookeeper> clients = new ArrayList<>();
    private ArrayList<TopicZookeeper> availableTopics = new ArrayList<>();


    public synchronized ArrayList<TopicZookeeper> getAvailableTopics() {
        return availableTopics;
    }

    public synchronized HashMap<ProfileName, ArrayList<TopicZookeeper>> getAvailableClients() {
        return availableClients;
    }

    public void addAvailableClients(ProfileName profile) {
        availableClients.put(profile, new ArrayList<TopicZookeeper>());
    }

    public ArrayList<BrokerInZookeeper> getAvailableBrokers(){
        return this.availableBrokers;
    }

    public void addAvailableClients(ProfileName profile, TopicZookeeper topic) {
        ArrayList<TopicZookeeper> topics = this.availableClients.get(profile);
        topics.add(topic);
        availableClients.put(profile, topics);
    }

    public void availableTopics(TopicZookeeper topic) {
        this.availableTopics.add(topic);
    }

    public void addClients(UserZookeeper user){
        this.clients.add(user);
    }

    public void addBroker(BrokerInZookeeper broker){
        this.availableBrokers.add(broker);
        this.brokersConnectionWithTopics.put(broker, null);
    }
    public void addTopicOnBroker(BrokerInZookeeper broker, TopicZookeeper topic){
        ArrayList<TopicZookeeper> topics = this.brokersConnectionWithTopics.get(broker);
        if(topics == null){
            topics = new ArrayList<>();
        }
        topics.add(topic);
        this.brokersConnectionWithTopics.put(broker, topics);
    }

    public int numOfBrokers(){
        return this.availableBrokers.size();
    }

    public int numOfUsers(){
        return this.availableClients.size();
    }

    public void addTopics(TopicZookeeper topic){
        int hash = this.availableTopics.size() % this.availableBrokers.size();
        this.availableTopics.add(topic);
        addTopicOnBroker(this.availableBrokers.get(hash), topic);
    }

    public void printInfo(){
        for(BrokerInZookeeper a : this.getAvailableBrokers()){
            System.out.println("Broker: " + a.id + " Has topics");
            for(TopicZookeeper t : this.brokersConnectionWithTopics.get(a)){
                    System.out.println("- " + t.getTopicName());
            }
            System.out.println("-----------------");

            for(UserZookeeper user :this.clients){
                System.out.println(user.getUserName());
            }
        }
    }

    public String printTopics(){
        int i = 0;
        String message = "";
        for(TopicZookeeper topic : this.availableTopics){
            i++;
            message = message + " ~ " + i + ": " + topic.getTopicName();
        }
        return message;
    }


    public ArrayList<TopicZookeeper> getTopicsFromBroker(BrokerInZookeeper broker){
        return brokersConnectionWithTopics.get(broker);
    }
    public Address getTopicBroker(int id){
        TopicZookeeper topic = this.availableTopics.get(id-1);

      for(BrokerInZookeeper brokerInZookeeper: availableBrokers){
            for(TopicZookeeper t : getTopicsFromBroker(brokerInZookeeper)){
                if(t.getTopicName().equals(topic.getTopicName())){
                    return brokerInZookeeper.getAddress();
                }
            }
      }
      return null;
    }

    public String getTopicNameFromId(int id){
        return this.availableTopics.get(id-1).getTopicName();
    }
}
