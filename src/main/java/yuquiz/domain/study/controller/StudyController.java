package yuquiz.domain.study.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yuquiz.common.api.SuccessRes;
import yuquiz.domain.study.dto.StudyReq;
import yuquiz.domain.study.service.StudyService;
import yuquiz.security.auth.SecurityUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/study")
public class StudyController {
    private final StudyService studyService;

    @PostMapping
    public ResponseEntity<?> createStudy(@Valid @RequestBody StudyReq studyReq,
                                         @AuthenticationPrincipal SecurityUserDetails userDetails) {

        studyService.createStudy(studyReq, userDetails.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(SuccessRes.from("성공적으로 생성되었습니다."));
    }
}
