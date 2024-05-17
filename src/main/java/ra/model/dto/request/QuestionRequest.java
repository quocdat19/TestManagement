package ra.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ra.model.entity.Enums.EActiveStatus;
import ra.model.entity.Enums.EQuestionLevel;
import ra.model.entity.Enums.EQuestionType;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class QuestionRequest {
    private String questionContent;
    private String questionType;
    private String questionLevel;
    private String image;
    private String status;
    private Long testId;

}
