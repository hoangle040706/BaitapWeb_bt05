package vn.iotstar.controller.admin;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.iotstar.dto.UserDto;
import vn.iotstar.dto.UserRequest;
import vn.iotstar.entity.Role;
import vn.iotstar.exception.BusinessException;
import vn.iotstar.security.AdminAuthInterceptor;
import vn.iotstar.service.UserService;
import vn.iotstar.util.PaginationHelper;
import vn.iotstar.validation.CreateGroup;
import vn.iotstar.validation.UpdateGroup;

import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    @GetMapping({"", "/", "/searchpaginated"})
    public String search(
            ModelMap model,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "role", required = false) Role role,
            @RequestParam(name = "status", required = false) Integer status,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size
    ) {
        int currentPage = Math.max(1, page.orElse(1));
        int pageSize = Math.min(Math.max(1, size.orElse(3)), 50);

        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("username").ascending());
        Page<UserDto> resultPage = userService.search(keyword, role, status, pageable);

        if (resultPage.getTotalPages() > 0 && currentPage > resultPage.getTotalPages()) {
            currentPage = resultPage.getTotalPages();
            pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("username").ascending());
            resultPage = userService.search(keyword, role, status, pageable);
        }

        if (StringUtils.hasText(keyword)) {
            model.addAttribute("keyword", keyword.trim());
        }
        model.addAttribute("selectedRole", role);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("roles", Role.values());
        model.addAttribute("pageNumbers", PaginationHelper.buildPageNumbers(currentPage, resultPage.getTotalPages()));
        model.addAttribute("userPage", resultPage);
        return "admin/user/list";
    }

    @GetMapping("/add")
    public String add(ModelMap model) {
        model.addAttribute("userRequest", new UserRequest());
        return renderForm(model, false);
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap model, RedirectAttributes redirectAttributes) {
        Optional<UserRequest> opt = userService.findRequestById(id);
        if (opt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy người dùng cần sửa.");
            return "redirect:/admin/users/searchpaginated";
        }
        model.addAttribute("userRequest", opt.get());
        return renderForm(model, true);
    }

    @PostMapping("/create")
    public String create(
            @Validated(CreateGroup.class) @ModelAttribute("userRequest") UserRequest userRequest,
            BindingResult bindingResult,
            ModelMap model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return renderForm(model, false);
        }
        try {
            userService.create(userRequest);
            redirectAttributes.addFlashAttribute("message", "Thêm người dùng mới thành công!");
            return "redirect:/admin/users/searchpaginated";
        } catch (BusinessException ex) {
            bindBusinessError(ex, bindingResult, model);
            return renderForm(model, false);
        }
    }

    @PostMapping("/update")
    public String update(
            @Validated(UpdateGroup.class) @ModelAttribute("userRequest") UserRequest userRequest,
            BindingResult bindingResult,
            HttpSession session,
            ModelMap model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return renderForm(model, true);
        }
        try {
            UserDto updated = userService.update(userRequest);
            if (session.getAttribute(AdminAuthInterceptor.SESSION_USER_KEY) instanceof UserDto currentUser
                    && Objects.equals(currentUser.getId(), updated.getId())) {
                session.setAttribute(AdminAuthInterceptor.SESSION_USER_KEY, updated);
            }
            redirectAttributes.addFlashAttribute("message", "Cập nhật người dùng thành công!");
            return "redirect:/admin/users/searchpaginated";
        } catch (BusinessException ex) {
            bindBusinessError(ex, bindingResult, model);
            return renderForm(model, true);
        }
    }

    @RequestMapping(value = "/delete/{id}", method = {RequestMethod.POST, RequestMethod.GET})
    public String delete(
            @PathVariable("id") Long id,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        Long currentUserId = (session.getAttribute(AdminAuthInterceptor.SESSION_USER_KEY) instanceof UserDto u)
                ? u.getId()
                : null;
        try {
            userService.deleteById(id, currentUserId);
            redirectAttributes.addFlashAttribute("message", "Đã xóa người dùng thành công!");
        } catch (BusinessException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/admin/users/searchpaginated";
    }

    private String renderForm(ModelMap model, boolean isEdit) {
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("roles", Role.values());
        return "admin/user/addOrEdit";
    }

    private void bindBusinessError(BusinessException ex, BindingResult bindingResult, ModelMap model) {
        if (ex.getField() != null) {
            bindingResult.rejectValue(ex.getField(), "business.error", ex.getMessage());
        } else {
            model.addAttribute("errorMessage", ex.getMessage());
        }
    }
}
