package controller;

import java.util.List;


public interface BrokerInterface {
    List<ConsumerInterface> registerPublishers = null;
    List<PublisherInterface> registeredPublishers=null;
    //TODO Add proper name
    ConsumerInterface acceptConection(ConsumerInterface cons);
    //TODO Add proper name
    PublisherInterface acceptConection(PublisherInterface publ);
    void calculateKeys();
    //TODO Add proper name
    void filterConsumers(String comsumer);
    void notifyBrokersOnChangers();
    //TODO Add proper name
    void notifyPublisher(String notify);
    //TODO Add proper name
    void pull(String pullfile);
}
