package controller;

import model.ProfileName;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static utils.socketMethods.closeEverything;

public class Broker {
    String hash;
    private int port;
    private String ip;
    public ServerSocket socket;
    private static ArrayList<Topic> topics = new ArrayList<Topic>();

    Broker(String ip, int port) {
        this.port = port;
        this.ip = ip;
//        calculateKeys();
       // topics.add(new Topic("DS"));
    }

    Broker(Address address){
        this.port = address.getPort();
        this.ip = address.getIp();
        connect();
    }

    public int getTopicsLength(){
        return topics.size();
    }
    public void disconnect() {
        try {
            socket.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void  addTopic(Topic topic){
        topics.add(topic);
    }
    public void connect() {
        try {
            socket = new ServerSocket(port, 10);
            System.out.println("Broker ip: " + this.ip + " port" + this.port + " is live");
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

    public String status(){
        if(socket == null){
            return  "Dead";
        }
        if(!socket.isClosed()){
            return "Broker ip: " + this.ip + " port" + this.port + " is live";
        }else{
            return "Broker ip: " + this.ip + " port" + this.port + " is dead";
        }
    }
    public int getPort() {
        return this.port;
    }

    public String getIp() {
        return this.ip;
    }

    public int calculateKeys(String topicname) {
        //Todo we want to return the hash key, compare variables or update an array?
        try {
            String hashtext1, hashtext2;

            //  hashing MD5
            MessageDigest md1 = MessageDigest.getInstance("MD5");
            MessageDigest md2 = MessageDigest.getInstance("MD5");
            //Todo we want also to add ip

            byte[] messageDigest1 = md1.digest((Integer.toString(this.getPort()) + this.getIp()).getBytes());
            byte[] messageDigest2 = md2.digest(topicname.getBytes());

            BigInteger no1 = new BigInteger(1, messageDigest1);
            hashtext1 = no1.toString(16);
            BigInteger no2 = new BigInteger(1, messageDigest2);
            hashtext2 = no2.toString(16);

            while (hashtext1.length() < 32) {
                hashtext1 = "0" + hashtext1;
            }
            while (hashtext2.length() < 32) {
                hashtext2 = "0" + hashtext2;
            }

            String hashText = hashtext1 + hashtext2;

//            this.hash =  hashText.hashCode();

            return (hashText.hashCode()) % 3;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }



    public static void main(String[] args) throws IOException {
        Broker broker = new Broker("localhost", 12345);
        broker.connect();
    }

    public static class ClientHandler implements Runnable {
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
                topics.get(0).addUser(new ProfileName(clientUsername), this);
                this.bufferedWriter.write( topics.get(0).getMessagesFromLength());
                this.bufferedWriter.newLine();
                this.bufferedWriter.flush();
                for(UserTopic user: topics.get(0).getUsers()){
                    user.clientHandler.bufferedWriter.write( "SERVER: " + clientUsername + " has entered the chat!");
                    user.clientHandler.bufferedWriter.newLine();
                    user.clientHandler.bufferedWriter.flush();
                }

            }catch (IOException e){
                removeClient();
                closeEverything(socket, bufferedReader, bufferedWriter);
            }

        }


        public void readyForPull() throws IOException {
            for (Topic topic : topics) {
                for (UserTopic user : topic.getUsers()) {
                    try {
                        int index = user.lastMessageHasUserRead;
                        if(index < topic.messageLength()){
                            user.clientHandler.bufferedWriter.write(topic.getMessagesFromLength(index));
                            user.clientHandler.bufferedWriter.newLine();
                            user.clientHandler.bufferedWriter.flush();
                            user.setLastMessageHasUserRead(topic.messageLength());
                        }
                    } catch (NullPointerException | IOException e) {
                        removeClient();
                        closeEverything(clientSocket, bufferedReader, bufferedWriter);
                    }
                }
            }
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
                    System.out.println("Server log: " + messageFromClient);

                    String[] arrOfStr = messageFromClient.split(": ");
                    String userid = topics.get(0).getUserIDbyName(arrOfStr[0]);
                    topics.get(0).addMessage(arrOfStr[1], userid, arrOfStr[0]);
                    readyForPull();

                } catch (IOException e) {
                    closeEverything(clientSocket, bufferedReader, bufferedWriter);
                    break;
                }
            }
        }
    }
}
