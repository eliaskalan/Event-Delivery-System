package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class BrokerHandler extends Thread{
    private Socket connection;
    private Zookeeper zookeeper;
    private static final int UPDATE_INFOTABLE = 0;
    private static final int UPDATE_ON_DELETE = 1;
    private static final int UPDATE_ID = 2;
    ObjectOutputStream out = null;
    ObjectInputStream in = null;
    public BrokerHandler(Socket connection, Zookeeper zookeeper){
        this.connection = connection;
        this.zookeeper = zookeeper;
        try{
            out = new ObjectOutputStream(connection.getOutputStream());
            in = new ObjectInputStream(connection.getInputStream());
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("We have an error");
        }
    }

    public synchronized void updateInfoTable(){

    }

    public synchronized void updateId(){

    }

    public synchronized void updateDelete(){
        
    }

    public boolean checkClientExist(Client client){
        for (Client clientFromArray : zookeeper.getInfoTable().getAvailableClients().keySet()){
            if(clientFromArray.profileName.getUserId().equals(client.profileName.getUserId())){
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



    @Override
    public void run(){
        System.out.println("Server: Zookeeper connect with broker in port: " + connection.getPort());
    }
}
