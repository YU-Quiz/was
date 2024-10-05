package yuquiz.domain.series.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yuquiz.common.exception.CustomException;
import yuquiz.domain.series.dto.SeriesReq;
import yuquiz.domain.series.repository.SeriesRepository;
import yuquiz.domain.study.entity.Study;
import yuquiz.domain.study.exception.StudyExceptionCode;
import yuquiz.domain.study.repository.StudyRepository;
import yuquiz.domain.user.entity.User;
import yuquiz.domain.user.exception.UserExceptionCode;
import yuquiz.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class SeriesService {

    private final SeriesRepository seriesRepository;
    private final StudyRepository studyRepository;
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
}
