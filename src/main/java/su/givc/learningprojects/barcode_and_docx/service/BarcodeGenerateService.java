package su.givc.learningprojects.barcode_and_docx.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.EAN13Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;

/**
 * Класс-компонент BarcodeGenerateService
 * <p/>
 * Сервис генерации штрихкодов
 * <p/>
 *
 * @author mihailinpk
 * created 16.02.2022 18:18
 */
@Component
public class BarcodeGenerateService {

    /**
     * Сгенерировать штрихкод в формате EAN13 по предоставленному тексту
     *
     * @param barcodeText предоставленный текст
     * @return сгенерированный штрих-код
     */
    public BufferedImage generateEAN13barcodeImage(String barcodeText) {
        EAN13Writer barcodeWriter = new EAN13Writer();
        BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.EAN_13, 300, 150);

        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    /**
     * Сгенерировать QR-код по предоставленному тексту
     *
     * @param barcodeText предоставленный текст
     * @return сгененированный QR-код
     * @throws WriterException возможное исключение
     */
    public BufferedImage generateQRcodeImage(String barcodeText) throws WriterException {
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix =
                barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 200, 200);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

}