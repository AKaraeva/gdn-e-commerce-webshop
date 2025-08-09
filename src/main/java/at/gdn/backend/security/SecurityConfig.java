package at.gdn.backend.security;

import at.gdn.frontend.views.anmelden.AnmeldenView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends VaadinWebSecurity {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

            http
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/images/*", "/icons/*", "/static.icons.favicon/*", "/frontend/*", "/theme-editor.css", "/bundles/*","/index.html").permitAll());

        // Call the super class configure method
        super.configure(http);

        // Register login view to the navigation access control mechanism
       setLoginView(http, AnmeldenView.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }
}