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
    private BufferedWriter bufferedWriter;
    public BrokerHandler(Socket connection, InfoTable infoTable) throws IOException {
        this.connection = connection;
        this.bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String ip = bufferedReader.readLine();
        int port = bufferedReader.read();
        BrokerInZookeeper bz = new BrokerInZookeeper(ip, port, this);
        infoTable.addBroker(bz);
    }

    public void sendTopics(ArrayList<TopicZookeeper> topics) throws IOException {
        this.bufferedWriter= new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        Config.sendAMessage(this.bufferedWriter, Integer.toString(topics.size()));
        for(TopicZookeeper topic: topics){
            Config.sendAMessage(this.bufferedWriter, topic.getTopicName());
        }

    }


    public void run(){
        System.out.println("Server: Zookeeper connect with broker");
    }
}
