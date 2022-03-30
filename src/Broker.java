import java.util.List;
import java.util.concurrent.Flow;

public interface Broker {
    List<Consumer> registerPublishers = null;
    List<Publisher> registeredPublishers=null;

    Consumer acceptConection(Consumer);

    Publisher acceptConection(Publisher);
    void calculateKeys();
    void filterConsumers(String);
    void notifyBrokersOnChangers();
    void notifyPublisher(String);
    void pull(String);
}
