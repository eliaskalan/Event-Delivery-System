import controller.Broker;
import utils.Config;

import java.io.IOException;

public class Broker3 {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Broker broker = new Broker(Config.BROKER_3);
        broker.connect();
    }
}
