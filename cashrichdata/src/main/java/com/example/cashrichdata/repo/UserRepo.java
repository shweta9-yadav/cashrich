package com.example.cashrichdata.repo;

import com.example.cashrichdata.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    User findByUserNameAndPassword(String email, String encPassword);

    @Query("SELECT u.email FROM User u WHERE u.email = :email")
    String findByEmail(@Param("email") String email);

    @Query("SELECT u.userName FROM User u WHERE u.userName = :userName")
    String findByUserName(String userName);
}

