package vn.iotstar.config;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;

public class CustomSiteMeshFilter extends ConfigurableSiteMeshFilter {

    @Override
    protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
        builder.addDecoratorPath("/*", "web.jsp")
                .addDecoratorPath("/admin/*", "admin.jsp")
                .addDecoratorPath("/login*", "login.jsp")
                .addDecoratorPath("/register*", "login.jsp")
                .addExcludedPath("/static/*")
                .addExcludedPath("/static/**")
                .addExcludedPath("/alogin*")
                .addExcludedPath("/alogin/*")
                .addExcludedPath("/api/**");
    }
}
