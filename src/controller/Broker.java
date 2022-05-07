package controller;

import model.MultimediaFile;

import model.ProfileName;

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
    private static ArrayList<Topic> topics = new ArrayList<Topic>();

    Broker(String ip, int port) {
        this.port = port;
        this.ip = ip;
//        calculateKeys();
        topics.add(new Topic("DS"));
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
            while (!socket.isClosed()) {
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
//        System.out.println(broker.calculateKeys("DSasgstbxfgbxfA")); // TODO results must be 0, 1, or 2 because of %3. For some values is -1
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
                bufferedWriter.flush();

                topics.get(0).addUser(new ProfileName(this.clientUsername), this);


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
                        while(index < topic.messageLength()){
                            user.clientHandler.bufferedWriter.write(topic.getMessagesFromLength(index));
                            user.clientHandler.bufferedWriter.newLine();
                            user.clientHandler.bufferedWriter.flush();
                            user.setLastMessageHasUserRead(index);
                            index++;
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
            topics.get(0).addMessage(FILE_TO_RECEIVED,userid,clientUsername,"image");
            readyForPull();
        }
        public void acceptMessage() throws IOException {
            String messageFromClient;
            messageFromClient = bufferedReader.readLine();
            System.out.println("Server log: " + messageFromClient);

            String[] arrOfStr = messageFromClient.split(": ");
            String userid = topics.get(0).getUserIDbyName(arrOfStr[0]);
            topics.get(0).addMessage(arrOfStr[1], userid, arrOfStr[0],"message");
            readyForPull();

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
