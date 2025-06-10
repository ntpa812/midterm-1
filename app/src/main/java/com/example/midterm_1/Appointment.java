package com.example.midterm_1;

public class Appointment {
    public String date;
    public String time;
    public String patientEmail;
    public String status;
    public String note;

    public Appointment(String date, String time, String patientEmail, String status, String note) {
        this.date = date;
        this.time = time;
        this.patientEmail = patientEmail;
        this.status = status;
        this.note = note;
    }

    @Override
    public String toString() {
        return date + " " + time + "\n" + patientEmail + "\n" + status + "\n" + (note.isEmpty() ? "-" : note);
    }
}
