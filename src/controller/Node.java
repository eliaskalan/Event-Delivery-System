package controller;
import java.io.*;
import java.net.*;
import java.util.List;

public class Node{
    ServerSocket socket;
    Socket connection = null;
    List<Broker> brokers;
    public int port;
    Node(String ip, int port) {
        this.port = port;
    }

    public void connect(String customMessage) {
        try {
            socket = new ServerSocket(port, 10);
            System.out.println(customMessage);
            while (true) {
                connection = socket.accept();

//                Thread t = new ActionsForClients(connection);
//                t.start();

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




    public void updateNodes() {
    }
}