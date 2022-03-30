package su.givc.learningprojects.barcode_and_docx;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import su.givc.learningprojects.barcode_and_docx.service.BarcodeGenerateService;
import su.givc.learningprojects.barcode_and_docx.service.DocumentsGenerateService;

import javax.imageio.ImageIO;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootTest
class BarcodeAndDocxApplicationTests {

    private final DocumentsGenerateService documentsGenerateService;
    private final BarcodeGenerateService barcodeGenerateService;

    private final String folderName = "generated files by tests";


    @Autowired
    public BarcodeAndDocxApplicationTests(DocumentsGenerateService documentsGenerateService, BarcodeGenerateService barcodeGenerateService) {
        this.documentsGenerateService = documentsGenerateService;
        this.barcodeGenerateService = barcodeGenerateService;
    }

    @Test
    void contextLoads() {
    }

    /**
     * Проверка функционала генерации штрих-кода в формате Code128 <br/>
     * создаем в виде *.jpg-файла и сохраняем в каталог {@code /generated files by tests/}
     */
    @Test
    void generateCode128Image() {
        String text = "And his name that sat on him was Death, and Hell followed with him";
        try {
            ImageIO.write(
                    barcodeGenerateService.generateCode128barcodeImage(text),
                    "jpg",
                    new File(folderName + "/code128.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Проверка функционала генерации *.docx-файла со вставкой QR-кода <br/>
     * создаем и сохраняем в каталог {@code /generated files by tests/}
     */
    @Test
    void generateDocxDocument() {
        String text = "It's a long, long way to Tipperary,\nBut my heart's right there.";
        Path pathOfFile = Path.of(folderName + "/WordDocument.docx");
        try {
            Files.deleteIfExists(pathOfFile);
            Files.write(pathOfFile, documentsGenerateService.generateWordDocument(text));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Проверка функционала генерации *.xlsx-файла со вставкой штрих-кода в формате Code128 <br/>
     * создаем и сохраняем в каталог {@code /generated files by tests/}
     */
    @Test
    void generateXlxDocument() {
        String text = "His name that sate on him was Death, and hell followed with him.";
        Path pathOfFile = Path.of(folderName + "/ExcelDocument.xlsx");
        try {
            Files.deleteIfExists(pathOfFile);
            Files.write(pathOfFile, documentsGenerateService.generateExcelDocument(text));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
