package yuquiz.domain.study.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yuquiz.common.exception.CustomException;
import yuquiz.domain.study.dto.StudyFilter;
import yuquiz.domain.study.dto.StudyReq;
import yuquiz.domain.study.dto.StudySortType;
import yuquiz.domain.study.dto.StudySummaryRes;
import yuquiz.domain.study.entity.Study;
import yuquiz.domain.study.exception.StudyExceptionCode;
import yuquiz.domain.study.repository.StudyRepository;
import yuquiz.domain.studyUser.entity.StudyRole;
import yuquiz.domain.studyUser.entity.StudyUser;
import yuquiz.domain.studyUser.repository.StudyUserRepository;
import yuquiz.domain.user.entity.User;
import yuquiz.domain.user.exception.UserExceptionCode;
import yuquiz.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class StudyService {
    private final StudyRepository studyRepository;
    private final StudyUserRepository studyUserRepository;
    private final UserRepository userRepository;


    @Transactional
    public void createStudy(StudyReq studyReq, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserExceptionCode.INVALID_USERID));

        Study study = studyReq.toEntity(user);

        StudyUser studyUser = StudyUser.builder()
                .study(study)
                .user(user)
                .role(StudyRole.LEADER)
                .build();

        studyRepository.save(study);
        studyUserRepository.save(studyUser);
    }

    @Transactional
    public void deleteStudy(Long studyId, Long userId) {
        if (!validateLeader(studyId, userId)) {
            throw new CustomException(StudyExceptionCode.UNAUTHORIZED_ACTION);
        }

        studyRepository.deleteById(studyId);
    }

    @Transactional
    public void updateStudy(StudyReq studyReq, Long studyId, Long userId) {
        if (!validateLeader(studyId, userId)) {
            throw new CustomException(StudyExceptionCode.UNAUTHORIZED_ACTION);
        }

        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new CustomException(StudyExceptionCode.INVALID_ID));

        study.update(studyReq);
    }

    @Transactional(readOnly = true)
    public Page<StudySummaryRes> getStudies(String keyword, Pageable pageable, StudySortType sort, StudyFilter filter) {

        Page<Study> studies = studyRepository.getStudies(keyword, pageable, sort, filter);

        return studies.map(StudySummaryRes::fromEntity);
    }

    private boolean validateLeader(Long studyId, Long userId) {
        return studyRepository.findLeaderById(studyId)
                .map(leaderId -> leaderId.equals(userId))
                .orElse(false);
    }
}
