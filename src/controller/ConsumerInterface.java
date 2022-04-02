package controller;
import model.Value;
public interface ConsumerInterface {
    void disconnect(String userId);
    void register(String register);
    //TODO Add proper name
    void showConversationDate(String name, Value date);
}
