package com.example.roombookingsys.repositry;

import com.example.roombookingsys.DTO.UserDetails;
import com.example.roombookingsys.model.Account;
import com.example.roombookingsys.model.Booking_Records;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Accounts_Repositry extends JpaRepository<Account,Integer>
{
    boolean existsByEmail(String email);
    boolean existsByUserID(int userid);
    Optional<Account> findByUserID(int userid);

    @Query("SELECT a.password FROM Account a WHERE a.email = :email")
    Optional<String> findPasswordByEmail(String email);

    @Query("SELECT a.name AS name, a.userID AS userID, a.email AS emailId FROM Account a")
    List<UserDetails> findAllWithoutPassword();

}
