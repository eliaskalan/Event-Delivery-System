package controller;

import model.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Consumer{
    Socket requestSocket = null;
    private BufferedReader bufferedReader;

    private Socket socket;
    Consumer(String ip, int port) throws IOException {
        this.socket = new Socket(ip, port);
        try{
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ioException) {

        }
    }
    //ToDo what we do here?
    public void disconnect(String userId) {

    }
    public void register(String register) {

    }

    public void showConversationDate(String name, Value date) {

    }

    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroupChat;
                while (socket.isConnected()) {
                    System.out.println("listen message");
                    try {
                        //ToDo problem with separate thread
                        msgFromGroupChat = bufferedReader.readLine();
                        System.out.println(msgFromGroupChat);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("i pass");

                }
            }
        }).start();
    }
}
