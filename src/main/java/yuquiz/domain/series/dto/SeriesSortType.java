package yuquiz.domain.series.dto;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;

@AllArgsConstructor
public enum SeriesSortType {
    DATE_DESC("createdAt", Sort.Direction.DESC),
    DATE_ASC("createdAt", Sort.Direction.ASC);

    private final String type;
    private final Sort.Direction direction;

    public Sort getSort() {
        return Sort.by(direction, type);
    }
}
