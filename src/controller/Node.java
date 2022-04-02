package controller;

import java.util.List;

public interface Node {
    List<Broker> brokers=null;

    void connect();
    void disconnect();
    //TODO add proper name
    void init(int number);
    void updateNodes();

    //@TODO This must be  properties i don't know what that is
    //List<Broker> brokers;

}
