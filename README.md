# Web_26-Nov — Spring Boot 4 + JSP/JSTL + SiteMesh 3

Ứng dụng web xây dựng trên **Spring Boot 4.1.3**, **Java 25 LTS**, **Spring Data JPA**, **MySQL**, **JSP/JSTL** và **SiteMesh 3.3.0** theo kiến trúc phân tầng (`Controller -> Service -> Repository -> Entity`, DTO & Mapper, Bean Validation).

## Tính năng đã hoàn thành (Mốc 26/11/2026)

- Cấu hình JSP/JSTL trên Tomcat nhúng cho Spring Boot 4 (`JSPStaticResourceConfigurer`, `TomcatServletWebServerFactory` tại `org.springframework.boot.tomcat.servlet`, `META-INF/WEB-INF`).
- Cấu hình SiteMesh 3 (`CustomSiteMeshFilter`, `FilterRegistrationBean`) và bộ lọc UTF-8 (`CharacterEncodingFilter`).
- Giao diện chuyển thể từ `store-video-template` (tông màu `stone/amber`, font serif tiêu đề, header kính mờ, form bo tròn).
- **Đăng ký tài khoản** (`/register`): validate input với `jakarta.validation`, kiểm tra trùng `username` / `email`, xác nhận mật khẩu, mã hóa BCrypt.
- **Đăng nhập** (`/login`): hỗ trợ đăng nhập bằng `username` hoặc `email`, chống session fixation (`changeSessionId`), điều hướng theo role (`ADMIN` / `USER`).
- **Đăng xuất** (`/logout`): hủy session và chuyển hướng về `/login?logout`.
- **Phân quyền route `/admin/**`**: bảo vệ qua `AdminAuthInterceptor` (`HttpSession` + `Role.ADMIN`).

## Yêu cầu môi trường

- Java 25 LTS+
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
