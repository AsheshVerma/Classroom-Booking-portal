
package com.example.roombookingsys.model;

import jakarta.persistence.*;

@Entity
@Table(name = "rooms_database")
public class Room_Records {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roomID;

    @Column(name = "room_name")
    private String roomName;

    @Column(name = "room_capacity")
    private Integer roomCapacity;

    public Room_Records() {
    }

    public Room_Records(String roomName, int roomCapacity) {
        this.roomName = roomName;
        this.roomCapacity = roomCapacity;
    }
    public Room_Records(int roomID,String roomName, Integer roomCapacity) {
        this.roomID = roomID;
        this.roomName = roomName;
        this.roomCapacity = roomCapacity;
    }

    // Getters and setters

    public int getRoomID() {
        return roomID;
    }

    public String getRoomName() {
        return roomName;
    }

    public Integer getRoomCapacity() {
        return roomCapacity;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setRoomCapacity(int roomCapacity) {
        this.roomCapacity = roomCapacity;
    }
}