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

public class Publisher{
    ProfileName profileName;
    private BufferedWriter bufferedWriter;
    private Socket socket;
    Publisher(String ip, int port, String profileName) throws IOException {
        this.socket =  new Socket(ip, port);
        this.profileName = new ProfileName(profileName);
        try{
            this.bufferedWriter= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException ioException) {

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
        bufferedWriter.write(this.profileName.getProfileName());
        bufferedWriter.newLine();
        bufferedWriter.flush();
                try {
                    // Initially send the username of the client.
                    bufferedWriter.write("aaaa");
                    bufferedWriter.newLine();
                    bufferedWriter.flush();

                    // Create a scanner for user input.
                    Scanner scanner = new Scanner(System.in);
                    // While there is still a connection with the server, continue to scan the terminal and then send the message.
                    while (socket.isConnected()) {
                        System.out.println("i send message");
                        String messageToSend = scanner.nextLine();
                        bufferedWriter.write("aaa" + ": " + messageToSend);
                        bufferedWriter.newLine();
                        bufferedWriter.flush();

                    }
                } catch (IOException e) {
                    // Gracefully close everything.
                    try {
                        socket.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
    }
