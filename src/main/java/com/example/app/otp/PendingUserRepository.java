package com.example.app.otp;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
public interface PendingUserRepository extends JpaRepository<PendingUser, Long>{
    Optional<PendingUser> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    @Modifying
    @Transactional
    void deleteByEmail(String email);
    @Modifying
    @Transactional
    void deleteByPhoneNumber(String phoneNumber);
}