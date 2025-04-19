package com.uni.uni.controller;

import com.google.zxing.WriterException;
import com.itextpdf.text.DocumentException;
import com.uni.uni.dto.StudentReceiverDto;
import com.uni.uni.entity.Certificate;
import com.uni.uni.service.CertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/certificate")
@RequiredArgsConstructor
public class CertificateController {

    private final CertificateService certificateService;

    @PostMapping("/create")
    public ResponseEntity<Certificate> generateSertificate(@RequestBody StudentReceiverDto dto) throws DocumentException, IOException, WriterException {
        return ResponseEntity.accepted().body(certificateService.generateCertificate(dto));
    }

    @GetMapping("/{key}")
    public ResponseEntity<InputStreamResource> downloadResource(@PathVariable String key) {
        try {
            InputStream inputStream = certificateService.downloadFile(key);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + key)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(inputStream));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }


}
