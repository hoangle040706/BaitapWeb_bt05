package vn.iotstar;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import vn.iotstar.dto.CategoryDto;
import vn.iotstar.dto.UserDto;
import vn.iotstar.entity.Role;
import vn.iotstar.repository.UserRepository;
import vn.iotstar.security.AdminAuthInterceptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    @SuppressWarnings("unchecked")
    void homePageLoadsPaginatedAndSearchableCategories() throws Exception {
        MvcResult result = mockMvc.perform(get("/").param("size", "4").param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("web/home"))
                .andExpect(model().attributeExists("categoryPage", "pageNumbers"))
                .andReturn();
        assertThat(result.getResponse().getIncludedUrl()).isEqualTo("/WEB-INF/views/web/home.jsp");

        Page<CategoryDto> page = (Page<CategoryDto>) result.getModelAndView().getModel().get("categoryPage");
        assertThat(page.getSize()).isEqualTo(4);
        assertThat(page.getTotalElements()).isGreaterThanOrEqualTo(4);

        MvcResult searchResult = mockMvc.perform(get("/").param("name", "Arabica"))
                .andExpect(status().isOk())
                .andExpect(view().name("web/home"))
                .andExpect(model().attribute("name", "Arabica"))
                .andReturn();
        Page<CategoryDto> filtered = (Page<CategoryDto>) searchResult.getModelAndView().getModel().get("categoryPage");
        assertThat(filtered.getContent()).extracting(CategoryDto::getName)
                .anyMatch(n -> n.contains("Arabica"));
    }

    @Test
    void registerLoginAndLogoutFlowSucceeds() throws Exception {
        mockMvc.perform(post("/register")
                        .param("fullname", "Nguyễn Văn Test")
                        .param("username", "testuser_nov26")
                        .param("email", "testuser_nov26@example.com")
                        .param("password", "Secret123")
                        .param("confirmPassword", "Secret123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        assertThat(userRepository.existsByUsernameIgnoreCase("testuser_nov26")).isTrue();

        MvcResult loginResult = mockMvc.perform(post("/login")
                        .param("usernameOrEmail", "testuser_nov26")
                        .param("password", "Secret123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andReturn();

        MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession(false);
        assertThat(session).isNotNull();
        UserDto sessionUser = (UserDto) session.getAttribute(AdminAuthInterceptor.SESSION_USER_KEY);
        assertThat(sessionUser).isNotNull();
        assertThat(sessionUser.getRole()).isEqualTo(Role.USER);

        // Non-admin user cannot access /admin/home
        mockMvc.perform(get("/admin/home").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/?error=forbidden"));

        // Logout invalidates session
        mockMvc.perform(post("/logout").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?logout"));
    }

    @Test
    void registerRejectsMismatchedPasswordAndDuplicateUsername() throws Exception {
        mockMvc.perform(post("/register")
                        .param("fullname", "Mismatch User")
                        .param("username", "mismatch1")
                        .param("email", "mismatch1@example.com")
                        .param("password", "Secret123")
                        .param("confirmPassword", "Wrong123"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register"))
                .andExpect(model().attributeHasFieldErrors("registerRequest", "confirmPassword"));

        mockMvc.perform(post("/register")
                        .param("fullname", "Duplicate Admin")
                        .param("username", "admin")
                        .param("email", "unique_mail@example.com")
                        .param("password", "Secret123")
                        .param("confirmPassword", "Secret123"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register"))
                .andExpect(model().attributeHasFieldErrors("registerRequest", "username"));
    }

    @Test
    void adminCanLoginAndAccessAdminHome() throws Exception {
        // Anonymous user redirected to login with next param
        mockMvc.perform(get("/admin/home"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?next=%2Fadmin%2Fhome"));

        // Wrong password fails
        mockMvc.perform(post("/login")
                        .param("usernameOrEmail", "admin")
                        .param("password", "WrongPass"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"))
                .andExpect(model().attributeExists("errorMessage"));

        // Correct password logs in and enters /admin/home
        MvcResult adminLogin = mockMvc.perform(post("/login")
                        .param("usernameOrEmail", "admin@rangxay.vn")
                        .param("password", "Admin@123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/home"))
                .andReturn();

        MockHttpSession adminSession = (MockHttpSession) adminLogin.getRequest().getSession(false);
        MvcResult adminResult = mockMvc.perform(get("/admin/home").session(adminSession))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/home"))
                .andExpect(model().attributeExists("categoryCount", "userCount"))
                .andReturn();
        assertThat(adminResult.getResponse().getIncludedUrl()).isEqualTo("/WEB-INF/views/admin/home.jsp");

        // Admin category search & pagination
        MvcResult searchPaginated = mockMvc.perform(get("/admin/categories/searchpaginated")
                        .session(adminSession)
                        .param("name", "Cà phê")
                        .param("page", "1")
                        .param("size", "3"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/category/list"))
                .andExpect(model().attributeExists("categoryPage", "pageNumbers"))
                .andExpect(model().attribute("name", "Cà phê"))
                .andReturn();
        assertThat(searchPaginated.getResponse().getIncludedUrl()).isEqualTo("/WEB-INF/views/admin/category/list.jsp");
    }
}
