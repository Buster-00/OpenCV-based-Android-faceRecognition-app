package com.fyp.databaseHelper;

import java.util.HashMap;

public class UserManager {

    private static User currentUser = null;

    public static User getCurrentUser(){
        return currentUser;
    }

    public static void initUser(String ID, StudentDB DB){
        currentUser = new User(ID, DB);
    }

    public static void initUser(String ID, LecturerDB DB){
        currentUser = new User(ID, DB);
    }

    public static class User{
        private String ID;
        private String name;
        private int label;

        User(String ID, StudentDB DB){
            setID(ID);
            HashMap<String, String> hashMap = new HashMap<>();
            DB.readById(ID, hashMap);
            setName(hashMap.get("name"));
            setLabel(DB.getLabelByID(ID));
        }

        User(String ID, LecturerDB DB){
            setID(ID);
            LecturerDB.Lecturer lecturer = DB.ReadByID(ID);
            setName(lecturer.getName());
        }

        public String getID(){ return ID;}

        public void setID(String input) { ID = input;}

        public String getName(){ return name;}

        public void setName(String input) { name = input;}

        public int getLabel() { return  label;}

        public void setLabel(int label) { this.label = label;}
    }
}
