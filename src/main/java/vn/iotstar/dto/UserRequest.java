package vn.iotstar.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.iotstar.entity.Role;
import vn.iotstar.validation.CreateGroup;
import vn.iotstar.validation.UpdateGroup;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

    @NotNull(groups = UpdateGroup.class, message = "Thiếu mã người dùng cần cập nhật")
    private Long id;

    @NotBlank(groups = {CreateGroup.class, UpdateGroup.class}, message = "Tên đăng nhập không được để trống")
    @Size(groups = {CreateGroup.class, UpdateGroup.class}, min = 3, max = 50, message = "Tên đăng nhập phải từ 3 đến 50 ký tự")
    @Pattern(groups = {CreateGroup.class, UpdateGroup.class}, regexp = "^[a-zA-Z0-9_.-]+$", message = "Tên đăng nhập chỉ gồm chữ, số và ký tự _, ., -")
    private String username;

    @NotBlank(groups = {CreateGroup.class, UpdateGroup.class}, message = "Họ và tên không được để trống")
    @Size(groups = {CreateGroup.class, UpdateGroup.class}, max = 100, message = "Họ và tên tối đa 100 ký tự")
    private String fullname;

    @NotBlank(groups = {CreateGroup.class, UpdateGroup.class}, message = "Email không được để trống")
    @Email(groups = {CreateGroup.class, UpdateGroup.class}, message = "Email không đúng định dạng")
    @Size(groups = {CreateGroup.class, UpdateGroup.class}, max = 100, message = "Email tối đa 100 ký tự")
    private String email;

    @Pattern(groups = {CreateGroup.class, UpdateGroup.class}, regexp = "^$|^0[0-9]{9,10}$", message = "Số điện thoại phải gồm 10-11 chữ số và bắt đầu bằng 0")
    private String phone;

    @Size(groups = {CreateGroup.class, UpdateGroup.class}, max = 255, message = "Đường dẫn ảnh đại diện tối đa 255 ký tự")
    private String avatar;

    @NotNull(groups = {CreateGroup.class, UpdateGroup.class}, message = "Vui lòng chọn vai trò")
    @Builder.Default
    private Role role = Role.USER;

    @NotNull(groups = {CreateGroup.class, UpdateGroup.class}, message = "Vui lòng chọn trạng thái")
    @Builder.Default
    private Integer status = 1;

    @NotBlank(groups = CreateGroup.class, message = "Mật khẩu không được để trống")
    @Size(groups = CreateGroup.class, min = 6, max = 72, message = "Mật khẩu phải từ 6 đến 72 ký tự")
    @Pattern(groups = UpdateGroup.class, regexp = "^$|.{6,72}$", message = "Nếu đổi mật khẩu, mật khẩu phải từ 6 đến 72 ký tự")
    private String password;

    private String confirmPassword;
}
