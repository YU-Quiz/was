package yuquiz.domain.subject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yuquiz.domain.subject.dto.SubjectRes;
import yuquiz.domain.subject.entity.Subject;
import yuquiz.domain.subject.repository.SubjectRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectRepository subjectRepository;

    public List<SubjectRes> getSubjectByKeyword(String keyword) {
        List<Subject> subjects = subjectRepository.findSubjectsByKeyword(keyword);

        return subjects.stream()
                .map(SubjectRes::fromEntity)
                .collect(Collectors.toList());
    }
}
