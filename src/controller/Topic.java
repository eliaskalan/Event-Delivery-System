package controller;

import model.ProfileName;

import java.io.IOException;
import java.util.ArrayList;

public class Topic {
    private String topicName;
    private ArrayList<Message> messages;
    private ArrayList<ProfileName> users;

    Topic(String topicName){
        this.topicName = topicName;
    }

    public void addUser(ProfileName user){
        users.add(user);
    }

    public void addMessage(String context, ProfileName user){
        messages.add(new Message(context, user));
    }

    public int getUserNumber(){
        return this.users.size();
    }

    public void deleteUser(String userId){
        for(ProfileName user: users){
            if(user.getUserId().equals(userId)){
                users.remove(user);
                break;
            }
        }
    }

    public void deleteUser(ProfileName user){
        users.remove(user);
    }
}
