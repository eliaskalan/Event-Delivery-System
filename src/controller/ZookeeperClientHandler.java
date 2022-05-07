package controller;

import model.ProfileName;

import java.io.*;
import java.net.Socket;

public class ZookeeperClientHandler implements Runnable{
    private BufferedReader bufferedReader;
    public BufferedWriter bufferedWriter;
    private InfoTable infoTable;
    private Socket connection;
    public ZookeeperClientHandler(Socket connection, InfoTable infoTable) throws IOException {
        this.connection = connection;
        this.bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        this.infoTable = infoTable;

    }

    public void run(){
        String clientUsername = null;
        try {
            clientUsername = bufferedReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        UserZookeeper user = new UserZookeeper(new ProfileName(clientUsername), this);
        infoTable.addClients(user);
        try {
            this.bufferedWriter= new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            user.clientHandler.bufferedWriter.write(infoTable.printTopics());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            this.bufferedWriter.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            this.bufferedWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String idInputs = null;
        try {
            idInputs = bufferedReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int id = Integer.parseInt(idInputs);
        try {
            user.clientHandler.bufferedWriter.write(infoTable.getTopicNameFromId(id));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            this.bufferedWriter.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            this.bufferedWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Address address = infoTable.getTopicBroker(id);
        if(address != null){
            try {
                user.clientHandler.bufferedWriter.write(address.getIp());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                this.bufferedWriter.newLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                this.bufferedWriter.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                user.clientHandler.bufferedWriter.write(Integer.toString(address.getPort()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                this.bufferedWriter.newLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                this.bufferedWriter.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
