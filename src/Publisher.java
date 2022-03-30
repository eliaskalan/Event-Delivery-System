import java.util.ArrayList;

public interface Publisher {
    ProfileName profileName = null;
    //TODO Add proper name
    ArrayList<Value> generateChunks(MultimediaFile multFile);
    void getBrokerList();
    //TODO Add proper name
    Broker hashTopic(String hash);
    //TODO Add proper name
    void notifyBrokersNewMessage(String notify);
    //TODO Add proper name
    void notifyFailure(Broker notify);
    //TODO put proper names
    void push(String a,Value b);


}
