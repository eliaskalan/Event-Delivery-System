package controller;

import utils.Config;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Zookeeper {
    transient InfoTable infoTable;
    ServerSocket zookeeperServerSocket;
    public Zookeeper(){
        infoTable = new InfoTable();
    }

    public void connect(){
        try{
            zookeeperServerSocket = new ServerSocket(Config.ZOOKEEPER.getPort(), 10);
            System.out.println("Zookeeper start");
            while (true){
                Socket connection = zookeeperServerSocket.accept();
                BrokerHandler brokerThread = new BrokerHandler(connection, infoTable);
                new Thread(brokerThread).start();
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                zookeeperServerSocket.close();
                System.out.println("close zookeeper");
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public synchronized InfoTable getInfoTable(){
        return  infoTable;
    }

    public static void main(String[] args) {
        Zookeeper zookeeper = new Zookeeper();
        zookeeper.connect();
    }

}
