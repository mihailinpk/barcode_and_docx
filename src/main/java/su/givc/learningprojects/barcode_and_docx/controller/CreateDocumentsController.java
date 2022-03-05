package su.givc.learningprojects.barcode_and_docx.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
     * @param barcode текст для вставки в документ и генерации qr-кода
     * @return сгенерированный *.docx-документ с qr-кодом в конце
     * @throws Exception возможное исключение
     */
    @PostMapping(value = "/docx")
    public ResponseEntity<byte[]> generateDocsFile(@RequestBody String barcode) throws Exception {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .header("Content-Disposition", "attachment; filename=\"document.docx\"")
                .body(documentsGenerateService.generateWordDocument(barcode));
    }

}