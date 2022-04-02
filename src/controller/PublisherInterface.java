package controller;
import model.Value;
import model.ProfileName;
import java.util.ArrayList;
import model.MultimediaFile;

public interface PublisherInterface {
    ProfileName profileName = null;
    //TODO Add proper name
    ArrayList<Value> generateChunks(MultimediaFile multFile);
    void getBrokerList();
    //TODO Add proper name
    BrokerInterface hashTopic(String hash);
    //TODO Add proper name
    void notifyBrokersNewMessage(String notify);
    //TODO Add proper name
    void notifyFailure(BrokerInterface notify);
    //TODO put proper names
    void push(String a, Value b);


}
