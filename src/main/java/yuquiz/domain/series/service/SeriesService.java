package yuquiz.domain.series.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yuquiz.common.exception.CustomException;
import yuquiz.domain.series.dto.SeriesReq;
import yuquiz.domain.series.dto.SeriesRes;
import yuquiz.domain.series.entity.Series;
import yuquiz.domain.series.exception.SeriesExceptionCode;
import yuquiz.domain.series.repository.SeriesRepository;
import yuquiz.domain.study.entity.Study;
import yuquiz.domain.study.exception.StudyExceptionCode;
import yuquiz.domain.study.repository.StudyRepository;
import yuquiz.domain.studyUser.repository.StudyUserRepository;
import yuquiz.domain.user.entity.User;
import yuquiz.domain.user.exception.UserExceptionCode;
import yuquiz.domain.user.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SeriesService {

    private final SeriesRepository seriesRepository;
    private final StudyRepository studyRepository;
    private final StudyUserRepository studyUserRepository;
    private final UserRepository userRepository;

    public void createSeries(SeriesReq seriesReq, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserExceptionCode.INVALID_USERID));

        Study study = null;
        if (seriesReq.studyId() != null) {
            study = studyRepository.findById(seriesReq.studyId())
                    .orElseThrow(() -> new CustomException(StudyExceptionCode.INVALID_ID));
        }

        seriesRepository.save(seriesReq.toEntity(user, study));
    }

    public SeriesRes findSeriesById(Long seriesId, Long userId) {

        Series series = seriesRepository.findById(seriesId)
                .orElseThrow(() -> new CustomException(SeriesExceptionCode.INVALID_ID));

        boolean isAuthorized = Optional.ofNullable(series.getStudy())
                .map(study -> validateMember(study.getId(), userId))
                .orElse(true);

        if (!isAuthorized) {
            throw new CustomException(SeriesExceptionCode.UNAUTHORIZED_ACTION);
        }

        return SeriesRes.fromEntity(series);
    }

    private boolean validateMember(Long studyId, Long userId) {
        return studyUserRepository.existsByStudy_IdAndUser_Id(studyId, userId);
    }
}
