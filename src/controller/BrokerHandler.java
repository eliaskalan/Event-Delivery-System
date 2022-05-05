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

public class BrokerHandler extends Thread implements Runnable{
    private Socket connection;
    private Zookeeper zookeeper;
    transient InfoTable infoTable;
    ObjectOutputStream out = null;
    ObjectInputStream in = null;
    public BrokerHandler(Socket connection){
        this.connection = connection;
        try{
            out = new ObjectOutputStream(connection.getOutputStream());
            in = new ObjectInputStream(connection.getInputStream());
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("We have an error");
        }
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
        while (this.connection.isConnected()){
            infoTable.addBroker(new Broker(Config.BROKER_1));
            infoTable.addBroker(new Broker(Config.BROKER_2));
            infoTable.addBroker(new Broker(Config.BROKER_3));
        }
    }
}
