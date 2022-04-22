package com.fyp.databaseHelper;

public class Lecture {
    private String lectureID;
    private String lectureName;
    private String lecturer;
    private String day;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String time;


    /**
     * @param s1 lectureID
     * @param s2 lectureName
     * @param s3 lecturer
     * @param s4 day
     * @param s5 time
     */
    Lecture(String s1, String s2, String s3, String s4, String s5){
        lectureID = s1;
        lectureName = s2;
        lecturer = s3;
        day = s4;
        time = s5;
    }

    public void setLectureID(String str){
        lectureID = str;
    }

    public String getLectureID(){
        return lectureID;
    }

    public void setLectureName(String str){
        lectureName = str;
    }

    public String getLectureName(){
        return lectureName;
    }

    public void setLecturer(String str){
        lectureName = str;
    }

    public String getLecturer(){
        return lecturer;
    }


}
