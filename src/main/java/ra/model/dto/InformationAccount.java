package ra.model.dto;

import lombok.*;
import ra.model.entity.Enums.EActiveStatus;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class InformationAccount {
    private Long userId;
    private String fullName;
    private String username;
    private String avatar;
    private String email;
    private String phone;
    private EActiveStatus status;
}
