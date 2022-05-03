package controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Zookeeper {
    transient InfoTable infoTable;
    Address zookeeperAddress = new Address("localhost", 12345);
    ServerSocket zookeeperServerSocket = null;
    public Zookeeper(){
        infoTable = new InfoTable();
    }

    public void start(){
        try{
            zookeeperServerSocket = new ServerSocket(zookeeperAddress.getPort(), 250);
            System.out.println("Zookeeper start");
            while (true){
                Socket broker = zookeeperServerSocket.accept();
                Thread brokerThread = new BrokerHandler(broker, this);
                brokerThread.start();
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                zookeeperServerSocket.close();
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
        zookeeper.start();
    }

}
