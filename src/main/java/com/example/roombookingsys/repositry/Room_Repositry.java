package com.example.roombookingsys.repositry;

import com.example.roombookingsys.model.Room_Records;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;
import java.util.Optional;

@Repository
public interface Room_Repositry extends JpaRepository<Room_Records,Integer>
{
    //int findroomIDByuserID(int userid);
    //String findroomNameByroomID(int roomID);
    Room_Records findByroomID(int roomID);
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Room_Records r WHERE r.roomID = :roomID")
    boolean existsByroomID(int roomID);
    @Transactional
    void deleteByroomID(int roomID);
    //String findroomNamebyuserID(int userID);
    List<Room_Records>findByroomCapacityGreaterThanEqual(int roomCapacity);
    //Room_Records findByuserID(int userID);
    boolean existsByroomName(String name);
    @Query("SELECT r.roomCapacity FROM Room_Records r WHERE r.roomName = ?1")
    Integer findRoomCapacityByRoomName(String roomName);

//    boolean existsByRoomNameAndRoomIDNot(String name, int roomID);
//    Room_Records findByroomName(String roomName);
}
