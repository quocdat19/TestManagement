package ra.model.dto.request;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class QuestionOptionRequest {
    QuestionRequest questionRequest;
    List<OptionRequest> optionRequests;
}
