package vn.iotstar.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.iotstar.dto.LoginRequest;
import vn.iotstar.dto.RegisterRequest;
import vn.iotstar.dto.UserDto;
import vn.iotstar.entity.Role;
import vn.iotstar.exception.BusinessException;
import vn.iotstar.security.AdminAuthInterceptor;
import vn.iotstar.service.UserService;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String showLoginForm(
            @RequestParam(name = "next", required = false) String next,
            @RequestParam(name = "logout", required = false) String logout,
            HttpSession session,
            Model model
    ) {
        if (session.getAttribute(AdminAuthInterceptor.SESSION_USER_KEY) instanceof UserDto currentUser) {
            return redirectByRole(currentUser, next);
        }
        if (!model.containsAttribute("loginRequest")) {
            model.addAttribute("loginRequest", new LoginRequest());
        }
        if (logout != null) {
            model.addAttribute("successMessage", "Bạn đã đăng xuất khỏi hệ thống.");
        }
        model.addAttribute("next", sanitizeNext(next));
        return "auth/login";
    }

    @PostMapping("/login")
    public String processLogin(
            @Valid @ModelAttribute("loginRequest") LoginRequest loginRequest,
            BindingResult bindingResult,
            @RequestParam(name = "next", required = false) String next,
            HttpServletRequest request,
            Model model
    ) {
        String safeNext = sanitizeNext(next);
        model.addAttribute("next", safeNext);

        if (bindingResult.hasErrors()) {
            return "auth/login";
        }

        try {
            UserDto authenticatedUser = userService.authenticate(loginRequest);
            HttpSession session = request.getSession(true);
            request.changeSessionId();
            session.setAttribute(AdminAuthInterceptor.SESSION_USER_KEY, authenticatedUser);
            return redirectByRole(authenticatedUser, safeNext);
        } catch (BusinessException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "auth/login";
        }
    }

    @GetMapping("/register")
    public String showRegisterForm(
            @RequestParam(name = "next", required = false) String next,
            HttpSession session,
            Model model
    ) {
        if (session.getAttribute(AdminAuthInterceptor.SESSION_USER_KEY) instanceof UserDto currentUser) {
            return redirectByRole(currentUser, next);
        }
        if (!model.containsAttribute("registerRequest")) {
            model.addAttribute("registerRequest", new RegisterRequest());
        }
        model.addAttribute("next", sanitizeNext(next));
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegister(
            @Valid @ModelAttribute("registerRequest") RegisterRequest registerRequest,
            BindingResult bindingResult,
            @RequestParam(name = "next", required = false) String next,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        String safeNext = sanitizeNext(next);
        model.addAttribute("next", safeNext);

        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        try {
            userService.register(registerRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Đăng ký tài khoản thành công. Vui lòng đăng nhập.");
            if (safeNext != null) {
                redirectAttributes.addAttribute("next", safeNext);
            }
            return "redirect:/login";
        } catch (BusinessException ex) {
            if (ex.getField() != null) {
                bindingResult.rejectValue(ex.getField(), "business.error", ex.getMessage());
            } else {
                model.addAttribute("errorMessage", ex.getMessage());
            }
            return "auth/register";
        }
    }

    @RequestMapping(value = "/logout", method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login?logout";
    }

    private String redirectByRole(UserDto user, String rawNext) {
        String safeNext = sanitizeNext(rawNext);
        if (safeNext != null) {
            if (safeNext.startsWith("/admin") && user.getRole() != Role.ADMIN) {
                return "redirect:/";
            }
            return "redirect:" + safeNext;
        }
        if (user.getRole() == Role.ADMIN) {
            return "redirect:/admin/home";
        }
        return "redirect:/";
    }

    private String sanitizeNext(String rawNext) {
        if (rawNext == null || rawNext.isBlank()) {
            return null;
        }
        if (rawNext.startsWith("/") && !rawNext.startsWith("//")
                && !rawNext.contains("\r") && !rawNext.contains("\n")) {
            return rawNext;
        }
        return null;
    }
}
