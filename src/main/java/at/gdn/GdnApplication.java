package at.gdn;

import at.gdn.frontend.views.MainLayout;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.vaadin.flow.component.page.AppShellConfigurator;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@Theme("gdn")
//public class GdnApplication extends SpringBootServletInitializer implements AppShellConfigurator{
public class GdnApplication implements AppShellConfigurator{

		public static void main(String[] args) {
		SpringApplication.run(GdnApplication.class, args);
	}
}
