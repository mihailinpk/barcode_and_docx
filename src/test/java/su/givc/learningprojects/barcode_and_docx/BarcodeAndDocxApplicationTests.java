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
	void generateDocxDocument()	{
		String text = "It's a long way to Tipperary,\n" +
				"It's a long way to go;\n" +
				"It's a long way to Tipperary,\n" +
				"And the sweetest girl I know.\n" +
				"Good-bye, Piccadilly,\n" +
				"Farewell, Lester Square:\n" +
				"It's a long, long way to Tipperary,\n" +
				"But my heart's right there.";
		try {
			documentsGenerateService.generateWordDocument(text);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
