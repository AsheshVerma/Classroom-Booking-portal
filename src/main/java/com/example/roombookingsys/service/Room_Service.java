package com.example.roombookingsys.service;

import com.example.roombookingsys.Error.Error;
import com.example.roombookingsys.model.Booking_Records;
import com.example.roombookingsys.model.Room_Records;
import com.example.roombookingsys.repositry.Booking_Repositry;
import com.example.roombookingsys.repositry.Room_Repositry;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class Room_Service
{
    @Autowired
    private Room_Repositry room_repositry;
    @Autowired
    private Booking_Repositry booking_Repositry;

    public ResponseEntity<?> delete_room(int roomID)
    {
        if (room_repositry.existsByroomID(roomID))
        {
            // If the room exists, delete it
            room_repositry.deleteByroomID(roomID);
            booking_Repositry.deleteByroomID(roomID);
            return new ResponseEntity<>(" Room deleted successfully", HttpStatus.OK);
        }
        else
        {
            // If the room does not exist, return false
//            Error error=new Error("Room does not exist");
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room does not exist");
        }
    }

    public ResponseEntity<?> get_rooms( Integer roomCapacity)
    {
        List<Room_Records> rooms;
        if (roomCapacity == null) {
            rooms = room_repositry.findAll();
        } else {
            if (roomCapacity <= 0) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid parameters");
            } else {
                rooms = room_repositry.findByroomCapacityGreaterThanEqual(roomCapacity);
            }
        }

        if (rooms.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No rooms found");
        }

        List<Map<String, Object>> response = new ArrayList<>();
        for (Room_Records room : rooms) {
            Map<String, Object> roomMap = new HashMap<>();
            roomMap.put("roomID", room.getRoomID());
            roomMap.put("roomName", room.getRoomName());
            roomMap.put("capacity", room.getRoomCapacity());

            List<Booking_Records> bookings = booking_Repositry.findByroomID(room.getRoomID());
            List<Map<String, Object>> booked = new ArrayList<>();
            bookings.forEach(booking -> {
                Map<String, Object> bookingMap = new HashMap<>();
                bookingMap.put("bookingID", booking.getBookingID());
                bookingMap.put("dateOfBooking", booking.getDateOfBooking());
                bookingMap.put("timeFrom", booking.getTimeFrom());
                bookingMap.put("timeTo", booking.getTimeTo());
                bookingMap.put("purpose", booking.getPurpose());
                bookingMap.put("userID", booking.getUserID());
                booked.add(bookingMap);
            });
            roomMap.put("booked", booked);
            response.add(roomMap);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?>add_room(/*@RequestBody*/ Room_Records room/*String roomName, Integer roomCapacity*/)
    {
        if(room_repositry.existsByroomName(room.getRoomName()/*roomName*/))
        {
            //Error error=new Error("Room already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Room already exists");
            /*return ResponseEntity.status(HttpStatus.CONFLICT).body(error);*/
        }

        if(/*roomCapacity*/room.getRoomCapacity()<=0)
        {
//            Error error=new Error("Invalid capacity");
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
              return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid capacity");
        }


        else
        {
            Room_Records room_obj=new Room_Records(room.getRoomName()/*roomName*/, room.getRoomCapacity()/*roomCapacity*/);
            Room_Records savedAccount =room_repositry.save(room_obj);
            room_repositry.save(room_obj);
            return new ResponseEntity<>("Room created successfully", HttpStatus.OK);
        }
    }

    public ResponseEntity<?> update_room(  Room_Records room )
    {
        if(!room_repositry.existsByroomID(room.getRoomID()))
        {
//            Error error=new Error("Room does not exist");
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
              return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Room does not exist");
        }
        if(room_repositry.findRoomCapacityByRoomName(room.getRoomName()) == room.getRoomCapacity() && room_repositry.existsByroomName(room.getRoomName()))
        {
//            Error error=new Error("Room already exists");
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
              return ResponseEntity.status(HttpStatus.CONFLICT).body("Room already exists with given capacity");

        }
        if(room.getRoomCapacity()<=0)
        {
//            Error error=new Error("Invalid capacity");
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Invalid capacity");
        }
        Room_Records room_obj=new Room_Records(room.getRoomID(), room.getRoomName()/*roomName*/, room.getRoomCapacity()/*roomCapacity*/);
        Room_Records savedAccount =room_repositry.save(room_obj);
        room_repositry.save(room_obj);
        return new ResponseEntity<>("Room updated successfully", HttpStatus.OK);

    }

}
