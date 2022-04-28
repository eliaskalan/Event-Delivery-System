package controller;

<<<<<<< Updated upstream
=======
import model.ProfileName;
import  java.util.Random;

>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
=======
    private static ArrayList<Topic> topics = new ArrayList<Topic>();
    private static int thesi;
>>>>>>> Stashed changes

    Broker(String ip, int port){
        this.port = port;
        this.ip = ip;
<<<<<<< Updated upstream
        calculateKeys();
=======
//        calculateKeys();
        topics.add(new Topic("DS-A"));
        topics.add(new Topic("DS-B"));
        topics.add(new Topic("DS-C"));
>>>>>>> Stashed changes
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

<<<<<<< Updated upstream


    private static class ClientHandler implements Runnable {
=======
    public static class ClientHandler implements Runnable {
        private int indexForTopic;
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
                acceptConnection(this);
                this.broadcastMessage("SERVER: " + clientUsername + " has entered the chat!");
=======
                Random rand = new Random();

                this.indexForTopic = rand.nextInt(3);

                topics.get(this.indexForTopic).addUser(new ProfileName(clientUsername), this);
                this.bufferedWriter.write(topics.get(this.indexForTopic).getMessagesFromLength());
                this.bufferedWriter.newLine();
                this.bufferedWriter.flush();
                for(UserTopic user: topics.get(this.indexForTopic).getUsers()){
                    user.clientHandler.bufferedWriter.write( "SERVER: " + clientUsername + " has entered the chat!");
                    user.clientHandler.bufferedWriter.newLine();
                    user.clientHandler.bufferedWriter.flush();
                }

>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
=======

                    String[] arrOfStr = messageFromClient.split(": ");
                    String userid = topics.get(this.indexForTopic).getUserIDbyName(arrOfStr[0]);
                    topics.get(this.indexForTopic).addMessage(arrOfStr[1], userid, arrOfStr[0]);
                    readyForPull();

>>>>>>> Stashed changes
                } catch (IOException e) {
                    closeEverything(clientSocket, bufferedReader, bufferedWriter);
                    break;
                }
            }
        }
    }
}
