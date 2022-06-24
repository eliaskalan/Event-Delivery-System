import controller.Broker;
import utils.Config;

import java.io.IOException;

public class Broker1 {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Broker broker = new Broker(Config.BROKER_1);
        broker.connect();
    }
}
