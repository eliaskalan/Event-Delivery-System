package controller;
import java.io.*;
import java.net.*;
import java.util.List;

public class Node{
    ServerSocket providerSocket;
    Socket connection = null;
    List<Broker> brokers;
    public int port;
    Node(int port) {
        this.port = port;
    }

    public void connect() {
        try {
            providerSocket = new ServerSocket(port, 10);

            while (true) {
                connection = providerSocket.accept();

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
            providerSocket.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }




    public void updateNodes() {
    }
}