package su.givc.learningprojects.barcode_and_docx.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Класс DefaultController
 * <p/>
 * Контроллер по-умолчанию
 * <p/>
 *
 * @author mihailinpk
 * created 16.02.2022 17:47
 */
@RestController
public class DefaultController {

    /**
     * Строка-приветствие
     */
    @Value("${application.greeting}")
    String greetingString;

    /**
     * Обработка запроса по-умолчанию
     *
     * @return строка-приветствие
     */
    @GetMapping("/")
    public ResponseEntity<String> greeting() {
        return new ResponseEntity<>(greetingString, HttpStatus.OK);
    }

}
