package controller;

import model.ProfileName;
import java.util.ArrayList;

public class Topic {
    private String topicName;
    private ArrayList<Message> messages = new ArrayList<Message>();
    private ArrayList<UserTopic> users = new ArrayList<UserTopic>();

    public Topic(String topicName){
        this.topicName = topicName;
    }

    public void addUser(ProfileName user, Broker.ClientHandler clientHandler){
        users.add(new UserTopic(user, this.messageLength(), clientHandler));
    }


    public String sendAllMessages(){
        String totalMessage = "";
        for(Message message: messages){
            totalMessage = totalMessage + "\n" + message;
        }
        return totalMessage;
    }

    public void addMessage(String context, String userId, String userName,String type){
        messages.add(new Message(context, userId, userName,type));
        for(UserTopic user : this.users){
            if(user.getUserId().equals(userId)){
                user.setLastMessageHasUserRead(user.lastMessageHasUserRead + 1);
            }
        }
    }

    public String getLastMessage(){
        return messages.get(messageLength() - 1).getMessage();
    }

    public String getLastMessageOfUser(String userId){
        for(UserTopic user: users){
            if(user.getUserId().equals(userId)){
                String messages = getMessagesFromLength(user.lastMessageHasUserRead);
                user.setLastMessageHasUserRead(messageLength() - 1);
                return messages;
            }
        }
        return "We don't find any new message";
    }

    public String getMessagesFromLength(int lastMessageHasRead){
        String message = "";


        Message messageObject = messages.get(lastMessageHasRead);
        message = message + "\n" + messageObject.getUserName() + ": " + messageObject.getMessage();

        return message;
    }
    public String getMessagesFromLength(){
        String message = "";
        for(int i = 0; i < messages.size(); i++){
            Message messageObject = messages.get(i);
            message = message + "\n" + messageObject.getUserName() + ": " + messageObject.getMessage();
        }
        return message;
    }


    public int getUserNumber(){
        return this.users.size();
    }

    public void deleteUser(String userId){
        for(UserTopic user: users){
            if(user.getUserId().equals(userId)){
                users.remove(user);
                break;
            }
        }
    }

    public void renameTopic(String name){
        this.topicName = name;
    }

    public int messageLength(){
        return messages.size();
    }

    public String getUserIDbyName(String username){
        String id = "";

        for (UserTopic usertopic: users){
            if(usertopic.getUserName().equals(username)){
                id = usertopic.getUserId();
            }
        }
        return id;
    }

    public String getTopicName(){
        return topicName;
    }

    public ArrayList<Message> getMessages(){
        return messages;
    }

    public ArrayList<UserTopic> getUsers(){
        return users;
    }
}
