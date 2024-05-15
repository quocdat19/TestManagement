package ra.model.dto.wrapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ra.model.entity.Enums.EHttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ResponseWrapper<T>{
    private EHttpStatus httpStatus;
    private Integer httpStatusCode;
    private String httpStatusName;
    private T content;
}
