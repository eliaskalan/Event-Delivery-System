package controller;

import utils.Config;

import java.io.IOException;

public class Broker2 {
    public static void main(String[] args) throws IOException {
        Broker broker = new Broker(Config.BROKER_2);
        broker.connect();
    }
}
