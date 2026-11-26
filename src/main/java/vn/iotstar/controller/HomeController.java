package vn.iotstar.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.UserService;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final CategoryService categoryService;
    private final UserService userService;

    @GetMapping({"/", "/home"})
    public String home(@RequestParam(name = "error", required = false) String error, Model model) {
        if ("forbidden".equals(error)) {
            model.addAttribute("errorMessage", "Bạn không có quyền truy cập khu vực quản trị.");
        }
        model.addAttribute("categories", categoryService.findAllActive());
        return "web/home";
    }

    @GetMapping({"/admin", "/admin/", "/admin/home"})
    public String adminHome(Model model) {
        model.addAttribute("categoryCount", categoryService.count());
        model.addAttribute("userCount", userService.count());
        return "admin/home";
    }
}
