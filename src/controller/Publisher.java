package controller;

import model.MultimediaFile;
import model.ProfileName;
import model.Value;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Publisher extends Node{
    ProfileName profileName;
    Socket requestSocket = null;
    ObjectOutputStream out = null;
    Publisher(int ip, int port, String profileName) {
        super(port);
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
        //ToDo what is the purpose of this method?
        for(Broker x : brokers){
            System.out.println(x);
        }
    }

    public Broker hashTopic(String hash) {
        //ToDo what is the purpose of this method?
        for(Broker x : brokers){
            if(x.hash.compareTo(hash) == 0){
                return x;
            }
        }
        return null;
    }

    public void notifyBrokersNewMessage(String notify) {

    }

    public void notifyFailure(BrokerInterface notify) {

    }
    public void push(String a, Value b) {
        //ToDo string is hash code?
        try{
            out.writeChars(a);
            out.flush();
        }catch (IOException ioException){

        }
    }
}
