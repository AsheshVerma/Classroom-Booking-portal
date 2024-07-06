package com.example.roombookingsys.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import com.example.roombookingsys.model.Room_Records;
import com.example.roombookingsys.service.Room_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/rooms")
public class RoomController
{
    @Autowired
    private Room_Service roomService;

    @GetMapping("/delete")
    public String delroom()
    {
        return "r";
    }
    @GetMapping("/deleteroom")
    public ResponseEntity<?> deleteroom ( @RequestParam("roomID") int roomID)
    {
        return  roomService.delete_room( roomID);
    }


    @GetMapping ("/get")
    public String getroom()
    {
        return "getroomcap";
    }
    @GetMapping("/getrooms")
    public /*ResponseEntity<?>*/String getRooms(@RequestParam(value = "roomCapacity", required = false) Integer roomCapacity, Model model)
    {
        //return roomService.get_rooms(roomCapacity);
        ResponseEntity<?> response = roomService.get_rooms(roomCapacity);

        if (response == null || response.getBody() == null) {
            //model.addAttribute("message", "No rooms found");
            return "Noroomavailable";
        } else {
            if (response.getStatusCode() == HttpStatus.OK) {
                List<Map<String, Object>> rooms = (List<Map<String, Object>>) response.getBody();
                model.addAttribute("rooms", rooms);
            } else {
                model.addAttribute("message", response.getBody());
            }
        }

        return "RoomList";
    }


    @GetMapping ("/addroom")
    public String add()
    {
        return "addroom";
    }
    @PostMapping( value = "/add",consumes="application/x-www-form-urlencoded")//else it will give http status 415 un macthed type
    public ResponseEntity<?> addroom(@ModelAttribute Room_Records room/*@RequestParam String roomName, @RequestParam int roomCapacity*/)
    {
        return roomService.add_room(room);
    }

    @GetMapping("/updateroom")
    public String update()
    {
        return "updateroom";
    }
    @GetMapping("/update")
    public  ResponseEntity<?>updateroom( @ModelAttribute Room_Records room)
    {
        return  roomService.update_room(room);
    }

}
