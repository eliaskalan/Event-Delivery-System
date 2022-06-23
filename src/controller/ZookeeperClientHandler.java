package controller;

import model.ProfileName;
import utils.Config;

import java.io.*;
import java.net.Socket;

public class ZookeeperClientHandler implements Runnable{
    private BufferedReader bufferedReader;
    private ObjectInputStream objectInputStream;
    public BufferedWriter bufferedWriter;
    private ObjectOutputStream objectOutputStream;
    private InfoTable infoTable;
    private Socket connection;
    public ZookeeperClientHandler(Socket connection, InfoTable infoTable) throws IOException {
        this.connection = connection;
        this.bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        InputStream inputStream = connection.getInputStream();
        objectInputStream = new ObjectInputStream(inputStream);
        this.infoTable = infoTable;
    }

    public void run(){
        String clientUsername = Config.readAMessage(bufferedReader);
        UserZookeeper user = new UserZookeeper(new ProfileName(clientUsername), this);
        infoTable.addClients(user);
        try {
            this.bufferedWriter= new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            OutputStream outputStream = connection.getOutputStream();
            objectOutputStream = new ObjectOutputStream(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Config.sendAMessage(user.clientHandler.bufferedWriter, infoTable.printTopics());
        String idInputs = Config.readAMessage(bufferedReader);
        int id = Integer.parseInt(idInputs);
        Config.sendAMessage(user.clientHandler.bufferedWriter, infoTable.getTopicNameFromId(id));
        Address address = infoTable.getTopicBroker(id);
        if(address != null){
            Config.sendAMessage(user.clientHandler.bufferedWriter, address.getIp());
            Config.sendAMessage(user.clientHandler.bufferedWriter,Integer.toString(address.getPort()));
        }
    }
}
