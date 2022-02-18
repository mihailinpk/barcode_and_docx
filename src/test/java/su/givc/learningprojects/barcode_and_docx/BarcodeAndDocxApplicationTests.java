package su.givc.learningprojects.barcode_and_docx;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import su.givc.learningprojects.barcode_and_docx.service.DocumentsGenerateService;

@SpringBootTest
class BarcodeAndDocxApplicationTests {

    DocumentsGenerateService documentsGenerateService;

    @Autowired
    public BarcodeAndDocxApplicationTests(DocumentsGenerateService documentsGenerateService) {
        this.documentsGenerateService = documentsGenerateService;
    }

    @Test
    void contextLoads() {
    }

    @Test
    void generateDocxDocument() {
        String text = "Меченый злом, в сердце пустом, слышишь меня, я иду за тобой !";
        try {
            documentsGenerateService.generateWordDocument(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
