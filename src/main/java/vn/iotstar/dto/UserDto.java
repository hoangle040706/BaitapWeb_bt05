package vn.iotstar.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.iotstar.entity.Role;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String email;
    private String fullname;
    private String phone;
    private String avatar;
    private Role role;
    private Integer status;
    private LocalDateTime createdAt;

    public String getDisplayName() {
        return (fullname != null && !fullname.isBlank()) ? fullname : username;
    }
}
