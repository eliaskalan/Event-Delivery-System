package controller;

import model.ProfileName;

public class Message {
    private String context;
    private ProfileName user;

    Message(String context, ProfileName user){
        this.context = context;
        this.user = user;
    }

    public String getMessage(){
        return this.context;
    }
    public String getUserId(){
        return  this.user.getUserId();
    }
    public String getUserName(){
        return  this.user.getProfileName();
    }
}
