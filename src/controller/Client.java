package controller;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Node implements Runnable {
    Consumer consumer;
    Publisher publisher;
    Client(String ip, int port, String name) {
        super(ip, port);
        this.consumer = new Consumer(ip, port);
        this.publisher = new Publisher(ip, port, name);
        this.publisher.notifyBrokersNewMessage("hello");
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        String username = scanner.nextLine();
        Client client = new Client("localhost", 1234,  username);
    }

    public static void main(String[] args) throws IOException {
        System.out.println("hello new main client");
    }
}
