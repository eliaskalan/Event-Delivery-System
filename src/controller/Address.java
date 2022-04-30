package controller;

import java.io.Serializable;

public class Address implements Serializable {

    private int port;

    private String ip;

    public Address(String ip,int port) {
        this.port = port;
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public String getIp() {
        return ip;
    }

    @Override
    public String toString() {
        return "Port: " + this.port + " IP: " + this.ip;
    }
}
