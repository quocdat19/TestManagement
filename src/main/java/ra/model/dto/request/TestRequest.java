package ra.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TestRequest {
    private String testName;
    private Integer testTime;
    private String testType;
    private String status;
    private Long examId;


}
