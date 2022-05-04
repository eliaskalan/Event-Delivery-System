package controller;

import model.ProfileName;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;


public class Client{
    Consumer consumer;
    Publisher publisher;
    ProfileName profileName;
    private Socket socket;
    Client(String ip, int port, String name) throws IOException {
        try{
            this.socket = new Socket(ip, port);
            this.consumer = new Consumer(socket);
            this.profileName = new ProfileName(name);
            this.publisher = new Publisher(socket, profileName);
        }catch (IOException e){
            System.out.println("There was a problem in the connection of the client");
        }
    }



    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("What is your name?");
        String username = scanner.nextLine();
        Client client  = new Client("localhost", 12345, username);
        /*System.out.println("Give Topic");
        String str_topic = scanner.nextLine();
        Topic topic = new Topic(str_topic);*/
        try{
            client.consumer.listenForMessage();
            client.publisher.sendMessage();
        }catch (IOException e){
            System.out.println("Try again!");
        }

        System.out.println("Welcome " + username);
    }

}
