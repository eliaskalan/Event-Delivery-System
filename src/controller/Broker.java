package controller;

import model.MultimediaFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import model.ProfileName;
import utils.Config;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import static java.lang.Thread.sleep;
import static utils.socketMethods.closeEverything;

public class Broker {
    String hash;
    private int port;
    private String ip;
    public ServerSocket socket;
    private Socket zookeeperSocket;
    private static ArrayList<Topic> topics = new ArrayList<Topic>();
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    Broker(Address address) throws IOException {
        this.port = address.getPort();
        this.ip = address.getIp();
        zookeeperSocket  = new Socket(Config.ZOOKEEPER_BROKERS.getIp(), Config.ZOOKEEPER_BROKERS.getPort());
        this.bufferedWriter= new BufferedWriter(new OutputStreamWriter(zookeeperSocket.getOutputStream()));
        bufferedWriter.write(this.ip);
        bufferedWriter.newLine();
        bufferedWriter.flush();
        System.out.println(this.port);
        bufferedWriter.write(this.port);
        bufferedWriter.newLine();
        bufferedWriter.flush();
        this.bufferedReader = new BufferedReader(new InputStreamReader(zookeeperSocket.getInputStream()));
        String topicName = this.bufferedReader.readLine();
       // System.out.println(topicName);
        while (topicName != ""){
            System.out.println(topicName);
            topics.add(new Topic(topicName));
            topicName = this.bufferedReader.readLine();
        }
        for(Topic topic : topics){
            System.out.println(topic.getTopicName());
        }
//        calculateKeys();
       topics.add(new Topic("DS"));
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

    public void addTopic(Topic topic){
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
                String topicName = bufferedReader.readLine();
                String id = bufferedReader.readLine();
                int topicPosition = getPositionOfTopic(topicName);
                topics.get(topicPosition).addUser(new ProfileName(clientUsername, id), this);
                this.bufferedWriter.write( topics.get(topicPosition).getMessagesFromLength());
                this.bufferedWriter.newLine();
                this.bufferedWriter.flush();
                for(UserTopic user: topics.get(topicPosition).getUsers()){
                    user.clientHandler.bufferedWriter.write( "SERVER: " + clientUsername + " has entered the chat!" + topics.get(topicPosition).getTopicName());
                    user.clientHandler.bufferedWriter.newLine();
                    user.clientHandler.bufferedWriter.flush();
                }

            }catch (IOException e){
                removeClient();
                closeEverything(socket, bufferedReader, bufferedWriter);
            }

        }

        public int getPositionOfTopic(String topic){
            for(int i=0; i < topics.size(); i++){
                if(topics.get(i).getTopicName().equals(topic)){
                    return i;
                }
            }
            System.out.println("We don't find topic");
            return 0;
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

        public final static String
                FILE_TO_RECEIVED = MultimediaFile.FOLDER_SAVE + "BrokersFile\\" + "new_download.jpeg";
        public final static int FILE_SIZE = 6022386;

        public void acceptImage() throws IOException {
            int bytesRead;
            int current = 0;
            FileOutputStream fos = null;
            BufferedOutputStream bos = null;
            System.out.println("Connecting...");
            // receive file
            byte [] mybytearray  = new byte [FILE_SIZE];
            InputStream is = clientSocket.getInputStream();
            fos = new FileOutputStream(FILE_TO_RECEIVED);
            bos = new BufferedOutputStream(fos);
            bytesRead = is.read(mybytearray,0,mybytearray.length);

            current = bytesRead;
            do {
                bytesRead =
                        is.read(mybytearray, current, (mybytearray.length-current));
                if(bytesRead >= 0) current += bytesRead;
            } while(bytesRead > -1);

            bos.write(mybytearray, 0 , current);
            bos.flush();
            System.out.println(current);
            System.out.println("File " + FILE_TO_RECEIVED
                    + " downloaded (" + current + " bytes read)");
        }
        public void broadcastImage() {
            for (ClientHandler client : registerClient) {
                if (!client.clientUsername.equals(clientUsername)) {
                    FileInputStream fis = null;
                    BufferedInputStream bis = null;
                    OutputStream os = null;


                    System.out.println("Waiting...");
                    try {
                        System.out.println("Accepted connection : " + clientSocket);
                        File myFile = new File(FILE_TO_RECEIVED);
                        byte[] mybytearray = new byte[(int) myFile.length()];
                        fis = new FileInputStream(myFile);
                        bis = new BufferedInputStream(fis);
                        bis.read(mybytearray, 0, mybytearray.length);
                        os = clientSocket.
                                getOutputStream();
                        System.out.println("Sending " + FILE_TO_RECEIVED + "(" + mybytearray.length + " bytes)");
                        os.write(mybytearray, 0, mybytearray.length);
                        os.flush();
                        System.out.println("Done.");
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        public void run() {
            String messageFromClient;
            while (clientSocket.isConnected()) {
                try {
                    //Images

                    // acceptImage();
                    // broadcastImage();

                    // Messages
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
