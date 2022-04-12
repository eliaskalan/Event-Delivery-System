package controller;

import java.util.List;

public interface NodeInterface{
    List<BrokerInterface> BROKER_INTERFACES =null;
    List<BrokerInterface> brokers = null;
    void connect();
    void disconnect();
    //ToDo is constructor?
    //void init(int port);
    void updateNodes();
}
