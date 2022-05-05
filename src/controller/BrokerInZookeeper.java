package controller;

import utils.Config;

public class BrokerInZookeeper {
    Address address;
    String id;

    public BrokerInZookeeper(String ip, int port){
        this.address = new Address(ip, port);
        this.id = Config.calculateKeys(address);
    }

    public Address getAddress(){
        return address;
    }
    public String getId(){
        return id;
    }
}
