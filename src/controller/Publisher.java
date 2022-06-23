package controller;


import model.MultimediaFile;
import model.ProfileName;
import model.Value;
import utils.Config;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Scanner;

import static utils.socketMethods.closeEverything;

public class Publisher{

    private BufferedWriter bufferedWriter;
    private ObjectOutputStream objectOutputStream = null;
    private Socket socket;
    ProfileName profileName;
    public static String FILE_TO_SEND = MultimediaFile.FOLDER_SAVE + "new.jpeg";
    Publisher(Socket socket, ProfileName profileName) throws IOException {
        this.socket = socket;
        this.profileName = profileName;
        try{
            OutputStream outputStream = socket.getOutputStream();
            this.objectOutputStream = new ObjectOutputStream(outputStream);

            this.bufferedWriter= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException ioException) {
            System.out.println("There was a problem in the connection of the client");
            closeEverything(socket, bufferedWriter);
        }
    }


    public void sendMessage() throws Exception {
        Scanner scanner = new Scanner(System.in);
        while (socket.isConnected()) {
            System.out.println("Publisher - sendMessage()");

            System.out.println("what type of message do you want to send?\n1. Video or photo\n2. Simple message");
            String msg_type = scanner.nextLine();
            System.out.println("give your message: ");
            String messageToSend = scanner.nextLine();

            // send type identifier
            objectOutputStream.writeObject(msg_type);
            objectOutputStream.flush();

            if(messageToSend.equals(Config.EXIT_FROM_TOPIC)){
                throw new IOException("Go to zookeeper");
            }
            else if (msg_type.equals("1")){ // send video
                System.out.println("empiken sto if gia na stili video h eikona");
                objectOutputStream.writeObject(messageToSend);
                objectOutputStream.flush();
                sendVideo(messageToSend);
            }
            else if(msg_type.equals("2")){ // sendMesasge
                System.out.println("empiken sto if gia na stili kanoniko minima");
                Config.sendAMessage(bufferedWriter, this.profileName.getUserId() + ": " + messageToSend);
            }
        }
    }

    public void sendOneTimeMessage(String messageToSend) throws IOException {
        Config.sendAMessage(bufferedWriter, messageToSend);
    }

    public void sendVideoSignal() throws IOException {
        objectOutputStream.writeObject("signal");
        objectOutputStream.flush();
    }



    public void sendImage() throws IOException {
        DataInputStream dataInputStream=null;
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;

        if(socket.isConnected()) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("File name");
            String file_name = scanner.nextLine();
            FILE_TO_SEND=FILE_TO_SEND+file_name+".jpeg";
            Config.sendAMessage(bufferedWriter,Config.IMAGE_TYPE);


            File myFile = new File(FILE_TO_SEND);
            byte[] mybytearray = new byte[(int) myFile.length()];
            fis = new FileInputStream(myFile);

            bis = new BufferedInputStream(fis);
            bis.read(mybytearray, 0, mybytearray.length);
            os = socket.getOutputStream();

            System.out.println("Sending " + FILE_TO_SEND + "(" + mybytearray.length + " bytes)");
            os.write(mybytearray, 0, mybytearray.length);
            os.flush();
            System.out.println("Done.");
        }

    }

    public void sendVideo(String video_name) throws Exception{
        // thelw na mpei sto path, j na piasei ta chunks arxeia j na ta stili
        // => tha mpei mesa, tha ta valei se pinaka j tha kalei me to path thn receive
        MultimediaFile mf = new MultimediaFile();
        mf.SplitFile(video_name);

        // path pou exw mesa ta splittes arxeia
        File splitFiles = new File(Config.PATH_OF_CHUNKS_FOR_SPLIT_FUNC + video_name + "\\");
        /*FileWriter myWriter = new FileWriter("filename.txt");
        myWriter.write(Config.PATH_OF_CHUNKS_FOR_SPLIT_FUNC + video_name + "\\");
        myWriter.close();*/
        System.out.println(Config.PATH_OF_CHUNKS_FOR_SPLIT_FUNC + video_name + "\\");
        File[] files = splitFiles.getAbsoluteFile().listFiles();

        // send original video name
        objectOutputStream.writeObject(video_name);
        objectOutputStream.flush();

        // send number of chunks
        objectOutputStream.writeObject(files.length);
        objectOutputStream.flush();

        for(File file: files){
            // gia orisma thn sendChunk() dio oullo to path j to onoma tou arxeiou pou thelw na steilw
            // ara en tha allaksw kati sto orisma pou thelw na ths doko

            objectOutputStream.writeObject(file.getName()); // send every chunk name
            objectOutputStream.flush();
            sendChunk(Config.PATH_OF_CHUNKS_FOR_SPLIT_FUNC + video_name + "\\", file.getName());
        }

    }

    private void sendChunk(String pathOfChunks, String chunkName) throws Exception{
        int bytes = 0;
        File file = new File(pathOfChunks + chunkName);
        FileInputStream fileInputStream = new FileInputStream(file);

        objectOutputStream.writeObject(file.length()); // send file size
        objectOutputStream.flush();

        byte[] buffer = new byte[100*1024]; // break file into chunks // send chunks
        while ((bytes=fileInputStream.read(buffer))!=-1){
            objectOutputStream.write(buffer,0,bytes);
            objectOutputStream.flush();
        }

        fileInputStream.close();
    }

}