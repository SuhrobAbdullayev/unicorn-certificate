package com.uni.uni.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.uni.uni.dto.StudentReceiverDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.util.Hashtable;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CertificateService {

    public String generateCertificate(StudentReceiverDto dto) throws IOException, DocumentException, WriterException {
        ClassPathResource resource = new ClassPathResource("Certificate.pdf");
        PdfReader reader = new PdfReader(resource.getInputStream());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfStamper stamper = new PdfStamper(reader, baos);
        BaseFont font = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.EMBEDDED);
        PdfContentByte canvas = stamper.getOverContent(1);

        String fullName = dto.firstName() + " " + dto.lastName();
        Rectangle pageSize = reader.getPageSize(1);
        float centerX = pageSize.getWidth() / 2;
        canvas.beginText();
        canvas.setFontAndSize(font, 30);
        canvas.showTextAligned(Element.ALIGN_CENTER, fullName, centerX, 330, 0); // Y = 400 stays fixed
        canvas.endText();


        // 2. Add current date bottom center
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        canvas.beginText();
        canvas.setFontAndSize(font, 14);
        canvas.showTextAligned(Element.ALIGN_CENTER, date, 452, 40, 0);
        canvas.endText();

        // 3. Generate QR code
        String qrCode = generateRandomCode(5);
        BufferedImage qrImage = generateQRCodeImage(qrCode, 110, 110);
        Image qr = convertBufferedImageToItextImage(qrImage);
        qr.setAbsolutePosition(650, 58);
        qr.scaleToFit(110, 110);
        stamper.getOverContent(1).addImage(qr);

        // 4. Finalize PDF
        stamper.close();
        reader.close();

        // 5. Save to file system
        File outputDir = new File("D:/sertificate/");
        if (!outputDir.exists()) outputDir.mkdirs();

        String filePath = "D:/sertificate/" + qrCode + ".pdf";
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(baos.toByteArray());
        }

        return filePath; // Or return qrCode if you prefer
    }

    private BufferedImage generateQRCodeImage(String text, int width, int height) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.MARGIN, 0); // ✅ Remove white border
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H); // optional: better QR resilience

        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }


    private String generateRandomCode(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(rand.nextInt(chars.length())));
        }
        return sb.toString();
    }


    private Image convertBufferedImageToItextImage(BufferedImage bufferedImage) throws IOException, BadElementException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        javax.imageio.ImageIO.write(bufferedImage, "png", baos);
        return Image.getInstance(baos.toByteArray());
    }
}
