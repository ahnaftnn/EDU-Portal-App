package com.ahnaftn.eduportal;

public class RoutineItem {
    String course, day, time, room, faculty;

    public RoutineItem(String day, String time, String room, String faculty) {
        this.day = day;
        this.time = time;
        this.room = room;
        this.faculty = faculty;
    }

    public RoutineItem(String course, String day, String time, String room, String faculty) {
        this.course = course;
        this.day = day;
        this.time = time;
        this.room = room;
        this.faculty = faculty;
    }

    public String getDay() { return day; }
    public String getTime() { return time; }
    public String getRoom() { return room; }
    public String getFaculty() { return faculty; }
}
