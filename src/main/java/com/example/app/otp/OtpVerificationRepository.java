package com.example.app.otp;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
public interface OtpVerificationRepository extends JpaRepository<OtpVerification,Long>{
    Optional<OtpVerification> findTopByEmailOrderByIdDesc(String email);
    @Transactional
    @Modifying
    @Query("update OtpVerification o set o.used =true where o.email=:email")
    int markAllUsedByEmail(@Param("email") String email);
}

