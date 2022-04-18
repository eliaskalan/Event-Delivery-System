package controller;
import java.io.*;
import java.net.*;
import java.util.List;

public class Node{
    public ServerSocket socket;
    Socket connection = null;
    public List<Broker> brokers;
    private int port;
    private String ip;
    Node(String ip, int port) {
        this.port = port;
        this.ip = ip;
    }

    public void connect(String customMessage) {
        try {
            socket = new ServerSocket(port, 10);
            System.out.println(customMessage);
            while (true) {
                System.out.println("A new connection");
                connection = socket.accept();
                System.out.println(connection);
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
                disconnect();
        }
    }

    public void disconnect() {
        try {
            socket.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public int getPort(){
        return this.port;
    }

    public String getIp(){
        return this.ip;
    }


    public void updateNodes() {
    }
}