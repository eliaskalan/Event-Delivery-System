package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class BrokerHandler extends Thread{
    private Socket connection;
    private Zookeeper zookeeper;
    private static final int UPDATE_INFOTABLE = 0;
    private static final int UPDATE_ON_DELETE = 1;
    private static final int UPDATE_ID = 2;
    ObjectOutputStream out = null;
    ObjectInputStream in = null;
    public BrokerHandler(Socket connection, Zookeeper zookeeper){
        this.connection = connection;
        this.zookeeper = zookeeper;
        try{
            out = new ObjectOutputStream(connection.getOutputStream());
            in = new ObjectInputStream(connection.getInputStream());
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("We have an error");
        }
    }

    @Override
    public void run(){
        System.out.println("Server: Zookeeper connect with broker in port: " + connection.getPort());
    }
}
