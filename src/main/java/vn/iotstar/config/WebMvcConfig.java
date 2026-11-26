package vn.iotstar.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import vn.iotstar.security.AdminAuthInterceptor;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final AdminAuthInterceptor adminAuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminAuthInterceptor)
                .addPathPatterns("/admin", "/admin/**");
    }

    /**
     * Tomcat 11 closes/unwraps the outer response after RequestDispatcher.forward(),
     * which empties SiteMesh's response buffer; include() keeps buffering intact.
     */
    @Bean
    static BeanPostProcessor jspAlwaysIncludePostProcessor() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof InternalResourceViewResolver resolver) {
                    resolver.setAlwaysInclude(true);
                }
                return bean;
            }
        };
    }
}
