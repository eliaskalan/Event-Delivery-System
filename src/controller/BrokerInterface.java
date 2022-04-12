package controller;

import java.util.List;


public interface BrokerInterface extends NodeInterface{
    List<ConsumerInterface> registerPublishers = null;
    List<PublisherInterface> registeredPublishers = null;
    //TODO Is void or ConsumerInterface
    void acceptConection(ConsumerInterface cons);
    //TODO Is void or PublisherInterface
    void acceptConection(PublisherInterface publ);
    void calculateKeys();
    //TODO Add proper name
    void filterConsumers(String comsumer);
    void notifyBrokersOnChangers();
    //TODO Add proper name
    void notifyPublisher(String notify);
    //TODO Add proper name
    void pull(String pullfile);
}
