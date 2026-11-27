package vn.iotstar.exception;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NoResourceFoundException.class)
    public String handleNotFound(NoResourceFoundException ex, HttpServletResponse response, Model model) {
        return renderError(response, model, HttpStatus.NOT_FOUND,
                "Không tìm thấy trang",
                "Đường dẫn bạn yêu cầu không tồn tại hoặc đã được di chuyển.");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletResponse response, Model model) {
        return renderError(response, model, HttpStatus.BAD_REQUEST,
                "Tham số không hợp lệ",
                "Giá trị tham số gửi lên không đúng định dạng.");
    }

    @ExceptionHandler(BusinessException.class)
    public String handleBusiness(BusinessException ex, HttpServletResponse response, Model model) {
        return renderError(response, model, HttpStatus.BAD_REQUEST,
                "Yêu cầu không hợp lệ",
                ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public String handleUnexpected(Exception ex, HttpServletResponse response, Model model) {
        log.error("Unhandled exception in MVC controller", ex);
        return renderError(response, model, HttpStatus.INTERNAL_SERVER_ERROR,
                "Lỗi hệ thống",
                "Đã xảy ra lỗi ngoài ý muốn. Vui lòng thử lại sau.");
    }

    private String renderError(HttpServletResponse response, Model model,
                               HttpStatus status, String title, String message) {
        response.setStatus(status.value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        model.addAttribute("statusCode", status.value());
        model.addAttribute("errorTitle", title);
        model.addAttribute("errorMessage", message);
        return "error/error";
    }
}
