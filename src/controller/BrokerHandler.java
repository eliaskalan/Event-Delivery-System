package controller;

import model.ProfileName;
import utils.Config;

import java.io.*;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class BrokerHandler implements Runnable{
    private Socket connection;
    private Zookeeper zookeeper;
    private BufferedReader bufferedReader;
    public BrokerHandler(Socket connection, InfoTable infoTable) throws IOException {
        this.connection = connection;
        this.bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String ip = bufferedReader.readLine();
        int port = bufferedReader.read();
        System.out.println(ip);
        System.out.println(port);
        BrokerInZookeeper bz = new BrokerInZookeeper(ip, port);
        infoTable.addBroker(bz);
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
