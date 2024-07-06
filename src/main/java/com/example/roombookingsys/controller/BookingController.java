package com.example.roombookingsys.controller;
import com.example.roombookingsys.model.Booking_Records;
import com.example.roombookingsys.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/book")
public class BookingController
{
    @Autowired
    private BookingService bookingService;

    @GetMapping("/booking")
    public String book()
    {
        return "booking";
    }
    @PostMapping("/books")
    public ResponseEntity<?> booking(@ModelAttribute Booking_Records bookingRecords)
    {
        //System.out.println(bookingRecords.getRoomID()+"roommn "+bookingRecords.getUserID()+" date"+bookingRecords.getDateOfBooking()+"timeto"+bookingRecords.getTimeTo());
        return bookingService.bookin(bookingRecords);
    }
    @GetMapping("/delete")
    public String delbookin()
    {
        return "deletebooking";
    }
    @GetMapping("/del")
    public ResponseEntity<?> deleteBooking(@RequestParam("bookingID") int bookingID)
    {
        // Call the service method to delete the booking
        return bookingService.deleteBooking(bookingID);
    }

    @GetMapping("/edit")
    public String editbookin()
    {
        return "editbooking";
    }
    @GetMapping("/editbooking")
    public ResponseEntity<?> editbooking(@ModelAttribute Booking_Records bookingRecords)
    {
        return bookingService.edit_booking(bookingRecords);
    }


}
