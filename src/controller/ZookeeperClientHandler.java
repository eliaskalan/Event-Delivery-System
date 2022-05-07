package controller;

import model.ProfileName;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ZookeeperClientHandler {
    private BufferedReader bufferedReader;
    private Socket connection;
    public ZookeeperClientHandler(Socket connection, InfoTable infoTable) throws IOException {
        this.connection = connection;
        this.bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String clientUsername = bufferedReader.readLine();
        ProfileName user = new ProfileName(clientUsername);
        infoTable.addClients(user);
    }
}
