# Web_26-Nov — Spring Boot 4 + JSP/JSTL + SiteMesh 3

Ứng dụng web xây dựng trên **Spring Boot 4.1.3**, **Java 21**, **Spring Data JPA**, **MySQL**, **JSP/JSTL**, **SiteMesh 3.3.0** và **MapStruct 1.6.3** theo kiến trúc phân tầng (`Controller -> Service (Interface/Impl) -> Repository (JpaRepository + JpaSpecificationExecutor) -> Entity`) từ Spring Boot Cheatsheet Guide.

## Tính năng đã hoàn thành

- **Cấu hình Spring Boot 4 + JSP/JSTL + SiteMesh 3**:
  - `JSPStaticResourceConfigurer`, `TomcatJSPConfiguration` (`org.springframework.boot.tomcat.servlet.TomcatServletWebServerFactory`), `CustomSiteMeshFilter` (`sitemesh.integration=filter`), `CharacterEncodingFilter` (`UTF-8`), `server.servlet.session.tracking-modes=cookie`.
  - Giao diện chuyển thể từ `store-video-template` (tông màu `stone/amber`, font serif tiêu đề, header kính mờ, form & bảng bo tròn).
- **Đăng ký, Đăng nhập, Đăng xuất & Phân quyền (`/register`, `/login`, `/logout`)**:
  - Hỗ trợ đăng nhập bằng `username` hoặc `email`, mã hóa BCrypt, chống session fixation (`changeSessionId`), bảo vệ `/admin/**` bằng `AdminAuthInterceptor`.
- **Quản trị CRUD Category (`/admin/categories/**`) & Trang chủ (`/`)**:
  - Tìm kiếm theo tên (`name`), chọn số dòng/trang (`size`), phân trang (`pageNumbers`), thêm/sửa/xóa danh mục với MapStruct + Bean Validation.
- **Quản trị CRUD User (`/admin/users/**`)**:
  - Tìm kiếm động theo từ khóa (`username`, `fullname`, `email`), lọc kết hợp `role` (`ADMIN`/`USER`) và `status` (`1`/`0`) bằng `JpaSpecificationExecutor` (`UserSpecifications`), phân trang `Pageable`.
  - Áp dụng **Validation Groups** (`CreateGroup`, `UpdateGroup`) tách biệt tạo mới (bắt buộc mật khẩu) và cập nhật (giữ mật khẩu cũ nếu để trống), dùng **MapStruct** 2 chiều, chặn admin tự xóa tài khoản đang đăng nhập.
- **Xử lý ngoại lệ tập trung**: `@ControllerAdvice` (`GlobalExceptionHandler`) render trang lỗi thống nhất (`error/error.jsp`).

## Yêu cầu môi trường

- Java 21+
- Maven 3.9+
- MySQL 8+ (chạy ở `localhost:3306`, cấu hình trong `src/main/resources/application.properties`)

## Chạy ứng dụng

```bash
mvn clean test
mvn spring-boot:run
```

Truy cập: `http://localhost:8090`

## Tài khoản mẫu (tự động seed khi khởi động)

- **Admin:** `admin` (hoặc `admin@rangxay.vn`) / `Admin@123`
- **User:** `user` (hoặc `user@rangxay.vn`) / `User@123`
