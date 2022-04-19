package controller;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static utils.socketMethods.closeEverything;

public class Broker{
    String hash;
    private int port;
    private String ip;
    public ServerSocket socket;

    Broker(String ip, int port){
        this.port = port;
        this.ip = ip;
        calculateKeys();
    }
    public void disconnect() {
        try {
            socket.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    public void connect() {
        try {
            socket = new ServerSocket(port, 10);
            System.out.println("Broker is live!");
            while (true) {
                Socket connection = socket.accept();
                ClientHandler clientSock = new ClientHandler(connection);
                new Thread(clientSock).start();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            disconnect();
        }
    }

    public int getPort(){
        return this.port;
    }

    public String getIp(){
        return this.ip;
    }

    public void calculateKeys() {
        //Todo we want to return the hash key, compare variables or update an array?
        try {
            //  hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Todo we want also to add ip
            byte[] messageDigest = md.digest((Integer.toString(this.getPort()) + this.getIp()).getBytes());

            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            this.hash =  hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        Broker broker = new Broker("localhost", 12345);
        broker.connect();
    }



    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private BufferedReader bufferedReader;
        private BufferedWriter bufferedWriter;
        private String clientUsername;
        public static ArrayList<ClientHandler> registerClient = new ArrayList<>();
        public ClientHandler(Socket socket) throws IOException {
            this.clientSocket = socket;
            try{
                this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                this.bufferedWriter= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                this.clientUsername = bufferedReader.readLine();
                acceptConnection(this);
                this.broadcastMessage("SERVER: " + clientUsername + " has entered the chat!");
            }catch (IOException e){
                removeClient();
                closeEverything(socket, bufferedReader, bufferedWriter);
            }

        }

        private void acceptConnection(ClientHandler client){
            this.registerClient.add(client);
        }

        public void broadcastMessage(String messageToSend) {
            for (ClientHandler client : registerClient) {
                if(!client.clientUsername.equals(clientUsername)){
                    try {
                        client.bufferedWriter.write(messageToSend);
                        client.bufferedWriter.newLine();
                        client.bufferedWriter.flush();
                    } catch (NullPointerException | IOException e) {
                        removeClient();
                        closeEverything(clientSocket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }
        public void removeClient() {
            System.out.println(this.clientUsername + " has left the server");
            registerClient.remove(this);
            broadcastMessage("SERVER:" + this.clientUsername + "has left the chat!");
        }

        public void run() {
            String messageFromClient;
            while (clientSocket.isConnected()) {
                try {
                    messageFromClient = bufferedReader.readLine();
                    broadcastMessage(messageFromClient);
                    System.out.println("Server log: " + messageFromClient);
                } catch (IOException e) {
                    closeEverything(clientSocket, bufferedReader, bufferedWriter);
                    break;
                }
            }
        }
    }
}
