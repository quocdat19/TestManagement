package ra.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ra.model.entity.Enums.EActiveStatus;
import ra.model.entity.Enums.EOptionStatus;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OptionRequest {
    private String optionContent;
    private String isCorrect;
    private String status;
    private Long questionId;
}
