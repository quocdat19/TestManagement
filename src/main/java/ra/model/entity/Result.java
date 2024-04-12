package ra.model.entity;

import jakarta.persistence.*;
import lombok.*;
import ra.model.entity.Enums.EActiveStatus;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private EActiveStatus status;
    private Double mark;
    private Integer examTimes;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "test_id",referencedColumnName = "id")
    private Test test;
}
