package controller;

import model.MultimediaFile;

import model.ProfileName;
import utils.Config;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
    public Broker(Address address) throws IOException {
        this.port = address.getPort();
        this.ip = address.getIp();
        zookeeperSocket  = new Socket(Config.ZOOKEEPER_BROKERS.getIp(), Config.ZOOKEEPER_BROKERS.getPort());
        this.bufferedWriter= new BufferedWriter(new OutputStreamWriter(zookeeperSocket.getOutputStream()));
        Config.sendAMessage(this.bufferedWriter, this.ip);
        Config.sendAMessage(this.bufferedWriter, this.port);
        this.bufferedReader = new BufferedReader(new InputStreamReader(zookeeperSocket.getInputStream()));
        int topicsNum = Integer.parseInt( this.bufferedReader.readLine());
        for(int i=0; i < topicsNum; i++){
            String topicName = this.bufferedReader.readLine();
            topics.add(new Topic(topicName));
        }
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
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
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
        private InputStreamReader inputStreamReader;
        private InputStream inputStream;







        public static ArrayList<ClientHandler> registerClient = new ArrayList<>();
        public ClientHandler(Socket socket) throws IOException, InterruptedException {

            this.clientSocket = socket;
            try{

                this.inputStream=socket.getInputStream();
                this.inputStreamReader =new InputStreamReader(this.inputStream);

                this.bufferedReader = new BufferedReader(this.inputStreamReader);
                this.bufferedWriter= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));


                this.clientUsername = bufferedReader.readLine();
                String userId = bufferedReader.readLine();
                String topicName = bufferedReader.readLine();
                System.out.println(topicName);
                int topicPosition = getPositionOfTopic(topicName);
                topics.get(topicPosition).addUser(new ProfileName(clientUsername, userId), this);
                Config.sendAMessage(bufferedWriter, topics.get(topicPosition).getMessagesFromLength());
                for(UserTopic user: topics.get(topicPosition).getUsers()){
                    Config.sendAMessage(user.clientHandler.bufferedWriter, "SERVER: " + clientUsername + " has entered the chat!" + topics.get(topicPosition).getTopicName());
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
                        while(index < topic.messageLength()){
                            Config.sendAMessage(user.clientHandler.bufferedWriter, topic.getMessagesFromLength(index));
                            user.setLastMessageHasUserRead(index);
                            index++;
                        }
                        
                    } catch (NullPointerException e) {
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
                        Config.sendAMessage(client.bufferedWriter, messageToSend);
                    } catch (NullPointerException  e) {
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
        //TODO static name for the image must change
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


            fos = new FileOutputStream(FILE_TO_RECEIVED);
            bos = new BufferedOutputStream(fos);
            bytesRead = inputStream.read(mybytearray,0,mybytearray.length);

            current = bytesRead;

            do {
                try{
                    bytesRead =inputStream.read(mybytearray, current, (mybytearray.length-current));

                if(bytesRead >= 0) {
                    current += bytesRead;
                }
                }catch (IOException e)
                {
                    bytesRead=-1;
                }
            } while(bytesRead > -1);

            bos.write(mybytearray, 0 , current);
            bos.flush();
            System.out.println(current);
            System.out.println("File " + FILE_TO_RECEIVED
                    + " downloaded (" + current + " bytes read)");
            String userid = topics.get(0).getUserIDbyName(clientUsername);
            topics.get(0).addMessage(FILE_TO_RECEIVED,userid,clientUsername,Config.IMAGE_TYPE);
            readyForPull();
        }
        public void acceptMessage() throws IOException {
                String messageFromClient = bufferedReader.readLine();
                System.out.println("Server log: " + messageFromClient);

                String[] arrOfStr = messageFromClient.split(": ");
                String userid = arrOfStr[0];
                String context=arrOfStr[1];
                String username = returnNameFromTopicAndUserId(topics.get(returnTopicFromUserId(userid)),userid);
                topics.get(returnTopicFromUserId(userid)).addMessage(context,userid,username,Config.MESSAGE_TYPE);
                readyForPull();

        }


        public  int returnTopicFromUserId(String id){
            int i = 0;
            for(Topic topic : topics){
                for(UserTopic user : topic.getUsers()){
                    if(user.getUserId().equals(id)){
                        return i;
                    }
                }
                i++;
            }
            System.out.println("We don't find user with userId " + id);
            return 0;
        }

        public String returnNameFromTopicAndUserId(Topic topic, String id){
            for(UserTopic user: topic.getUsers()){
                if(user.getUserId().equals(id)){
                    return user.getUserName();
                }
            }
            System.out.println("We don't find name " + id);
            return "";
        }
        public void run() {

            while (clientSocket.isConnected()) {
                try {
                    //Images

                    //acceptImage();
                    //broadcastImage();

                    // Messages
                    acceptMessage();
//
                    

                } catch (IOException e) {
                    //closeEverything(clientSocket, bufferedReader, bufferedWriter);

                }
            }
        }
    }
}
