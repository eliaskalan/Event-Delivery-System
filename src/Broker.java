import java.util.List;


public interface Broker {
    List<Consumer> registerPublishers = null;
    List<Publisher> registeredPublishers=null;
    //TODO Add proper name
    Consumer acceptConection(Consumer cons);
    //TODO Add proper name
    Publisher acceptConection(Publisher publ);
    void calculateKeys();
    //TODO Add proper name
    void filterConsumers(String comsumer);
    void notifyBrokersOnChangers();
    //TODO Add proper name
    void notifyPublisher(String notify);
    //TODO Add proper name
    void pull(String pullfile);
}
