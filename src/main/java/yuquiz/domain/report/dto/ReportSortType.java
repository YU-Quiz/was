package yuquiz.domain.report.dto;

import org.springframework.data.domain.Sort;

public enum ReportSortType {
    TYPE_DESC("reportType", Sort.Direction.DESC),
    TYPE_ASC("reportType", Sort.Direction.ASC),
    DATE_DESC("createdAt", Sort.Direction.DESC),
    DATE_ASC("createdAt", Sort.Direction.ASC);

    private String type;
    private Sort.Direction direction;

    ReportSortType(String type, Sort.Direction direction) {
        this.type = type;
        this.direction = direction;
    }

    public Sort getSort() {
        return Sort.by(direction, type);
    }
}
