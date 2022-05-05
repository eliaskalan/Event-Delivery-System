package controller;

import utils.Config;

import java.net.InetAddress;

public class BrokerInZookeeper {
    Address address;
    String id;

    public BrokerInZookeeper(InetAddress ip, int port){
        this.address = new Address(ip.getHostAddress(), port);
        this.id = Config.calculateKeys(address);
    }

    public Address getAddress(){
        return address;
    }
    public String getId(){
        return id;
    }

    public String toString(){
        return getId();
    }
}
