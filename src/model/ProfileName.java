package model;

import utils.Config;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileName {
    String profileName;
    private static int usersNum = 0;
    HashMap<String, ArrayList<Value>>  userVideoFilesMap;
    HashMap<String,Integer> subscribedConversations;
    String userId;

    public ProfileName(String profileName) {
        this.profileName = profileName;
        this.userVideoFilesMap = null;
        this.subscribedConversations = null;
        this.userId = profileName + Config.generateRandomPassword(100);
    }
    public ProfileName(String profileName, String userId) {
        this.profileName = profileName;
        this.userVideoFilesMap = null;
        this.subscribedConversations = null;
        this.userId = userId;
    }

    public String getUserId(){
        return userId;
    }

    public String getProfileName(){
        return  this.profileName;
    }


}
