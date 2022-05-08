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

public class Publisher {
    ProfileName profileName;
    //TODO static name for the image must change
    public  static String FILE_TO_SEND = MultimediaFile.FOLDER_SAVE ;
    private BufferedWriter bufferedWriter;
    private Socket socket;
    Publisher(Socket socket, ProfileName profileName) throws IOException {
        this.socket = socket;
        this.profileName = profileName;
        try{
            this.bufferedWriter= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException ioException) {
            System.out.println("There was a problem in the connection of the client");
            closeEverything(socket, bufferedWriter);
        }
    }


    public void sendMessage(){

        Scanner scanner = new Scanner(System.in);
            if (socket.isConnected()) {
            Config.sendAMessage(bufferedWriter,Config.MESSAGE_TYPE);
            String messageToSend = scanner.nextLine();
            Config.sendAMessage(bufferedWriter, this.profileName.getUserId() + ": " + messageToSend);
        }
    }


    public void sendOneTimeMessage(String messageToSend) throws IOException {
        Config.sendAMessage(bufferedWriter, messageToSend);
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
}