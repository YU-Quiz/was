package yuquiz.domain.subject.dto;

import yuquiz.domain.subject.entity.Subject;

public record SubjectRes(
        Long id,
        String subjectName,
        String subjectCode
) {
    public static SubjectRes fromEntity(Subject subject) {
        return new SubjectRes(
                subject.getId(),
                subject.getSubjectName(),
                subject.getSubjectCode()
        );
    }
}
