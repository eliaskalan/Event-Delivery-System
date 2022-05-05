package controller;

import model.ProfileName;
import utils.Config;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class BrokerHandler implements Runnable{
    private Socket connection;
    private Zookeeper zookeeper;
    transient InfoTable infoTable;

    public BrokerHandler(Socket connection){
        this.connection = connection;
        System.out.println(this.connection.getLocalPort());
        System.out.println(this.connection.getLocalAddress());
    }

    public boolean checkClientExist(ProfileName client){
        for (ProfileName clientFromArray : zookeeper.getInfoTable().getAvailableClients().keySet()){
            if(clientFromArray.getUserId().equals(client.getUserId())){
                return true;
            }
        }
        return false;
    }

    public boolean checkBrokerExist(Address brokerAddress, HashMap<Address, ArrayList<String>> topicsAssociatedWithBrokers, HashMap<Address, BigInteger> hashingIDAssociatedWithBrokers){
        if(topicsAssociatedWithBrokers != null){
            for (Address address : topicsAssociatedWithBrokers.keySet()){
                if (brokerAddress.equals(address)){
                    return true;
                }
            }
        }

        if(hashingIDAssociatedWithBrokers != null){
            for (Address address : hashingIDAssociatedWithBrokers.keySet()){
                if (brokerAddress.equals(address)){
                    return true;
                }
            }
        }
        return false;
    }



    public void run(){
        System.out.println("Server: Zookeeper connect with broker in port: " + connection.getPort());
    }
}
