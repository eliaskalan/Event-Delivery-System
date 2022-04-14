package controller;

public class Client extends Node{
    Consumer consumer;
    Publisher publisher;
    Client(int ip, int port, String name) {
        super(port);
        this.consumer = new Consumer(ip, port);
        this.publisher = new Publisher(ip, port, name);
    }
}
