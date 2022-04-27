package controller;

import model.ProfileName;

import java.util.ArrayList;

public class Topic {
    private String topicName;
    private ArrayList<Message> messages = new ArrayList<Message>();
    private ArrayList<UserTopic> users = new ArrayList<UserTopic>();

    Topic(String topicName){
        this.topicName = topicName;
    }

    public void addUser(ProfileName user){
        users.add(new UserTopic(user, this.messageLength()));
    }

    public void addMessage(String context, String userId, String userName){
        messages.add(new Message(context, userId, userName));
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
        for(int i = lastMessageHasRead + 1; i < messages.size(); i++){
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
        return users.size();
    }

    public String getUserIDbyName(String username){
        String id = "";

        for (UserTopic usertopic: users){
            if(usertopic.getUserName().equals(username)){
                id = usertopic.getUserId();
            }
        }
        System.out.println("Topic.java: ID: " + id);
        return id;
    }

    public String getTopicName(){
        return topicName;
    }

    public static void main(String[] args) {
        Topic topic = new Topic("hello");
        System.out.println(topic.users.size());
        topic.addUser(new ProfileName("user1"));
    }
}
