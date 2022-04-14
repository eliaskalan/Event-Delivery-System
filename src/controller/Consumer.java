package controller;

import model.Value;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Consumer{
    Socket requestSocket = null;
    ObjectOutputStream in = null;
    Consumer(String ip, int port) {
        try{
            this.requestSocket = new Socket(ip, port);
            this.in = new ObjectOutputStream(this.requestSocket.getOutputStream());
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
}
