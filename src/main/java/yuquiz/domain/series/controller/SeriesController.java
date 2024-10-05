package yuquiz.domain.series.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import yuquiz.common.api.SuccessRes;
import yuquiz.domain.series.dto.SeriesReq;
import yuquiz.domain.series.dto.SeriesRes;
import yuquiz.domain.series.service.SeriesService;
import yuquiz.security.auth.SecurityUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/series")
public class SeriesController {

    private final SeriesService seriesService;

    @PostMapping
    public ResponseEntity<?> createSeries(@RequestBody @Valid SeriesReq seriesReq, @AuthenticationPrincipal SecurityUserDetails userDetails) {

        seriesService.createSeries(seriesReq, userDetails.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(SuccessRes.from("문제집이 생성되었습니다."));
    }

    @GetMapping("/{seriesId}")
    public ResponseEntity<?> findSeriesById(@PathVariable(value = "seriesId") Long seriesId, @AuthenticationPrincipal SecurityUserDetails userDetails) {

        SeriesRes seriesRes = seriesService.findSeriesById(seriesId, userDetails.getId());

        return ResponseEntity.status(HttpStatus.OK).body(seriesRes);
    }

    @DeleteMapping("/{seriesId}")
    public ResponseEntity<?> deleteSeriesById(@PathVariable(value = "seriesId") Long seriesId, @AuthenticationPrincipal SecurityUserDetails userDetails) {

        seriesService.deleteSeriesById(seriesId, userDetails.getId());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
