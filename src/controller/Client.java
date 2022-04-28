package controller;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;


public class Client{
    Consumer consumer;
    Publisher publisher;
    private Socket socket;
    Client(String ip, int port, String name) throws IOException {
        try{
            this.socket = new Socket(ip, port);
            this.consumer = new Consumer(socket);
            this.publisher = new Publisher(socket, name);
        }catch (IOException e){
            System.out.println("There was a problem in the connection of the client");
        }
    }



    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("What is your name?");
        String username = scanner.nextLine();
        Client client  = new Client("localhost", 12345, username);
        try{
            //client.consumer.listenForMessage();
            //client.publisher.sendMessage();

            client.publisher.sendImage();


        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Welcome " + username);
    }

}
