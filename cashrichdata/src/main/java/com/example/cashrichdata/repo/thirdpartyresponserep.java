package com.example.cashrichdata.repo;

import com.example.cashrichdata.dto.thirdpartyResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface thirdpartyresponserep extends JpaRepository<thirdpartyResponse,Long> {
}
