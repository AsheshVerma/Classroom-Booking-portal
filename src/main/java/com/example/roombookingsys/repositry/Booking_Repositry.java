package com.example.roombookingsys.repositry;

import com.example.roombookingsys.model.Booking_Records;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository
public interface Booking_Repositry extends JpaRepository<Booking_Records,Integer> {

    int findBookingIdByuserID(int userID);
    boolean existsBybookingID(int bookingID);
    @Transactional
    void deleteByroomID(int roomID);

    @Query("SELECT b FROM Booking_Records b WHERE b.userID = :userID AND b.dateOfBooking < :currentDate OR (b.dateOfBooking = :currentDate AND  b.timeTo < :currentTime)")
    List<Booking_Records> getUserHistory(@Param("userID") int userID, @Param("currentDate") /*Date*/String currentDate, @Param("currentTime") String currentTime);


@Query("SELECT b FROM Booking_Records b WHERE b.userID = :userID AND b.dateOfBooking > :currentDate OR(b.dateOfBooking = :currentDate AND b.timeFrom > :currentTime) ")
List<Booking_Records>getUserUpcoming(@Param("userID") int userID, @Param("currentDate") /*Date*/String currentDate, @Param("currentTime") String currentTime);

    List<Booking_Records>findByroomID(int roomID);
//    Date findBookingDateByuserID(int userID);

    @Transactional
    @Modifying
    @Query("DELETE FROM Booking_Records e WHERE e.bookingID = ?1")
    void deleteByBookingId(int bookingID);

    @Query("SELECT COUNT(b) FROM Booking_Records b WHERE b.roomID = :roomID")
    int countByroomID(@Param("roomID") int roomID);

    @Query("SELECT COUNT(b) FROM Booking_Records b WHERE b.roomID = :roomID " +
            "AND b.dateOfBooking = :dateOfBooking " +
            "AND ((b.timeFrom <= :timeTo AND b.timeTo >= :timeFrom) OR " +
            "(b.timeFrom >= :timeFrom AND b.timeTo <= :timeTo))")
    int countOverlappingBookings(@Param("roomID") int roomID, @Param("dateOfBooking") /*Date*/String dateOfBooking, @Param("timeFrom") String timeFrom,@Param("timeTo") String timeTo);

}
