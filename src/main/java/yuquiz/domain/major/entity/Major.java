package yuquiz.domain.major.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yuquiz.domain.subject.entity.Subject;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Major {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "major_name")
    private String majorName;

    @OneToMany(mappedBy = "major", cascade = CascadeType.REMOVE)
    private List<Subject> subjects = new ArrayList<>();

    @Builder
    public Major(String majorName) {
        this.majorName = majorName;
    }
}
