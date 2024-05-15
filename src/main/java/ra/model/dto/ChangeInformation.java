package ra.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ChangeInformation {
    @NotEmpty(message = "Không được bỏ trống chỗ này nha!!")
    private String fullName;
    @Email(message = "email bạn chưa đúng định dạng")
    private String email;
    @Pattern(regexp = "^0[1-9]\\d{8}$", message = "Sai số rồi nhaaa, nhập lại đeeeee!!")
    private String phone;
    private String avatar;
}
