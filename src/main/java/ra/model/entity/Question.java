package ra.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import ra.model.base.AuditableEntity;
import ra.model.entity.Enums.EActiveStatus;
import ra.model.entity.Enums.EQuestionLevel;
import ra.model.entity.Enums.EQuestionType;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Question extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String questionContent;
    @Enumerated(EnumType.STRING)
    @Column(name = "question_type")
    private EQuestionType questionType;
    @Enumerated(EnumType.STRING)
    @Column(name = "question_level")
    private EQuestionLevel questionLevel;
    private String image;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EActiveStatus status;
    @ManyToOne
    @JoinColumn(name = "test_id", referencedColumnName = "id")
    private Test test;
    @OneToMany(mappedBy = "question")
    @JsonIgnore
    List<Option> options;
}


