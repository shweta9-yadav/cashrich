package com.example.cashrichdata.repo;

import com.example.cashrichdata.dto.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface UserAuthRepo extends JpaRepository<UserAuth,String> {
    UserAuth findByToken(String token);

    @Modifying
    @Query("UPDATE UserAuth auth SET auth.expiredTime = :expiredTime WHERE auth.token = :token")
    void updateExpiredDateByTokenId(String token, Date expiredTime);
}
