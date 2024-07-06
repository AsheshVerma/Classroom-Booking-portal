package com.example.roombookingsys.controller;

//import ch.qos.logback.core.model.Model;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.example.roombookingsys.DTO.UserDetails;
import com.example.roombookingsys.model.Account;
import com.example.roombookingsys.service.Account_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;


import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Controller
public class AccountController
{

    @Autowired
    private Account_Service accountService;

    @GetMapping()
    public String homepage()
    {
        return "home";
    }

    @GetMapping("/authenticate")
    public String login() {
        return "login";
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(/*@RequestBody*/ @ModelAttribute Account accounts)
    {
        return accountService.log_in(accounts.getEmail(),accounts.getPassword());
    }

    @GetMapping("/sign")
    public String signup() {
        return "AccountSignup";
    }
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(/*@RequestBody*/ @ModelAttribute Account account)
    {
        return accountService.sign_up(account.getEmail(), account.getName(), account.getPassword());
    }

    @GetMapping("/userid")
    public String userid() {
        return "userid";
    }

    @GetMapping("/user")
    public String userDetail(@RequestParam("userID") int userID, Model model)
    {
        ResponseEntity<?> responseEntity = accountService.user_detail(userID);
        if (responseEntity == null || responseEntity.getBody() == null)
        {
            model.addAttribute("message", "User does not exist");
        }
        else
        {
            Object userDetails = responseEntity.getBody();
            try
            {
                if (userDetails != null)
                {
                    model.addAttribute("userID", userDetails.getClass().getMethod("getUserID").invoke(userDetails));
                    model.addAttribute("name", userDetails.getClass().getMethod("getName").invoke(userDetails));
                    model.addAttribute("email", userDetails.getClass().getMethod("getEmail").invoke(userDetails));
                }
                else
                {
                    model.addAttribute("message", "User does not exist");
                }
            }
            catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e)
            {
                model.addAttribute("message", "User does not exist");
            }

        }
        return "AccountUserExists";
    }

    @GetMapping("/history")
    public String history() {
        return "bookinghistory";
    }
    @GetMapping("/bookinghistory")
    public /*ResponseEntity<?>*/String RoomBookingHistory(@ModelAttribute/*@RequestParam*/("userID") int userID,Model model)
    {
        LocalDate currentDate1 = LocalDate.now();
        Date currentDate = Date.valueOf(currentDate1);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentDate);
        LocalTime currentTime1 = LocalTime.now();
        String currentTime = currentTime1.toString();

        ResponseEntity<?> response = accountService.getUserHistory(userID, dateString, currentTime);

        if (response.getStatusCode() == HttpStatus.OK) {
            model.addAttribute("history", response.getBody());
        } else {
            model.addAttribute("message", response.getBody());
        }

        return "bookinhistory";

    }

    @GetMapping("/mybooking")
    public String upcominbooking() {
        return "upcomingbooking";
    }
    @GetMapping("/upcoming")
    public /*ResponseEntity<?>*/String RoomBookingUpcoming(/*@RequestParam*/@ModelAttribute("userID") int userID,Model model)
    {
        LocalDate currentDate1 = LocalDate.now();
        Date currentDate= Date.valueOf(currentDate1);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentDate);
        LocalTime currentTime1 = LocalTime.now();
        String  currentTime= currentTime1.toString();
        ResponseEntity<?>response= accountService.getUserUpcoming(userID, dateString, currentTime);
        if (response.getStatusCode() == HttpStatus.OK) {
            model.addAttribute("upcoming", response.getBody());
        } else {
            model.addAttribute("message", response.getBody());
        }
        return "upcomingbookin";
    }
    @GetMapping("/users")
    public /*ResponseEntity<List<UserDetails>>*/ String  allUsers(/**/ Model model)
    {
        //return accountService.allUsers();

        ResponseEntity<List<UserDetails>> responseEntity = accountService.allUsers();
        if (responseEntity == null || responseEntity.getBody().isEmpty())
        {
            model.addAttribute("message", "No Users Found");
        }
        else
        {
            List<UserDetails> users = responseEntity.getBody();
            model.addAttribute("users", users);
        }
        return "AccountAllusers";
    }
}
