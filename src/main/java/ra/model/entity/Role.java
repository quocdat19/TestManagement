package ra.model.entity;

import jakarta.persistence.*;
import lombok.*;
import ra.model.entity.Enums.ERoles;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "role_name")
    private ERoles role ;
}
