package su.givc.learningprojects.barcode_and_docx.service;

import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Класс-компонент DocumentsGenerateService
 * <p/>
 * Сервис генерации документов
 * <p/>
 *
 * @author mihailinpk
 * created 18.02.2022 12:28
 */
@Component
public class DocumentsGenerateService {

    /**
     * Текст для заголовка документа
     */
    @Value("${application.docx.title.text}")
    private String titleDocxText;

//    /**
//     * Имя генерируемого файла
//     */
//    @Value("${application.docx.filename}")
//    private String fileName;

    /**
     * Генерация штрих-кодов
     */
    private final BarcodeGenerateService barcodeGenerateService;

    public DocumentsGenerateService(BarcodeGenerateService barcodeGenerateService) {
        this.barcodeGenerateService = barcodeGenerateService;
    }

    /**
     * Генерация *.docx-документа, вставка в него переданного в параметре текста и генерация и вставка в конец
     * документа QR-кода, генерируемого из переданного текста
     *
     * @param text текст для вставки в сгенерированный *.docx-документ и для генерации QR-кода для вставки в конец
     *             этого же документа
     * @return сгенерированный документ в виде массива байт
     * @throws Exception возможное исключение
     */
    public byte[] generateWordDocument(String text) throws Exception {

        XWPFDocument document = new XWPFDocument();
        XWPFParagraph titleParagraph = document.createParagraph();
        titleParagraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun titleRun = titleParagraph.createRun();
        titleRun.setText(titleDocxText);
        titleRun.setBold(true);
        titleRun.setFontFamily("Times New Roman");
        titleRun.setFontSize(20);

        XWPFParagraph mainTextParagraph = document.createParagraph();
        XWPFRun mainTextRun = mainTextParagraph.createRun();
        mainTextRun.setText(text);
        mainTextRun.setFontFamily("Times New Roman");
        mainTextRun.setTextPosition(20);
        mainTextRun.setFontSize(14);

        BufferedImage qrCode = barcodeGenerateService.generateQRcodeImage(text);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(qrCode, "png", byteArrayOutputStream);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        XWPFParagraph qrCodeParagraph = document.createParagraph();
        qrCodeParagraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun qrCodeRun = qrCodeParagraph.createRun();
        qrCodeRun.setTextPosition(20);
        qrCodeRun.addPicture(
                byteArrayInputStream,
                XWPFDocument.PICTURE_TYPE_PNG, "qr", Units.toEMU(100), Units.toEMU(100));

        document.write(byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        document.close();
        byteArrayOutputStream.close();
        return byteArray;

    }

}
