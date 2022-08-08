package bg.softuni.creddit.config;

import bg.softuni.creddit.web.interceptor.SecurityCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final SecurityCheckInterceptor securityCheckInterceptor;

    public WebConfig(SecurityCheckInterceptor securityCheckInterceptor) {
        this.securityCheckInterceptor = securityCheckInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(securityCheckInterceptor);
    }
}
