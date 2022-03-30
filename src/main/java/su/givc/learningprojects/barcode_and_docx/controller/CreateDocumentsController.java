package su.givc.learningprojects.barcode_and_docx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import su.givc.learningprojects.barcode_and_docx.service.DocumentsGenerateService;

/**
 * Класс CreateDocumentsController
 * <p/>
 * Обработка запросов по созданию документов
 * <p/>
 *
 * @author mihailinpk
 * created 23.02.2022 18:17
 */
@RestController
@RequestMapping("/createdocuments")
public class CreateDocumentsController {

    /**
     * Сервис генерации документов
     */
    DocumentsGenerateService documentsGenerateService;

    /**
     * Имя генерируемого *.docx файла
     */
    @Value("${application.docx.filename}")
    private String docxFileName;

    /**
     * Имя генерируемого *.xlsx файла
     */
    @Value("${application.xlsx.filename}")
    private String xlsxFileName;

    /**
     * Конструктор, выполняет "dependency injection" сервиса генерации документов
     *
     * @param documentsGenerateService сервис генерации документов
     */
    @Autowired
    public CreateDocumentsController(DocumentsGenerateService documentsGenerateService) {
        this.documentsGenerateService = documentsGenerateService;
    }

    /**
     * Обработка POST-запроса {@code /docx}
     * возвращает сгенерированный *.docx-документ с qr-кодом в конце
     *
     * @param text текст для вставки в документ и генерации qr-кода
     * @return сгенерированный *.docx-документ с qr-кодом в конце
     * @throws Exception возможное исключение
     */
    @PostMapping(value = "/docx")
    public ResponseEntity<byte[]> generateDocsFile(@RequestBody String text) throws Exception {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .header("Content-Disposition", String.format("attachment; filename=\"%s\"", docxFileName))
                .body(documentsGenerateService.generateWordDocument(text));
    }

    /**
     * Обработка POST-запроса {@code /xlsx}
     * возвращает сгенерированный *.xlsx-документ с Code128-кодом помещенным в нижний правый край листа
     *
     * @param text текст для вставки в документ и генерации Code128-кода
     * @return *.xlsx-документ с Code128-кодом помещенным в нижний правый край листа
     */
    @PostMapping(value = "/xlsx")
    public ResponseEntity<byte[]> generateExcelFile(@RequestBody String text) throws Exception {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .header("Content-Disposition", String.format("attachment; filename=\"%s\"", xlsxFileName))
                .body(documentsGenerateService.generateExcelDocument(text));
    }

}