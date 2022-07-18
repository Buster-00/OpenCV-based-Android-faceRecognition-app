package com.fyp.databaseHelper;

import android.location.Location;

import java.util.HashMap;

public class UserManager {

    public static Location location;

    public static String userName = new String();

    private static User currentUser = null;

    public static User getCurrentUser(){
        return currentUser;
    }

    public static void initUser(String ID, StudentDB DB){

        currentUser = new User(ID, DB);
    }

    public static void initUser(String ID, LecturerDB.Lecturer lecturer){
        currentUser = new User(ID, lecturer);
    }

    public static class User{
        private String ID;
        private String name;
        private int label;
        private Location location;

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        User(String ID, StudentDB DB){
            setID(ID);
            HashMap<String, String> hashMap = new HashMap<>();
            DB.readById(ID, hashMap);
            setName(hashMap.get("name"));
            userName = hashMap.get("name");
            setLabel(DB.getLabelByID(ID));
        }

        User(String ID, LecturerDB.Lecturer lecturer){
            setID(ID);
            setName(lecturer.getName());
            userName = lecturer.getName();
        }

        public String getID(){ return ID;}

        public void setID(String input) { ID = input;}

        public String getName(){ return name;}

        public void setName(String input) { name = input;}

        public int getLabel() { return  label;}

        public void setLabel(int label) { this.label = label;}
    }
}
