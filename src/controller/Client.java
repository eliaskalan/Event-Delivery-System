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


    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("What is your name?");
        String username = scanner.nextLine();
        Client client  = new Client(Config.ZOOKEEPER_CLIENTS, username);
        client.publisher.sendOneTimeMessage(username);

        //Images
        //client.consumer.listenForImages();
        //client.publisher.sendImage();

        //Messages

        System.out.println("Welcome " + username);
        System.out.println("Select the topic you want");
        client.consumer.printListenForMessageOneTime();
        String id = scanner.nextLine();
        System.out.println(id);
        client.publisher.sendOneTimeMessage(id);
        String topicName = client.consumer.listenForMessageOneTime();
        System.out.println("You select " + topicName);
        String ip = client.consumer.listenForMessageOneTime();
        String port = client.consumer.listenForMessageOneTime();

        System.out.println("Complete set up");
        int portInt = Integer.parseInt(port);
        client  = new Client(new Address(ip, portInt), username);
        
        client.publisher.sendOneTimeMessage(username);
        client.publisher.sendOneTimeMessage(client.profileName.getUserId());
        client.publisher.sendOneTimeMessage(topicName);

        client.consumer.listenForMessage();
        client.publisher.sendMessage();



    }
}
