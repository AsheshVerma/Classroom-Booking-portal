package com.example.roombookingsys.service;

import com.example.roombookingsys.Error.Error;
import com.example.roombookingsys.model.Booking_Records;
import com.example.roombookingsys.repositry.Accounts_Repositry;
import com.example.roombookingsys.repositry.Booking_Repositry;
import com.example.roombookingsys.repositry.Room_Repositry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class BookingService
{
    @Autowired
    private Room_Repositry room_repositry;
    @Autowired
    private Accounts_Repositry accounts_repositry;
    @Autowired
    private Booking_Repositry booking_repositry;


    public ResponseEntity<?> bookin( Booking_Records bookingRecords)
    {
        if(!accounts_repositry.existsByUserID(bookingRecords.getUserID()))//no user in user table
        {
//            Error Error=new Error("User does not exist");
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Error);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body( "User does not exist");
        }
        //System.out.println(+bookingRecords.getUserID());
        if (!room_repositry.existsByroomID(bookingRecords.getRoomID()))//if no room in room table
        {
//            Error Error=new Error("Room does not exist");
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Error);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body( "Room does not exist");
        }

        // Check room availability
        int count = booking_repositry.countByroomID(bookingRecords.getRoomID());
        if (count == 0)
        {
//            LocalDate today = LocalDate.now();
//            LocalDate bookDate = bookingRecords.getDateOfBooking().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            Date bookdate=null;
            // Room is available, but we dk about time date
            String pattern = "yyyy-MM-dd";
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            try {
                // Parse the string to a Date object
                bookdate= dateFormat.parse(bookingRecords.getDateOfBooking());

            } catch (ParseException e) {
                // Handle parsing exception
                e.printStackTrace();
            }


            LocalDate today = LocalDate.now();
            LocalDate bookDate = /*bookingRecords.getDateOfBooking()*/bookdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if(today.isBefore(bookDate) || today.isEqual(bookDate))
            {
                LocalTime currentTime = LocalTime.now();
                LocalTime bookingTimeFrom = LocalTime.parse(bookingRecords.getTimeFrom());
                LocalTime bookingTimeTo = LocalTime.parse(bookingRecords.getTimeTo());
                if (today.isBefore(bookDate) || bookingTimeFrom.isAfter(currentTime))
                {
                    Booking_Records booking = new Booking_Records();
                    booking.setUserID(bookingRecords.getUserID());
                    booking.setRoomID(bookingRecords.getRoomID());
                    booking.setDateOfBooking(bookingRecords.getDateOfBooking());
                    booking.setTimeFrom(bookingRecords.getTimeFrom());
                    booking.setTimeTo(bookingRecords.getTimeTo());
                    booking.setPurpose(bookingRecords.getPurpose());
                    booking_repositry.save(booking);
                    return new ResponseEntity<>("Booking created successfully", HttpStatus.OK);
                }
                else
                {
//                    Error error = new Error("Invalid date/time");
//                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body( "Invalid date/time");

                }

            }
            else
            {
//                Error Error=new Error("Invalid date/time");
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Error);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body( "Invalid date/time");
            }
        }
        else
        {
            // Check for overlapping bookings
            int overlappingBookings = booking_repositry.countOverlappingBookings(bookingRecords.getRoomID(), bookingRecords.getDateOfBooking(), bookingRecords.getTimeFrom(), bookingRecords.getTimeTo());
            if (overlappingBookings > 0)
            {
                // Room is unavailable due to overlapping bookings
//                Error Error=new Error("Room unavailable");
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Error);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body( "Room unavailable");

            }
            else
            {
                Date bookdate=null;
                // Room is available, but we dk about time date
                String pattern = "yyyy-MM-dd";
                SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
                try {
                    // Parse the string to a Date object
                     bookdate= dateFormat.parse(bookingRecords.getDateOfBooking());

                } catch (ParseException e) {
                    // Handle parsing exception
                    e.printStackTrace();
                }


                LocalDate today = LocalDate.now();
                LocalDate bookDate = /*bookingRecords.getDateOfBooking()*/bookdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                if (today.isBefore(bookDate) || today.isEqual(bookDate))
                {
                    LocalTime currentTime = LocalTime.now();
                    LocalTime bookingTimeFrom = LocalTime.parse(bookingRecords.getTimeFrom());
                    LocalTime bookingTimeTo = LocalTime.parse(bookingRecords.getTimeTo());
                    if (today.isBefore(bookDate) || bookingTimeFrom.isAfter(currentTime))
                    {
                        Booking_Records booking = new Booking_Records();
                        booking.setUserID(bookingRecords.getUserID());
                        booking.setRoomID(bookingRecords.getRoomID());
                        booking.setDateOfBooking(bookingRecords.getDateOfBooking());
                        booking.setTimeFrom(bookingRecords.getTimeFrom());
                        booking.setTimeTo(bookingRecords.getTimeTo());
                        booking.setPurpose(bookingRecords.getPurpose());
                        booking_repositry.save(booking);
                        return new ResponseEntity<>("Booking created successfully", HttpStatus.OK);
                    }
                    else
                    {
//                        Error error = new Error("Invalid date/time");
//                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body( "Invalid date/time");
                    }
                }
                else
                {
//                    Error Error=new Error("Invalid date/time");
//                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Error);
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body( "Invalid date/time");
                }
            }
        }
    }

    public ResponseEntity<?>deleteBooking(int BookingID)
    {
        if (booking_repositry.existsBybookingID(BookingID))
        {
            // If the booking exists, delete it
            booking_repositry.deleteByBookingId(BookingID);
            return new ResponseEntity<>("Booking deleted successfully", HttpStatus.OK);
        }
        else
        {
            // If the booking does not exist, return false
//            Error error=new Error("Booking does not exist");
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body( "Booking does not exist");
        }
    }

    public ResponseEntity<?> edit_booking(@RequestBody Booking_Records bookingRecords)
    {
        if(!accounts_repositry.existsByUserID(bookingRecords.getUserID()))//no user in user table
        {
//            Error Error=new Error("User does not exist");
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Error);
              return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User does not exist");
        }
        //System.out.println(+bookingRecords.getUserID());
        if (!room_repositry.existsByroomID(bookingRecords.getRoomID()))//if no room in room table
        {
//            Error Error=new Error("Room does not exist");
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Error);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Room does not exist");
        }

        if(!booking_repositry.existsBybookingID(bookingRecords.getBookingID()))//if no booking id for given bookin id in booking table
        {
//            System.out.println(bookingRecords.getBookingID());
//            Error Error=new Error("Booking does not exist");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Booking does not exist");
        }

        // Check room availability
        int count = booking_repositry.countByroomID(bookingRecords.getRoomID());
        if (count == 0)
        {
//            LocalDate today = LocalDate.now();
//            LocalDate bookDate = bookingRecords.getDateOfBooking().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            Date bookdate=null;
            // Room is available, but we dk about time date
            String pattern = "yyyy-MM-dd";
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            try {
                // Parse the string to a Date object
                bookdate= dateFormat.parse(bookingRecords.getDateOfBooking());

            } catch (ParseException e) {
                // Handle parsing exception
                e.printStackTrace();
            }
            LocalDate today = LocalDate.now();
            LocalDate bookDate = /*bookingRecords.getDateOfBooking()*/bookdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if(today.isBefore(bookDate) || today.isEqual(bookDate))
            {
                LocalTime currentTime = LocalTime.now();
                LocalTime bookingTimeFrom = LocalTime.parse(bookingRecords.getTimeFrom());
                LocalTime bookingTimeTo = LocalTime.parse(bookingRecords.getTimeTo());
                if (today.isBefore(bookDate) || bookingTimeFrom.isAfter(currentTime))
                {
                    Booking_Records booking = new Booking_Records();
                    booking.setUserID(bookingRecords.getUserID());
                    booking.setBookingID(bookingRecords.getBookingID());
                    booking.setRoomID(bookingRecords.getRoomID());
                    booking.setDateOfBooking(bookingRecords.getDateOfBooking());
                    booking.setTimeFrom(bookingRecords.getTimeFrom());
                    booking.setTimeTo(bookingRecords.getTimeTo());
                    booking.setPurpose(bookingRecords.getPurpose());
                    booking_repositry.save(booking);
                    return new ResponseEntity<>("Booking modified successfully", HttpStatus.OK);
                }
                else
                {
//                    Error error = new Error("Invalid date/time");
//                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body( "Invalid date/time");
                }
            }
            else
            {
//                Error Error=new Error("Invalid date/time");
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Error);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body( "Invalid date/time");
            }
        }
        else
        {
            // Check for overlapping bookings
            int overlappingBookings = booking_repositry.countOverlappingBookings(bookingRecords.getRoomID(), bookingRecords.getDateOfBooking(), bookingRecords.getTimeFrom(), bookingRecords.getTimeTo());
            if (overlappingBookings > 0)
            {
                // Room is unavailable due to overlapping bookings
//                Error Error=new Error("Room unavailable");
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Error);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body( "Room unavailable");

            }
            else
            {
                // Room is available, but we dk about time date
//                LocalDate today = LocalDate.now();
//                LocalDate bookDate = bookingRecords.getDateOfBooking().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                Date bookdate=null;

                String pattern = "yyyy-MM-dd";
                SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
                try {
                    // Parse the string to a Date object
                    bookdate= dateFormat.parse(bookingRecords.getDateOfBooking());

                } catch (ParseException e) {
                    // Handle parsing exception
                    e.printStackTrace();
                }
                LocalDate today = LocalDate.now();
                LocalDate bookDate = /*bookingRecords.getDateOfBooking()*/bookdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                if (today.isBefore(bookDate) || today.isEqual(bookDate))
                {
                    LocalTime currentTime = LocalTime.now();
                    LocalTime bookingTimeFrom = LocalTime.parse(bookingRecords.getTimeFrom());
                    LocalTime bookingTimeTo = LocalTime.parse(bookingRecords.getTimeTo());
                    if (today.isBefore(bookDate) || bookingTimeFrom.isAfter(currentTime))
                    {
                        Booking_Records booking = new Booking_Records();
                        booking.setUserID(bookingRecords.getUserID());
                        booking.setBookingID(bookingRecords.getBookingID());
                        booking.setRoomID(bookingRecords.getRoomID());
                        booking.setDateOfBooking(bookingRecords.getDateOfBooking());
                        booking.setTimeFrom(bookingRecords.getTimeFrom());
                        booking.setTimeTo(bookingRecords.getTimeTo());
                        booking.setPurpose(bookingRecords.getPurpose());
                        booking_repositry.save(booking);
                        return new ResponseEntity<>("Booking modified successfully", HttpStatus.OK);
                    }
                    else
                    {
//                        Error error = new Error("Invalid date/time");
//                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body( "Invalid date/time");

                    }
                }
                else
                {
//                    Error Error=new Error("Invalid date/time");
//                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Error);
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body( "Invalid date/time");

                }
            }
        }
    }
}

