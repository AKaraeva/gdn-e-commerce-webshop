package at.gdn.frontend.views;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Zugriff verweigert | Gesindel der Nacht")
@Route(value="/access_denied", layout = MainLayout.class)
@AnonymousAllowed
public class AccesDenied extends Composite<VerticalLayout> {

    public AccesDenied(AuthenticationContext authContext) {
        configureLayout();
        getContent().add(new Paragraph("Zugriff verweigert"));
    }

    private void configureLayout() {
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setAlignItems(FlexComponent.Alignment.CENTER);
    }

    private Paragraph createForgotPasswordText() {
        Paragraph textSmall = new Paragraph("");
        textSmall.setWidth("max-content");
        textSmall.setHeight("25px");
        textSmall.getStyle().set("font-size", "var(--lumo-font-size-xs)");
        return textSmall;
    }
}