package vn.iotstar;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import vn.iotstar.dto.CategoryDto;
import vn.iotstar.dto.UserDto;
import vn.iotstar.entity.Category;
import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;
import vn.iotstar.repository.CategoryRepository;
import vn.iotstar.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AdminCrudIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private MockHttpSession loginAsAdmin() throws Exception {
        MvcResult result = mockMvc.perform(post("/login")
                        .param("usernameOrEmail", "admin")
                        .param("password", "Admin@123"))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        return (MockHttpSession) result.getRequest().getSession(false);
    }

    @Test
    void categoryCrudLifecycleWorksEndToEnd() throws Exception {
        MockHttpSession session = loginAsAdmin();

        // Validation rejects blank name
        mockMvc.perform(post("/admin/categories/saveOrUpdate")
                        .session(session)
                        .param("name", " ")
                        .param("status", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/category/addOrEdit"))
                .andExpect(model().attributeHasFieldErrors("category", "name"));

        // Create category
        mockMvc.perform(post("/admin/categories/saveOrUpdate")
                        .session(session)
                        .param("name", "Matcha Nhật Bản")
                        .param("images", "matcha.jpg")
                        .param("status", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/categories/searchpaginated"))
                .andExpect(flash().attributeExists("message"));

        Category created = categoryRepository.findAll().stream()
                .filter(c -> "Matcha Nhật Bản".equals(c.getName()))
                .findFirst()
                .orElseThrow();

        // Edit & update category
        mockMvc.perform(get("/admin/categories/edit/{id}", created.getId()).session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/category/addOrEdit"));

        mockMvc.perform(post("/admin/categories/saveOrUpdate")
                        .session(session)
                        .param("id", String.valueOf(created.getId()))
                        .param("name", "Matcha Uji Premium")
                        .param("images", "uji.jpg")
                        .param("status", "0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/categories/searchpaginated"));

        Category updated = categoryRepository.findById(created.getId()).orElseThrow();
        assertThat(updated.getName()).isEqualTo("Matcha Uji Premium");
        assertThat(updated.getStatus()).isEqualTo(0);

        // Delete category
        mockMvc.perform(post("/admin/categories/delete/{id}", created.getId()).session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/categories/searchpaginated"));
        assertThat(categoryRepository.existsById(created.getId())).isFalse();
    }

    @Test
    @SuppressWarnings("unchecked")
    void userSearchPaginationAndCrudWithValidationGroupsWork() throws Exception {
        MockHttpSession session = loginAsAdmin();

        // Search + Specification filter by role & keyword
        MvcResult listResult = mockMvc.perform(get("/admin/users/searchpaginated")
                        .session(session)
                        .param("keyword", "admin")
                        .param("role", "ADMIN")
                        .param("status", "1")
                        .param("page", "1")
                        .param("size", "3"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/user/list"))
                .andExpect(model().attributeExists("userPage", "pageNumbers"))
                .andReturn();

        Page<UserDto> filtered = (Page<UserDto>) listResult.getModelAndView().getModel().get("userPage");
        assertThat(filtered.getContent()).isNotEmpty();
        assertThat(filtered.getContent()).allMatch(u -> u.getRole() == Role.ADMIN);

        // CreateGroup rejects blank password
        mockMvc.perform(post("/admin/users/create")
                        .session(session)
                        .param("username", "staff_new")
                        .param("fullname", "Nhân viên Mới")
                        .param("email", "staff_new@rangxay.vn")
                        .param("role", "USER")
                        .param("status", "1")
                        .param("password", "")
                        .param("confirmPassword", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/user/addOrEdit"))
                .andExpect(model().attributeHasFieldErrors("userRequest", "password"));

        // Create valid user
        mockMvc.perform(post("/admin/users/create")
                        .session(session)
                        .param("username", "staff_new")
                        .param("fullname", "Nhân viên Mới")
                        .param("email", "staff_new@rangxay.vn")
                        .param("phone", "0901234567")
                        .param("role", "USER")
                        .param("status", "1")
                        .param("password", "Staff@123")
                        .param("confirmPassword", "Staff@123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users/searchpaginated"))
                .andExpect(flash().attributeExists("message"));

        User createdUser = userRepository.findByUsernameIgnoreCase("staff_new").orElseThrow();
        String originalHash = createdUser.getPassword();
        assertThat(passwordEncoder.matches("Staff@123", originalHash)).isTrue();

        // UpdateGroup allows blank password to keep existing hash
        mockMvc.perform(post("/admin/users/update")
                        .session(session)
                        .param("id", String.valueOf(createdUser.getId()))
                        .param("username", "staff_new")
                        .param("fullname", "Nhân viên Đã Sửa")
                        .param("email", "staff_new@rangxay.vn")
                        .param("phone", "0909876543")
                        .param("role", "ADMIN")
                        .param("status", "0")
                        .param("password", "")
                        .param("confirmPassword", ""))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users/searchpaginated"));

        User updatedUser = userRepository.findById(createdUser.getId()).orElseThrow();
        assertThat(updatedUser.getFullname()).isEqualTo("Nhân viên Đã Sửa");
        assertThat(updatedUser.getRole()).isEqualTo(Role.ADMIN);
        assertThat(updatedUser.getStatus()).isEqualTo(0);
        assertThat(updatedUser.getPassword()).isEqualTo(originalHash);

        // Self-deletion is blocked
        User adminUser = userRepository.findByUsernameIgnoreCase("admin").orElseThrow();
        mockMvc.perform(post("/admin/users/delete/{id}", adminUser.getId()).session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users/searchpaginated"))
                .andExpect(flash().attributeExists("errorMessage"));
        assertThat(userRepository.existsById(adminUser.getId())).isTrue();

        // Deleting created user succeeds
        mockMvc.perform(post("/admin/users/delete/{id}", createdUser.getId()).session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users/searchpaginated"))
                .andExpect(flash().attributeExists("message"));
        assertThat(userRepository.existsById(createdUser.getId())).isFalse();
    }

    @Test
    void globalExceptionHandlerRendersErrorViewOnInvalidPathType() throws Exception {
        MockHttpSession session = loginAsAdmin();

        mockMvc.perform(get("/admin/users/edit/not-a-number").session(session))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("error/error"))
                .andExpect(model().attribute("statusCode", 400));
    }
}
