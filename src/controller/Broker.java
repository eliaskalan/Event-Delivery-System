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
        System.out.println("Broker topics: ");
        for(Topic topic : topics){
            System.out.println(topic.getTopicName());
        }
        System.out.println("---------------");
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
        private ObjectInputStream objectInputStream;
        private BufferedWriter bufferedWriter;
        private ObjectOutputStream objectOutputStream = null;
        private String clientUsername;
        private String clientId;

        public static ArrayList<ClientHandler> registerClient = new ArrayList<>();
        public ClientHandler(Socket socket) throws IOException {

            this.clientSocket = socket;
            try{
                InputStream inputStream = socket.getInputStream();
                this.objectInputStream = new ObjectInputStream(inputStream);
                this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                this.bufferedWriter= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                this.clientUsername = bufferedReader.readLine();
                String userId = bufferedReader.readLine();
                this.clientId = userId;
                String topicName = bufferedReader.readLine();
                System.out.println(topicName);
                int topicPosition = getPositionOfTopic(topicName);
                topics.get(topicPosition).addUser(new ProfileName(clientUsername, userId), this);
                Config.sendAMessage(bufferedWriter, topics.get(topicPosition).getMessagesFromLength());
                for(UserTopic user: topics.get(topicPosition).getUsers()){
                    Config.sendAMessage(user.clientHandler.bufferedWriter, "SERVER: " + clientUsername + " has entered the chat!" + topics.get(topicPosition).getTopicName());
                }

            }catch (IOException e){
                System.out.println("Remove");
                this.removeClient();
                closeEverything(socket, bufferedReader, bufferedWriter, objectInputStream, objectOutputStream);
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
                        System.out.println("Broker - readyForPull()");
                        int index = user.lastMessageHasUserRead;
                        if(index < topic.messageLength()){
                            // apostolh eidous minimatos ISOS NA MHN XREIAZETAI TELIKA(vlepe periptosis minimatwn)
                            // Config.sendAMessage(user.clientHandler.bufferedWriter, "2");

                            // apostolh minimatos xrhsth
                            Config.sendAMessage(user.clientHandler.bufferedWriter, topic.getMessagesFromLength(index));
                            user.setLastMessageHasUserRead(topic.messageLength());
                        }
                    } catch (NullPointerException e) {
                        this.removeClient();
                        closeEverything(clientSocket, bufferedReader, bufferedWriter, objectInputStream, objectOutputStream);
                    }
                }
            }
        }

        public void broadcastMessage(String messageToSend) {
            for (ClientHandler client : registerClient) {
                if(!client.clientUsername.equals(clientUsername)){
                    try {
                        System.out.println("Broker - broadCastMessage()");
                        Config.sendAMessage(client.bufferedWriter, messageToSend);
                    } catch (NullPointerException  e) {
                        removeClient();
                        closeEverything(clientSocket, bufferedReader, bufferedWriter, objectInputStream, objectOutputStream);
                    }
                }
            }
        }

        public void broadcastImage(String IMAGE_PATH) throws IOException {
            DataInputStream dataInputStream=null;
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            OutputStream os = null;

            if(clientSocket.isConnected()) {
                //Config.sendAMessage(bufferedWriter,Config.IMAGE_TYPE);

                File myFile = new File(IMAGE_PATH);
                byte[] mybytearray = new byte[(int) myFile.length()];
                fis = new FileInputStream(myFile);

                bis = new BufferedInputStream(fis);
                bis.read(mybytearray, 0, mybytearray.length);
                os = clientSocket.getOutputStream();

                System.out.println("Sending " + IMAGE_PATH + "(" + mybytearray.length + " bytes)");
                os.write(mybytearray, 0, mybytearray.length);
                os.flush();
                System.out.println("Done.");
            }
        }


        public void removeClient() {
            System.out.println(this.clientUsername + " has left the server");
            for(Topic topic: topics){
                System.out.println(this.clientId);
                if(topic.removeUserIfExist(this.clientId)){
                    break;
                }
            }
            broadcastMessage("SERVER:" + this.clientUsername + "has left the chat!");
        }

        public final static String
                FILE_TO_RECEIVED = MultimediaFile.FOLDER_SAVE + "BrokersFile\\" + "new_download.jpeg";
        public final static int FILE_SIZE = 6022386;
        public void acceptImage() throws IOException {
            InputStream inputStream = clientSocket.getInputStream();
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
            //If image send works we will add this
            //topics.get(0).addMessage(FILE_TO_RECEIVED,userid,clientUsername,Config.IMAGE_TYPE);
            readyForPull();
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
                        os = clientSocket.getOutputStream();
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

        public void readyForPullWorksWithImage() throws IOException {
            for (Topic topic : topics) {
                for (UserTopic user : topic.getUsers()) {
                    try {
                        int index = user.lastMessageHasUserRead;
                        while(index < topic.messageLength()) {
                            if (topic.getType(index).equals(Config.MESSAGE_TYPE)) {
                                Config.sendAMessage(user.clientHandler.bufferedWriter, topic.getMessagesFromLength(index));
                                index++;
                                user.setLastMessageHasUserRead(index);
                            }
                            else if(topic.getType(index).equals(Config.IMAGE_TYPE)) {
                                Config.sendAMessage(user.clientHandler.bufferedWriter, topic.getMessagesFromLength(index));
                                //Will work with the images
                                //broadcastImage(topic.getContext(index));
                                index++;
                                user.setLastMessageHasUserRead(index);
                            }
                        }


                    } catch (NullPointerException e) {
                        removeClient();
                        closeEverything(clientSocket, bufferedReader, bufferedWriter, objectInputStream, objectOutputStream);
                    }
                }
            }
        }


        public void acceptVideo() throws Exception{
            String chunkName = "";

            String oiginal_video_name = (String) objectInputStream.readObject();

            File splitFile = new File(Config.PATH_OF_CHUNKS_FOR_BROKER + oiginal_video_name + "\\");//Destination folder to sa
            if (!splitFile.exists()) {
                splitFile.mkdirs();
            }

            int numOfChunks = (int) objectInputStream.readObject(); // receive the number of chunks

            for(int i=0; i<numOfChunks; i++){
                chunkName = (String) objectInputStream.readObject(); // receive every chunk name
                receiveChunk(Config.PATH_OF_CHUNKS_FOR_BROKER + oiginal_video_name + "\\" + chunkName);
            }

//            MultimediaFile mf = new MultimediaFile(); einai doulia tou telikou client => del
//            mf.JoinVideo(oiginal_video_name);
        }


        // to path pou thelw na ta lavw, dio to san orisma ths acceptVideo()
        private void receiveChunk(String fileName) throws Exception{
            int bytes = 0;
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);

            long size = (long) objectInputStream.readObject(); // read file size

            byte[] buffer = new byte[100*1024];
            while (size > 0 && (bytes = objectInputStream.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1) {
                fileOutputStream.write(buffer,0,bytes);
                size -= bytes;      // read upto file size
            }
            fileOutputStream.close();
        }

        public void broadcastVideo(String video_name, String folder_for_chunks) throws Exception {
            DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());;
            System.out.println(clientSocket.isConnected());

            for (ClientHandler client : registerClient) {

                // if (!client.clientUsername.equals(clientUsername)) {
                File splitFiles = new File(folder_for_chunks);
                File[] files = splitFiles.getAbsoluteFile().listFiles();
                dataOutputStream.write(files.length);
                for (File file : files) {
                    System.out.println(file.getAbsolutePath());
                    broadcastChunk(file.getAbsolutePath());
                }
                // }
            }
        }
        private void broadcastChunk(String path) throws Exception{
            DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
            int bytes = 0;
            File file = new File(path);
            FileInputStream fileInputStream = new FileInputStream(file);
            // send file size
            dataOutputStream.writeLong(file.length());
            System.out.println("file.length(): " + file.length());
            // break file into chunks
            byte[] buffer = new byte[(int)file.length()];
            while ((bytes=fileInputStream.read(buffer))!=-1){
                dataOutputStream.write(buffer,0,bytes);
                dataOutputStream.flush();
            }
            //fileInputStream.close();
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

        public void readyForPullVideo() throws IOException {
            System.out.println("readyForPullVideo()");
            for (Topic topic : topics) {
                for (UserTopic user : topic.getUsers()) {
                    try {
                        int index = user.lastMessageHasUserRead;
                        if(index < topic.messageLength()){
                            // apostolh eidous minimatos: DEN XREIAZETAI, THA STELNW MES TO MINIMA ENA v/f
                            /*user.clientHandler.bufferedWriter.write("1");
                            bufferedWriter.newLine();
                            bufferedWriter.flush();*/
                            // @TODO allagh gia apostolh video

                            // @TODO  to: topic.getMessagesFromLength(index), kapou prepei na gini LOGO Config.sendAMessage(user.clientHandler.bufferedWriter, topic.getMessagesFromLength(index));
                            Config.sendAMessage(user.clientHandler.bufferedWriter, "v/f Hello");
                            //sendFile("C:\\Users\\user\\OneDrive - aueb.gr\\Desktop\\brokersChunks\\foto.jpg\\01_foto.jpg", user.clientHandler.clientSocket);

//                            user.setLastMessageHasUserRead(topic.messageLength()); @TODO uncomment sthn apostolh vid
                        }
                    } catch (NullPointerException e) {
                        this.removeClient();
//                        closeEverything(clientSocket, bufferedReader, bufferedWriter, objectInputStream, objectOutputStream);
                        closeEverything(clientSocket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }

        private void sendFile(String path, Socket clientSock) {

            File file = new File(path);
            // to exw idi
            // Socket socket = null;
            try {
                // to exw idi
                // socket = new Socket(HOSTNAME, PORT);

                // to impl.
                PrintStream out = new PrintStream(clientSock.getOutputStream(), true);

                out.println(file.getName());
                out.println(file.length());

                System.out.println("Sending " + file.getName() + " (" + file.length() + " bytes) to server...");

                writeFile(file, clientSock.getOutputStream());

                System.out.println("Finished sending " + file.getName() + " to server");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (clientSock != null) {
                    try {
                        clientSock.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        private void writeFile(File file, OutputStream outStream) {
            FileInputStream reader = null;
            try {
                reader = new FileInputStream(file);
                byte[] buffer = new byte[Config.CHUNK_SIZE];
                int pos = 0;
                int bytesRead;
                while ((bytesRead = reader.read(buffer, 0, Config.CHUNK_SIZE)) >= 0) {
                    outStream.write(buffer, 0, bytesRead);
                    outStream.flush();
                    pos += bytesRead;
                    System.out.println(pos + " bytes (" + bytesRead + " bytes read)");
                }
            } catch (IndexOutOfBoundsException e) {
                System.err.println("Error while reading file");
                e.printStackTrace();
            } catch (IOException e) {
                System.err.println("Error while writing " + file.toString() + " to output stream");
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public void run() {
            String messageFromClient;
            while (clientSocket.isConnected()) {
                try {
                    //Images
                    System.out.println("run() tou Broker --> handler");
                    // acceptImage();
                    // broadcastImage();

                    // receive type identifier
                    String msg_type = (String) objectInputStream.readObject();
                    System.out.println("thelw msg_type: " + msg_type);

                    if (msg_type.equals("1")){
                        String video_name = (String) objectInputStream.readObject();
                        acceptVideo();
                        System.out.println("Server log - Vid(): " + video_name);

                        String[] arrOfStr = video_name.split(": "); // gia na mpei to vid sto collection me ta minimata
                        String userid = arrOfStr[0];
                        topics.get(returnTopicFromUserId(userid)).addMessage(arrOfStr[1], userid, returnNameFromTopicAndUserId(topics.get(returnTopicFromUserId(userid)), userid));

                        readyForPullVideo(); // @TODO need set up
                    } else if (msg_type.equals("2")) {
                        messageFromClient = bufferedReader.readLine();
                        System.out.println("Server log: " + messageFromClient);

                        String[] arrOfStr = messageFromClient.split(": ");
                        String userid = arrOfStr[0];
                        topics.get(returnTopicFromUserId(userid)).addMessage(arrOfStr[1], userid, returnNameFromTopicAndUserId(topics.get(returnTopicFromUserId(userid)), userid));

                        readyForPull();
                    }

                } catch (IOException e) {
                    System.out.println("IOException e || Broker -> run()");
                    removeClient();
                    closeEverything(clientSocket, bufferedReader, bufferedWriter, objectInputStream, objectOutputStream);
                    break;
                } catch (NullPointerException e){
                    System.out.println("NullPointerException e || Broker -> run()");
                    removeClient();
                    closeEverything(clientSocket, bufferedReader, bufferedWriter, objectInputStream, objectOutputStream);
                    break;
                } catch (ClassNotFoundException e) {
                    System.out.println("ClassNotFoundException on Broker --> run() --> (probably for video)");
                    throw new RuntimeException(e);
                } catch (Exception e) {
                    System.out.println("Exception on Broker --> run() --> (probably for video)");
                    throw new RuntimeException(e);
                }
            }
        }
    }
}