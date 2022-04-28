package controller;

import model.ProfileName;

public class UserTopic {
    ProfileName user;
    int lastMessageHasUserRead;

    UserTopic(ProfileName user, int lastMessage){
        this.user = user;
        this.lastMessageHasUserRead = lastMessage;
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
