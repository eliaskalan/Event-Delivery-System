package controller;

import utils.Config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;

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

    public String toString(){
        return getId();
    }

}
