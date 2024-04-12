package ra.model.entity;

import jakarta.persistence.*;
import lombok.*;
import ra.model.entity.Enums.EActiveStatus;
import ra.model.entity.Enums.EOptionStatus;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String optionContent;
    @Enumerated(EnumType.STRING)
    @Column(name = "is_correct")
    private EOptionStatus isCorrect;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EActiveStatus status;
    @ManyToOne
    @JoinColumn(name = "question_id",referencedColumnName = "id")
    private Question question;
}
