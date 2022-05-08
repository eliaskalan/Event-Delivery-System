package controller;

import model.ProfileName;

public class UserZookeeper {
    ProfileName user;

    ZookeeperClientHandler clientHandler;

    UserZookeeper(ProfileName user, ZookeeperClientHandler clientHandler){
        this.user = user;
        this.clientHandler = clientHandler;
    }

    public String getUserId(){
        return this.user.getUserId();
    }

    public String getUserName(){
        return this.user.getProfileName();
    }
}
