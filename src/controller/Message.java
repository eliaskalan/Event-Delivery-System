package controller;
public class Message {
    private String context;
    private String userId;
    private String userName;
    private String type;
    Message(String context, String user, String userName,String type){
        this.context = context;
        this.userId = user;
        this.userName = userName;
        this.type=type;
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

    public String getType() {
        return this.type;
    }

    public String getContext() { return this.context;}
}
