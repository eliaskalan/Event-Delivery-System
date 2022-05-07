package controller;

import utils.Config;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Zookeeper {
    transient InfoTable infoTable;
    ServerSocket zookeeperServerSocket;
    ServerSocket zookeeperClients;
    public Zookeeper(){
        infoTable = new InfoTable();
    }

    public void connect(){
        try{
            zookeeperServerSocket = new ServerSocket(Config.ZOOKEEPER_BROKERS.getPort(), 10);
            zookeeperClients = new ServerSocket(Config.ZOOKEEPER_CLIENTS.getPort(), 10);

            System.out.println("Zookeeper start");
            while (true){
                if(infoTable.numOfBrokers() < Config.NUMBER_OF_BROKERS){
                    Socket connection = zookeeperServerSocket.accept();
                    BrokerHandler brokerThread = new BrokerHandler(connection, infoTable);
                    new Thread(brokerThread).start();
                    if(infoTable.numOfBrokers() == Config.NUMBER_OF_BROKERS){
                        infoTable.addTopics(Config.TOPIC_1);
                        infoTable.addTopics(Config.TOPIC_2);
                        infoTable.addTopics(Config.TOPIC_3);
                        infoTable.addTopics(Config.TOPIC_4);
                        infoTable.addTopics(Config.TOPIC_5);
                        infoTable.addTopics(Config.TOPIC_6);
                        System.out.println("start send");
                        for(BrokerInZookeeper broker: infoTable.getAvailableBrokers()){
                            System.out.println(infoTable.getTopicsFromBroker(broker).size());
                            broker.brokerHandlers.sendTopics(infoTable.getTopicsFromBroker(broker));
                        }
                       // infoTable.printInfo();
                    }
                }else{
                    Socket connection = zookeeperClients.accept();
                    ZookeeperClientHandler zookeeperClientHandler = new ZookeeperClientHandler(connection, infoTable);
                   // new Thread(zookeeperClientHandler).start();
                   // infoTable.printInfo();
                }
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
