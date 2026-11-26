package vn.iotstar.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.iotstar.dto.CategoryDto;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.UserService;
import vn.iotstar.util.PaginationHelper;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final CategoryService categoryService;
    private final UserService userService;

    @GetMapping({"/", "/home"})
    public String home(
            @RequestParam(name = "error", required = false) String error,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            Model model
    ) {
        if ("forbidden".equals(error)) {
            model.addAttribute("errorMessage", "Bạn không có quyền truy cập khu vực quản trị.");
        }

        int currentPage = Math.max(1, page.orElse(1));
        int pageSize = Math.min(Math.max(1, size.orElse(4)), 24);
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("name").ascending());
        Page<CategoryDto> categoryPage = categoryService.findActivePaginated(name, pageable);

        if (categoryPage.getTotalPages() > 0 && currentPage > categoryPage.getTotalPages()) {
            currentPage = categoryPage.getTotalPages();
            pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("name").ascending());
            categoryPage = categoryService.findActivePaginated(name, pageable);
        }

        if (StringUtils.hasText(name)) {
            model.addAttribute("name", name.trim());
        }
        model.addAttribute("categoryPage", categoryPage);
        model.addAttribute("pageNumbers", PaginationHelper.buildPageNumbers(currentPage, categoryPage.getTotalPages()));
        return "web/home";
    }

    @GetMapping({"/admin", "/admin/", "/admin/home"})
    public String adminHome(Model model) {
        model.addAttribute("categoryCount", categoryService.count());
        model.addAttribute("userCount", userService.count());
        return "admin/home";
    }
}
