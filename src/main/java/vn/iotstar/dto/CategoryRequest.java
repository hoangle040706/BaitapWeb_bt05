package vn.iotstar.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryRequest {

    private Long id;

    @NotBlank(message = "Tên danh mục không được để trống")
    @Size(max = 255, message = "Tên danh mục tối đa 255 ký tự")
    private String name;

    @Size(max = 500, message = "Đường dẫn ảnh tối đa 500 ký tự")
    private String images;

    @NotNull(message = "Vui lòng chọn trạng thái")
    @Builder.Default
    private Integer status = 1;
}
