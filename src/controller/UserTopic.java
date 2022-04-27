package controller;

import model.ProfileName;

public class UserTopic {
    ProfileName user;
    int lastMessageHasUserRead;

    Broker.ClientHandler clientHandler;

    UserTopic(ProfileName user, int lastMessage, Broker.ClientHandler clientHandler){
        this.user = user;
        this.lastMessageHasUserRead = lastMessage;
        this.clientHandler = clientHandler;
    }

    public String getUserId(){
        return this.user.getUserId();
    }

    public String getUserName(){
        return this.user.getProfileName();
    }

    public void setLastMessageHasUserRead(int lastMessage){
        this.lastMessageHasUserRead = lastMessage;
    }
}
