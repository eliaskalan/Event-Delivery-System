package model;

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
        this.userId = Integer.toString(usersNum);
        usersNum++;
    }

    public int getUserId(){
        return usersNum;
    }

    public String getProfileName(){
        return  this.profileName;
    }


}
