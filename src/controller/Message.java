package controller;
public class Message {
    private String context;
    private String userId;
    private String userName;
    Message(String context, String user, String userName){
        this.context = context;
        this.userId = user;
        this.userName = userName;
    }

    public String getMessage(){
        return this.context;
    }
    public String getUserId(){
        return  this.userId;
    }

    public String getUserName(){
        return this.userName;
    }
}
