package controller;

import model.ProfileName;

import java.io.*;
import java.net.Socket;

public class ZookeeperClientHandler {
    private BufferedReader bufferedReader;
    public BufferedWriter bufferedWriter;
    private Socket connection;
    public ZookeeperClientHandler(Socket connection, InfoTable infoTable) throws IOException {
        this.connection = connection;
        this.bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String clientUsername = bufferedReader.readLine();
        UserZookeeper user = new UserZookeeper(new ProfileName(clientUsername), this);
        infoTable.addClients(user);
        this.bufferedWriter= new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        user.clientHandler.bufferedWriter.write(infoTable.printTopics());
        this.bufferedWriter.newLine();
        this.bufferedWriter.flush();
        String[] idInputs = bufferedReader.readLine().split(": ");
        int id = Integer.parseInt(idInputs[1]);
        Address address = infoTable.getTopicBroker(id);
        if(address != null){
            user.clientHandler.bufferedWriter.write(address.getIp());
            this.bufferedWriter.newLine();
            this.bufferedWriter.flush();
            user.clientHandler.bufferedWriter.write(Integer.toString(address.getPort()));
            this.bufferedWriter.newLine();
            this.bufferedWriter.flush();
        }

    }
}
