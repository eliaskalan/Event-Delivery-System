package controller;

import model.MultimediaFile;
import model.ProfileName;
import model.Value;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Publisher{
    ProfileName profileName;
    Socket requestSocket = null;
    ObjectOutputStream out = null;
    Publisher(int ip, int port, String profileName) {
        this.profileName = new ProfileName(profileName);
        try{
            this.requestSocket = new Socket(String.valueOf(ip), port);
            this.out = new ObjectOutputStream(this.requestSocket.getOutputStream());
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
    public void push(String a, Value b) {
        //ToDo string a is hash code?
        try{
            out.writeChars(a);
            out.flush();
        }catch (IOException ioException){

        }
    }
}
