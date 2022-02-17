package su.givc.learningprojects.barcode_and_docx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import su.givc.learningprojects.barcode_and_docx.service.BarcodeGenerateService;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;

/**
 * Класс BarcodesController
 * <p/>
 * Обработка запросов по формированию штрих-кодов
 * <p/>
 *
 * @author mihailinpk
 * created 16.02.2022 18:02
 */
@RestController
@RequestMapping("/barcodes")
public class BarcodesController {

    /**
     * Сервис генерации штрих-кодов
     */
    BarcodeGenerateService barcodeGenerateService;

    /**
     * Конструктор, выполняет "dependency injection" сервиса генерации штрих-кодов
     *
     * @param barcodeGenerateService сервис генерации штрих-кодов
     */
    @Autowired
    public BarcodesController(BarcodeGenerateService barcodeGenerateService) {
        this.barcodeGenerateService = barcodeGenerateService;
    }

    /**
     * Обработка POST-запроса {@code /ean13/текст_для_генерации_штрих_кода}
     * возвращает сгенерированный штрих-код в формате EAN13 в виде картинки PNG
     *
     * @param barcode текст для генерации штрих-кода
     * @return сгенерированный штрих-код
     */
    @PostMapping(value = "/ean13")
    public ResponseEntity<byte[]> createEAN13barcode(@RequestBody String barcode)    {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(barcodeGenerateService.generateEAN13barcodeImage(barcode), "png", byteArrayOutputStream);
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Content-Type", MediaType.IMAGE_PNG_VALUE)
                    .body(byteArrayOutputStream.toByteArray());
        } catch (Exception ex)   {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Обработка POST-запроса {@code /qrcode}
     * возращает сгенерированный QR-код в виде картинки PNG
     *
     * @param barcode текст для генерации QR-кода
     * @return сгенерированный QR-код
     */
    @PostMapping(value = "/qrcode")
    public ResponseEntity<byte[]> createQRcode(@RequestBody String barcode)   {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(barcodeGenerateService.generateQRcodeImage(barcode), "png", byteArrayOutputStream);
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Content-Type", MediaType.IMAGE_PNG_VALUE)
                    .body(byteArrayOutputStream.toByteArray());
        } catch (Exception ex)   {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
