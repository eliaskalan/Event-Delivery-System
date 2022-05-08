package controller;

import model.ProfileName;
import utils.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Scanner;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;


public class Client {
    Consumer consumer;
    Publisher publisher;
    ProfileName profileName;
    private Socket socket;
    Client(Address address, String name) throws IOException {
        try{
            this.socket = new Socket(address.getIp(), address.getPort());
            this.consumer = new Consumer(socket);
            this.profileName = new ProfileName(name);
            this.publisher = new Publisher(socket, profileName);
        }catch (IOException e){
            System.out.println("There was a problem in the connection of the client");
        }
    }

    public void initialBroker(String topicName) throws IOException {
        this.publisher.sendOneTimeMessage(this.profileName.getProfileName());
        this.publisher.sendOneTimeMessage(this.profileName.getUserId());
        this.publisher.sendOneTimeMessage(topicName);
    }

    public Address getBrokerAddress() throws IOException {
        String ip = this.consumer.listenForMessageOneTime();
        String port = this.consumer.listenForMessageOneTime();
        return new Address(ip, Integer.parseInt(port));
    }

    public String initialConnectWithZookeeperAndGetTopic(String username) throws IOException {
        this.publisher.sendOneTimeMessage(username);
        System.out.println("Welcome " + username);
        this.consumer.printListenForMessageOneTime();
        String id = Config.readFromUser("Select the topic you want");
        System.out.println(id);
        this.publisher.sendOneTimeMessage(id);
        String topicName = this.consumer.listenForMessageOneTime();
        System.out.println("If you want to exit from topic write " + Config.EXIT_FROM_TOPIC);
        return topicName;
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        String username = Config.readFromUser("What is your name?");
        Client client  = new Client(Config.ZOOKEEPER_CLIENTS, username);
        String topicName = client.initialConnectWithZookeeperAndGetTopic(username);

        //Images
        //client.consumer.listenForImages();
        //client.publisher.sendImage();

        //Messages



        System.out.println("You select " + topicName);
        System.out.println("Complete set up");
        Address address = client.getBrokerAddress();
        client  = new Client(address, username);
        client.initialBroker(topicName);
        while (true){
            try{
                client.consumer.listenForMessage();
                client.publisher.sendMessage();
            }catch (IOException e){
                client.socket.close();
                client  = new Client(Config.ZOOKEEPER_CLIENTS, username);
                topicName = client.initialConnectWithZookeeperAndGetTopic(username);
                System.out.println("You select " + topicName);
                System.out.println("Complete set up");
                address = client.getBrokerAddress();
                client  = new Client(address, username);
                client.initialBroker(topicName);
            }
        }




    }
}
