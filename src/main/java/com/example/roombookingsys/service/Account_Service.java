package com.example.roombookingsys.service;

import com.example.roombookingsys.DTO.AccountsUserDetailDTO;
import com.example.roombookingsys.DTO.UserDetails;
import com.example.roombookingsys.Error.Error;
import com.example.roombookingsys.model.Account;
//import com.example.roombookingsys.model.Booking_Records;
import com.example.roombookingsys.model.Booking_Records;
import com.example.roombookingsys.model.Room_Records;
import com.example.roombookingsys.repositry.Accounts_Repositry;
import com.example.roombookingsys.repositry.Booking_Repositry;
import com.example.roombookingsys.repositry.Room_Repositry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class Account_Service
{
    @Autowired
    Accounts_Repositry accounts_repositry;
    @Autowired
    Booking_Repositry booking_repositry;
    @Autowired
    Room_Repositry room_repositry;

    public ResponseEntity<?> log_in(String email,String password)
    {
        if(accounts_repositry.existsByEmail(email))
        {
            Optional<String> optionalPassword = accounts_repositry.findPasswordByEmail(email);
            String actualPassword = optionalPassword.get();
            if(actualPassword.equals(password))
            {
                return ResponseEntity.status(HttpStatus.OK).body( "Login Successful");
            }
            else
            {
//                Error error=new Error("Username/Password Incorrect");
//                return ResponseEntity.ok(error);
                  return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body( "Username/Password Incorrect");
            }
        }
        else
        {
//            Error error=new Error("User does not exist");
//            return ResponseEntity.ok(error);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User does not exist");

        }
    }

    public ResponseEntity<?> sign_up(String email,String name,String password)
    {
        if(accounts_repositry.existsByEmail(email)==true)
        {
            /*Error error=new Error("Forbidden, Account already exists");
            return ResponseEntity.ok(error);*/
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden, Account already exists");
        }
        else
        {
            Account acc_obj=new Account(email,password,name);
            Account savedAccount =accounts_repositry.save(acc_obj);
            return ResponseEntity.status(HttpStatus.OK).body("Account Creation Successful");
        }
    }


    public ResponseEntity<?> user_detail(@RequestParam("userID")int userID)
    {
        Optional<Account> acc_obj = accounts_repositry.findByUserID(userID);
        if(acc_obj.isPresent())
        {
            Account account = acc_obj.get();
            AccountsUserDetailDTO accountDetailsDTO = new AccountsUserDetailDTO();
            accountDetailsDTO.setEmail(account.getEmail());
            accountDetailsDTO.setName(account.getName());
            accountDetailsDTO.setUserID(account.getUserid());
            return new ResponseEntity<>(accountDetailsDTO, HttpStatus.OK);
        }
        else
        {
            Error error=new Error("User does not exist");
            return ResponseEntity.ok(error);
        }
    }

    public ResponseEntity<?>getUserHistory(int userID,String currentDate, String currentTime)
    {
        if(!accounts_repositry.existsByUserID(userID))
        {
//            Error error=new Error("User does not exist");
//            return ResponseEntity.ok(error);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User does not exist");

        }
        List<Booking_Records>history=booking_repositry.getUserHistory(userID,currentDate,currentTime);
        List<Map<String,Object>> response=new ArrayList<>();

        for(Booking_Records booking:history)
        {
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date bookdate=null;

            String pattern = "yyyy-MM-dd";
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            try {
                // Parse the string to a Date object
                bookdate= dateFormat.parse(booking.getDateOfBooking());

            } catch (ParseException e) {
                // Handle parsing exception
                e.printStackTrace();
            }
            //LocalDate today = LocalDate.now();
            //LocalDate bookDate = /*bookingRecords.getDateOfBooking()*/bookdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            Map<String,Object> bookingMap=new HashMap<>();
            bookingMap.put("purpose",booking.getPurpose());
            bookingMap.put("roomID",booking.getRoomID());
            bookingMap.put("bookingID",booking.getBookingID());
            bookingMap.put("dateOfBooking",/*dateFormat.format(booking.getDateOfBooking())*/bookdate);
            bookingMap.put("timeFrom",booking.getTimeFrom());
            bookingMap.put("timeTo",booking.getTimeTo());

            bookingMap.put("roomName",room_repositry.findByroomID(booking.getRoomID()).getRoomName());
            response.add(bookingMap);
        }
        return ResponseEntity.ok(response);
    }


    public ResponseEntity<?> getUserUpcoming(/*@RequestParam*/ int userID, /*Date*/String currentDate, String currentTime)
    {
        Map<String,Object> mp=new HashMap<>();
        if(!accounts_repositry.existsByUserID(userID))
        {
//            Error error=new Error("User does not exist");
//            return ResponseEntity.ok(error);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User does not exist");

        }

        List<Booking_Records>history=booking_repositry.getUserUpcoming(userID,currentDate,currentTime);
        List<Map<String,Object>> response=new ArrayList<>();

        for(Booking_Records booking:history)
        {
            //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            //dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date bookdate=null;

            String pattern = "yyyy-MM-dd";
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            try {
                // Parse the string to a Date object
                bookdate= dateFormat.parse(booking.getDateOfBooking());

            } catch (ParseException e) {
                // Handle parsing exception
                e.printStackTrace();
            }
            Map<String,Object> bookingMap=new HashMap<>();
            bookingMap.put("purpose",booking.getPurpose());
            bookingMap.put("roomID",booking.getRoomID());
            bookingMap.put("bookingID",booking.getBookingID());
            bookingMap.put("dateOfBooking",/*dateFormat.format(booking.getDateOfBooking())*/bookdate);
            bookingMap.put("timeFrom",booking.getTimeFrom());
            bookingMap.put("timeTo",booking.getTimeTo());

            bookingMap.put("roomName",room_repositry.findByroomID(booking.getRoomID()).getRoomName());
            response.add(bookingMap);
        }
        return ResponseEntity.ok(response);
    }


    public ResponseEntity<List<UserDetails>> allUsers()
    {
        List<UserDetails> users = accounts_repositry.findAllWithoutPassword();
        return ResponseEntity.ok(users);
    }
}
