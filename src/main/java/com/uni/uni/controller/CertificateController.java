package com.uni.uni.controller;

import com.google.zxing.WriterException;
import com.itextpdf.text.DocumentException;
import com.uni.uni.dto.StudentReceiverDto;
import com.uni.uni.entity.Certificate;
import com.uni.uni.service.CertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/certificate")
@RequiredArgsConstructor
public class CertificateController {

    private final CertificateService certificateService;

    @PostMapping("/create")
    public ResponseEntity<String> generateSertificate(@RequestBody StudentReceiverDto dto) throws DocumentException, IOException, WriterException {
        return ResponseEntity.accepted().body(certificateService.generateCertificate(dto));
    }

    @GetMapping("/{qr}")
    public ResponseEntity<Certificate> getOne(@PathVariable String qr){
        return ResponseEntity.status(HttpStatus.FOUND).body(certificateService.getOne(qr));
    }


}
