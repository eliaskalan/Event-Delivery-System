package controller;

public class TopicZookeeper {
    private String name;
    private static int id = 0;
    private int topicId;

    public TopicZookeeper(String name){
        this.name = name;
        this.topicId = id;
        TopicZookeeper.id++;
    }

    public String getTopicName(){
        return name;
    }

}
