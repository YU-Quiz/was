package yuquiz.domain.study.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yuquiz.common.exception.CustomException;
import yuquiz.domain.study.dto.StudyFilter;
import yuquiz.domain.study.dto.StudyReq;
import yuquiz.domain.study.dto.StudyRequestRes;
import yuquiz.domain.study.dto.StudyRes;
import yuquiz.domain.study.dto.StudySortType;
import yuquiz.domain.study.dto.StudySummaryRes;
import yuquiz.domain.study.entity.Study;
import yuquiz.domain.study.exception.StudyExceptionCode;
import yuquiz.domain.study.repository.StudyRepository;
import yuquiz.domain.studyUser.entity.StudyRole;
import yuquiz.domain.studyUser.entity.StudyUser;
import yuquiz.domain.studyUser.entity.UserState;
import yuquiz.domain.studyUser.repository.StudyUserRepository;
import yuquiz.domain.user.entity.User;
import yuquiz.domain.user.exception.UserExceptionCode;
import yuquiz.domain.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

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

    @Transactional(readOnly = true)
    public StudyRes getStudy(Long studyId, Long userId) {

        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new CustomException(StudyExceptionCode.INVALID_ID));

        return studyUserRepository.findStudyUserByStudy_IdAndUser_Id(studyId, userId)
                .map(studyUser -> StudyRes.fromEntity(study, true, studyUser.getRole()))
                .orElseGet(() -> StudyRes.fromEntity(study, false, null));
    }

    @Transactional
    public void requestRegister(Long studyId, Long userId) {

        if (studyUserRepository.existsByStudy_IdAndUser_Id(studyId, userId)) {
            throw new CustomException(StudyExceptionCode.ALREADY_REGISTERED);
        }

        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new CustomException(StudyExceptionCode.INVALID_ID));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserExceptionCode.INVALID_USERID));

        StudyUser studyUser = StudyUser.builder()
                .user(user)
                .study(study)
                .state(UserState.PENDING)
                .role(StudyRole.USER)
                .build();

        studyUserRepository.save(studyUser);
    }

    @Transactional(readOnly = true)
    public List<StudyRequestRes> getRegisterRequests(Long studyId, Long userId) {
        if (!validateLeader(studyId, userId)) {
            throw new CustomException(StudyExceptionCode.UNAUTHORIZED_ACTION);
        }

        List<StudyUser> studyUsers = studyUserRepository.findByStudyIdAndState(studyId, UserState.PENDING);

        return studyUsers.stream().map(StudyRequestRes::fromEntity).toList();
    }

    @Transactional
    public void acceptRequest(Long studyId, Long pendingUserId, Long userId) {
        if (!validateLeader(studyId, userId)) {
            throw new CustomException(StudyExceptionCode.UNAUTHORIZED_ACTION);
        }

        StudyUser studyUser = studyUserRepository.findStudyUserByStudy_IdAndUser_Id(studyId, pendingUserId)
                .orElseThrow(() -> new CustomException(StudyExceptionCode.REQUEST_NOT_EXIST));

        if (studyUser.getState().equals(UserState.REGISTERED)) {
            throw new CustomException(StudyExceptionCode.ALREADY_REGISTERED);
        }

        studyUser.accept();
    }

    private boolean validateLeader(Long studyId, Long userId) {
        return studyRepository.findLeaderById(studyId)
                .map(leaderId -> leaderId.equals(userId))
                .orElse(false);
    }
}
