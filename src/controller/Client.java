package controller;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Node{
    Consumer consumer;
    Publisher publisher;
    Socket socket;
    Client(String ip, int port, String name) throws IOException {
        super(ip, port);
        this.consumer = new Consumer(ip, port);
        this.publisher = new Publisher(ip, port, name);
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("username");
        String username = scanner.nextLine();
        Client client  = new Client("localhost", 12345, username);
        client.consumer.listenForMessage();
        client.publisher.sendMessage();
    }
}
