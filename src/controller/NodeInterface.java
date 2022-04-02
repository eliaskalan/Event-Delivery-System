package controller;

import java.util.List;

public interface NodeInterface {
    List<BrokerInterface> BROKER_INTERFACES =null;
    List<BrokerInterface> brokers = null;
    void connect();
    void disconnect();
    //TODO add proper name
    void init(int number);
    void updateNodes();
}
