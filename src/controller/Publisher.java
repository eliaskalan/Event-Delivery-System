package controller;

import model.MultimediaFile;
import model.ProfileName;
import model.Value;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import static utils.socketMethods.closeEverything;

public class Publisher{
    ProfileName profileName;
    private BufferedWriter bufferedWriter;
    private Socket socket;
    Publisher(Socket socket, String profileName) throws IOException {
        this.socket = socket;
        this.profileName = new ProfileName(profileName);

        try{
            this.bufferedWriter= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException ioException) {
            System.out.println("There was a problem in the connection of the client");
            closeEverything(socket, bufferedWriter);
        }


    }

    public ArrayList<Value> generateChunks(MultimediaFile multFile) {
        return null;
    }
    public void getBrokerList() {

    }

    public Broker hashTopic(String hash) {
        return null;
    }

    public void notifyBrokersNewMessage(String notify) {

    }

    public void notifyFailure(Broker notify) {

    }

//    public void push(String a, Value b) {
//        //ToDo string a is hash code?
//        try{
//            out.writeChars(a);
//            out.flush();
//        }catch (IOException ioException){
//
//        }
//    }

    public void sendMessage() throws IOException {
                try {
                    bufferedWriter.write(this.profileName.getProfileName());
                    bufferedWriter.newLine();
                    bufferedWriter.flush();


                    Scanner scanner = new Scanner(System.in);
                    while (socket.isConnected()) {
                        String messageToSend = scanner.nextLine();
                        bufferedWriter.write(this.profileName.getProfileName() + ": " + messageToSend);
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                    }
                } catch (IOException e) {
                    closeEverything(socket, bufferedWriter);
                }
            }
    }
