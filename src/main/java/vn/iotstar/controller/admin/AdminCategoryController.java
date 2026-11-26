package vn.iotstar.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.iotstar.dto.CategoryDto;
import vn.iotstar.exception.BusinessException;
import vn.iotstar.service.CategoryService;
import vn.iotstar.util.PaginationHelper;

import java.util.Optional;

@Controller
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryService categoryService;

    @GetMapping({"", "/", "/searchpaginated"})
    public String search(
            ModelMap model,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size
    ) {
        int currentPage = Math.max(1, page.orElse(1));
        int pageSize = Math.min(Math.max(1, size.orElse(3)), 50);

        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("name").ascending());
        Page<CategoryDto> resultPage = fetchPage(name, pageable);

        if (resultPage.getTotalPages() > 0 && currentPage > resultPage.getTotalPages()) {
            currentPage = resultPage.getTotalPages();
            pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("name").ascending());
            resultPage = fetchPage(name, pageable);
        }

        if (StringUtils.hasText(name)) {
            model.addAttribute("name", name.trim());
        }
        model.addAttribute("pageNumbers", PaginationHelper.buildPageNumbers(currentPage, resultPage.getTotalPages()));
        model.addAttribute("categoryPage", resultPage);
        return "admin/category/list";
    }

    @GetMapping("/add")
    public String add(ModelMap model) {
        model.addAttribute("category", new CategoryDto());
        model.addAttribute("isEdit", false);
        return "admin/category/addOrEdit";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap model, RedirectAttributes redirectAttributes) {
        Optional<CategoryDto> opt = categoryService.findById(id);
        if (opt.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Không tìm thấy danh mục cần sửa.");
            return "redirect:/admin/categories/searchpaginated";
        }
        model.addAttribute("category", opt.get());
        model.addAttribute("isEdit", true);
        return "admin/category/addOrEdit";
    }

    @PostMapping("/saveOrUpdate")
    public String saveOrUpdate(
            @Valid @ModelAttribute("category") CategoryDto category,
            BindingResult bindingResult,
            ModelMap model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", category.getId() != null);
            return "admin/category/addOrEdit";
        }
        try {
            boolean isEdit = category.getId() != null;
            categoryService.save(category);
            redirectAttributes.addFlashAttribute("message",
                    isEdit ? "Cập nhật danh mục thành công!" : "Thêm danh mục mới thành công!");
            return "redirect:/admin/categories/searchpaginated";
        } catch (BusinessException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("isEdit", category.getId() != null);
            return "admin/category/addOrEdit";
        }
    }

    @RequestMapping(value = "/delete/{id}", method = {RequestMethod.POST, RequestMethod.GET})
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            categoryService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Đã xóa danh mục thành công!");
        } catch (BusinessException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }
        return "redirect:/admin/categories/searchpaginated";
    }

    private Page<CategoryDto> fetchPage(String name, Pageable pageable) {
        if (StringUtils.hasText(name)) {
            return categoryService.findByNameContaining(name, pageable);
        }
        return categoryService.findAll(pageable);
    }
}
