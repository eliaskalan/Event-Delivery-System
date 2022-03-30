import java.util.ArrayList;

public interface Publisher {
    ProfileName profileName = null;
    ArrayList<Value> generateChunks(MultimediaFile);
    void getBrokerList();
    Broker hashTopic(String);
    void notifyBrokersNewMessage(String);
    void notifyFailure(Broker);
    //TODO put proper names
    void push(String a,Value b);


}
