package com.bsep.bezbednosttim32.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;

public class QRCodeGenerator {

    public static byte[] generateQRCode(String secret, String username, String issuer) throws Exception {
        String qrCodeText = String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s", issuer, username, secret, issuer);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, 200, 200);
        // Pretvoriti BitMatrix u bajtove za sliku i vratiti
        return toByteArray(bitMatrix);
    }

    private static byte[] toByteArray(BitMatrix bitMatrix) {
        // Implementacija konverzije BitMatrix u byteArray
        return new byte[0];
    }
}
