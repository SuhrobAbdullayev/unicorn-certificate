package com.uni.uni.repository;

import com.uni.uni.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    Certificate findByQrId(String qrId);
}