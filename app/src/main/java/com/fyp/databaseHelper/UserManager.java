package com.fyp.databaseHelper;

import java.util.HashMap;

public class UserManager {

    private static User currentUser = null;

    public static User getCurrentUser(){
        return currentUser;
    }

    public static void initUser(String ID, Student DB){
        currentUser = new User(ID, DB);
    }

    public static class User{
        private String ID;
        private String name;
        private int label;

        User(String ID, Student DB){
            setID(ID);
            HashMap<String, String> hashMap = new HashMap<>();
            DB.readById(ID, hashMap);
            setName(hashMap.get("name"));
            setLabel(DB.getLabelByID(ID));
        }

        public String getID(){ return ID;}

        public void setID(String input) { ID = input;}

        public String getName(){ return name;}

        public void setName(String input) { name = input;}

        public int getLabel() { return  label;}

        public void setLabel(int label) { this.label = label;}
    }
}
