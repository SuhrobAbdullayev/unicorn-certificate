package com.uni.uni.controller;

import com.uni.uni.dto.ResponseDTO;
import com.uni.uni.exception.CertificateNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionController {
    @ExceptionHandler(CertificateNotFoundException.class)
    public ResponseEntity<Object> handleProductException(CertificateNotFoundException certificateNotFoundException) {
        String errorMessage = certificateNotFoundException.getMessage();
        certificateNotFoundException.printStackTrace();
        ResponseDTO<?> responseDTO = ResponseDTO.error(errorMessage, null);
        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
    }

}
