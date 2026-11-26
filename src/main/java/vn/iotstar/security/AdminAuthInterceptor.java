package vn.iotstar.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import vn.iotstar.dto.UserDto;
import vn.iotstar.entity.Role;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Protects /admin/** routes via session authentication and ADMIN role verification.
 */
@Component
public class AdminAuthInterceptor implements HandlerInterceptor {

    public static final String SESSION_USER_KEY = "currentUser";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        Object attr = (session != null) ? session.getAttribute(SESSION_USER_KEY) : null;

        if (!(attr instanceof UserDto currentUser)) {
            String target = request.getRequestURI();
            String query = request.getQueryString();
            if (query != null && !query.isBlank()) {
                target = target + "?" + query;
            }
            String nextParam = URLEncoder.encode(target, StandardCharsets.UTF_8);
            response.sendRedirect(request.getContextPath() + "/login?next=" + nextParam);
            return false;
        }

        if (currentUser.getRole() != Role.ADMIN) {
            response.sendRedirect(request.getContextPath() + "/?error=forbidden");
            return false;
        }

        return true;
    }
}
