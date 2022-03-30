package su.givc.learningprojects.barcode_and_docx.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.Units;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
        XWPFDocument document = createWordDocument(text);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        document.write(byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        document.close();
        byteArrayOutputStream.close();
        return byteArray;
    }

    /**
     * Генерация *.xlsx-документа, вставка в него переданного в параметре текста и генерация и вставка в конец
     * документа Code128-баркода, генерируемого из переданного текста
     *
     * @param text текст для вставки в сгенерированный *.docx-документ и для генерации QR-кода для вставки в конец
     *             этого же документа
     * @return сгенерированный документ в виде массива байт
     * @throws Exception возможное исключение
     */
    public byte[] generateExcelDocument(String text) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        Row firstRow = sheet.createRow(0);
        Cell firstCell = firstRow.createCell(0);
        firstCell.setCellValue(text);

        BufferedImage code128image = barcodeGenerateService.generateCode128barcodeImage(text);
        ByteArrayOutputStream imageByteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(code128image, "jpg", imageByteArrayOutputStream);
        byte[] imageByteArray = imageByteArrayOutputStream.toByteArray();

        int code128id = workbook.addPicture(imageByteArray, Workbook.PICTURE_TYPE_JPEG);
        XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
        XSSFClientAnchor clientAnchor= new XSSFClientAnchor();
        clientAnchor.setCol1(0);
        clientAnchor.setCol2(3);
        clientAnchor.setRow1(2);
        clientAnchor.setRow2(4);
        drawing.createPicture(clientAnchor, code128id);
        sheet.autoSizeColumn(0);

        ByteArrayOutputStream bookByteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(bookByteArrayOutputStream);
        byte[] bookByteArray = bookByteArrayOutputStream.toByteArray();
        workbook.close();
        imageByteArrayOutputStream.close();
        bookByteArrayOutputStream.close();
        return bookByteArray;
    }

    /**
     * Создать *.docx-документ со сгенерированным и вставленным в конце документа qr-кодом
     *
     * @param text текст
     * @return сгенерированный *.docx-документ
     * @throws Exception возможное исключение
     */
    private XWPFDocument createWordDocument(String text) throws Exception {
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
        byteArrayInputStream.close();
        byteArrayOutputStream.close();
        return document;
    }

}
