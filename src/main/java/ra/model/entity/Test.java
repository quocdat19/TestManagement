package ra.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import ra.model.entity.Enums.EActiveStatus;
import ra.model.entity.Enums.ETestType;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "test_name")
    private String testName;
    @Column(name = "test_time")
    private Integer testTime;
    @Enumerated(EnumType.STRING)
    @Column(name = "test_type")
    private ETestType testType;;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EActiveStatus status;
    @OneToMany(mappedBy = "test")
    @JsonIgnore
    List<Question> questions;
    @OneToMany(mappedBy = "test")
    @JsonIgnore
    List<Result> historyTests;
    @ManyToOne
    @JoinColumn(name = "exam_id", referencedColumnName = "id")
    private Exam exam;
}
