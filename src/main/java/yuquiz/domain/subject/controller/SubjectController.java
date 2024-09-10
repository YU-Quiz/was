package yuquiz.domain.subject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yuquiz.domain.subject.service.SubjectService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/subject")
public class SubjectController {
    private final SubjectService subjectService;

    @GetMapping()
    public ResponseEntity<?> getSubjectByKeyword(
            @RequestParam(value = "keyword", defaultValue = "") String keyword) {

        return ResponseEntity.status(HttpStatus.OK).body(subjectService.getSubjectByKeyword(keyword));
    }

}
