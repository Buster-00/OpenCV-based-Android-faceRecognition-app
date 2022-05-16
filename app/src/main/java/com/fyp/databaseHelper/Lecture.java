package com.fyp.databaseHelper;

public class Lecture {
    private String lectureID;
    private String lectureName;
    private String lecturerID;
    private String date;
    private String venue;

    public Lecture(String lectureID, String lectureName, String lecturerID, String date, String venue) {
        this.lectureID = lectureID;
        this.lectureName = lectureName;
        this.lecturerID = lecturerID;
        this.date = date;
        this.venue = venue;
    }

    public String getLectureID() {
        return lectureID;
    }

    public void setLectureID(String lectureID) {
        this.lectureID = lectureID;
    }

    public String getLectureName() {
        return lectureName;
    }

    public void setLectureName(String lectureName) {
        this.lectureName = lectureName;
    }

    public String getLecturerID() {
        return lecturerID;
    }

    public void setLecturerID(String lecturerID) {
        this.lecturerID = lecturerID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }
}
