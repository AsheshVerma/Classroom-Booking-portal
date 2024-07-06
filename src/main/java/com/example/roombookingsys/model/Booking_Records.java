package com.example.roombookingsys.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "bookings_database")
public class Booking_Records
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookingID;//bookingId
    //@Column(name = "booking_id")


    private int userID;

    private int roomID;

    //@Column(name = "date_of_booking")
    //private Date dateOfBooking;
    private String dateOfBooking;

    //@Column(name = "time_from")
    private String timeFrom;

    //@Column(name = "time_to")
    private String timeTo;

    //@Column(name = "purpose")
    private String purpose;

    public int getBookingID() {
        return bookingID;
    }

    public int getUserID() {
       return userID;
   }
    public int getRoomID() {
       return roomID;
   }
    public /*Date*/String getDateOfBooking() {
        return dateOfBooking;
    }
    public String getTimeFrom() {
        return timeFrom;
    }
    public String getTimeTo() {
        return timeTo;
    }
    public String getPurpose() {
        return purpose;
    }
    public void setBookingID(int bookingId) {
        this.bookingID = bookingId;
    }
    //NO SETTER FOR USERID AND ROOM ID
    public void setDateOfBooking(/*Date*/String dateOfBooking) {
           this.dateOfBooking = dateOfBooking;
    }
    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }
    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public void setUserID(int userid) {
        this.userID = userid;
    }
}
