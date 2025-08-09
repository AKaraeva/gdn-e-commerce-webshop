package at.gdn.frontend.views.anmelden;

import at.gdn.backend.security.AuthenticatedUser;
import at.gdn.frontend.StringLoch;
import at.gdn.frontend.views.MainLayout;
import at.gdn.frontend.views.registrieren.RegistrierenView;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.lineawesome.LineAwesomeIconUrl;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;


@PageTitle("Anmelden | Gesindel der Nacht")
@Route(value = "login", layout = MainLayout.class)
@Menu(order = 1)
@AnonymousAllowed
public class AnmeldenView extends Composite<VerticalLayout> implements BeforeEnterObserver {

    @Autowired
    private AuthenticatedUser authenticatedUser;

    private LoginForm login = new LoginForm();

    public AnmeldenView() {

//        login.setAction("login");
//
//        LoginI18n i18n = LoginI18n.createDefault();
//        i18n.getForm().setTitle(""); // Leerer String entfernt den Titel
//        login.setI18n(i18n);
//        login.getElement().getStyle().set("width", "350px");
//        login.getElement().executeJs(
//                "const style = document.createElement('style');" +
//                        "style.textContent = 'vaadin-login-overlay::part(form) vaadin-button { cursor: pointer !important; }';" +
//                        "document.head.appendChild(style);"
//        );

        login.setAction("login");

        // Setze die Labels auf E-Mail und Passwort
        LoginI18n i18n = LoginI18n.createDefault();
        i18n.getForm().setTitle("");
        i18n.getForm().setUsername("E-Mail-Adresse");
        i18n.getForm().setPassword("Passwort");
        i18n.getForm().setSubmit("Anmelden");
        i18n.getForm().setForgotPassword("Passwort vergessen?");
        login.setI18n(i18n);

        login.getElement().getStyle().set("width", "350px");
        login.getElement().executeJs(
                "this.$.vaadinLoginUsername.setAttribute('type', 'email');"
        );

        // Tabs section
        Tabs tabs = createTabs();
//        tabs.getStyle().set("padding", "20px");
        getContent().add(tabs);

        getContent().add(login);

        // General layout configuration
        configureLayout();
        initializeFooterLayout();

    }

    private void configureLayout() {
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setAlignItems(FlexComponent.Alignment.CENTER);
    }

    private Tabs createTabs() {
        Tabs tabs = new Tabs();
        Tab anmeldenTab = new Tab("Anmelden");
        Tab registrierenTab = new Tab("Registrieren");

        // Add tabs to the Tabs component
        tabs.add(anmeldenTab, registrierenTab);
        tabs.setWidth("min-content");
        tabs.getStyle().set("padding-top", "20px");

        // Correct event listener for tab selection
        tabs.addSelectedChangeListener(event -> {
            if (event.getSelectedTab() == anmeldenTab) {
                UI.getCurrent().navigate(AnmeldenView.class);
            } else if (event.getSelectedTab() == registrierenTab) {
                UI.getCurrent().navigate(RegistrierenView.class);
            }
        });

        return tabs;
    }

    private EmailField createEmailField() {
        EmailField emailField = new EmailField();
        emailField.setLabel("E-MAIL-ADRESSE:");
        emailField.setWidth("min-content");
        return emailField;
    }

    private PasswordField createPasswordField() {
        PasswordField passwordField = new PasswordField();
        passwordField.setLabel("PASSWORT:");
        passwordField.setWidth("min-content");
        return passwordField;
    }

    private Button createPrimaryButton() {
        Button buttonPrimary = new Button("ANMELDEN");
        buttonPrimary.setWidth("min-content");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        return buttonPrimary;
    }

    private Paragraph createForgotPasswordText() {
        Paragraph textSmall = new Paragraph("Passwort vergessen?");
        textSmall.setWidth("max-content");
        textSmall.setHeight("25px");
        textSmall.getStyle().set("font-size", "var(--lumo-font-size-xs)");
        return textSmall;
    }


    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (beforeEnterEvent.getLocation()
                            .getQueryParameters()
                            .getParameters()
                            .containsKey("error")) {
            login.setError(true);
        }
    }

    //----------------Footer-------------------
    private void initializeFooterLayout() {
        getContent().setSizeFull();
        getContent().getStyle().set("display", "flex");
        getContent().getStyle().set("flex-direction", "column");
        getContent().getStyle().set("min-height", "100vh");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setSpacing(false);
        getContent().setPadding(false);

        // Hauptinhalt
        HorizontalLayout layoutRow = new HorizontalLayout();
        VerticalLayout layoutColumn2 = new VerticalLayout();

        layoutRow.setWidth("100%");
        layoutRow.setHeight("min-content");
        layoutRow.setSpacing(false);
        layoutRow.setPadding(false);
        layoutRow.setSizeFull();

        layoutColumn2.setWidth("100%");
//        layoutColumn2.getStyle().set("flex-grow", "1");
        layoutColumn2.getStyle().remove("flex-grow");
        layoutColumn2.setSpacing(true);
        layoutColumn2.setPadding(true);
        layoutColumn2.setAlignItems(FlexComponent.Alignment.CENTER);

        getContent().add(layoutRow);
        getContent().add(layoutColumn2);

        // Spacer sorgt für flexiblen Abstand
        Div spacer = new Div();
        spacer.getStyle().set("flex-grow", "1");
        getContent().add(spacer);
        spacer.getStyle().set("min-height", "20px");
        // Verhindere, dass sich das Layout ausdehnt
        getContent().getStyle().set("flex-grow", "0");
        // Min-height von 100vh auf auto setzen
        getContent().getStyle().set("min-height", "auto");

        // Footer
        HorizontalLayout layoutRow2 = new HorizontalLayout();
        layoutRow2.setWidth("100%");
        layoutRow2.setHeight("min-content");
        layoutRow2.getStyle().set("background-color", "black");
        layoutRow2.getStyle().set("color", "white");
        layoutRow2.getStyle().set("margin-top", "auto");

        VerticalLayout lowerPanel = new VerticalLayout();

        //-------------- Footer-Inhalt-----------
        HorizontalLayout footerContent = new HorizontalLayout();

        VerticalLayout lower1 = new VerticalLayout();
        VerticalLayout lower2 = new VerticalLayout();
        VerticalLayout lower3 = new VerticalLayout();
        VerticalLayout lower4 = new VerticalLayout();

        // ---- Bereich 1: Name & Impressum ----
        H4 lowerName = new H4("Gesindel der Nacht");
        Text lowerText = new Text("IMPRESSUM");
        lowerName.getStyle().set("color", StringLoch.WHITE);
        lower1.add(lowerName, lowerText);
        lower1.getStyle().set("color", StringLoch.WHITE);

        // ---- Bereich 2: Links ----
        Text lower2Heading = new Text("Links");
        UnorderedList downList = new UnorderedList();

        // Home-Link
        Anchor homeLink = new Anchor("/", "Home");
        homeLink.getStyle().set("color", "white");
        homeLink.getStyle().set("text-decoration", "none");
        ListItem homeItem = new ListItem(homeLink);
        homeItem.getStyle().set("cursor", "pointer");

        // Shop-Link
        Anchor shopLink = new Anchor("/shop", "Shop");
        shopLink.getStyle().set("color", "white");
        shopLink.getStyle().set("text-decoration", "none");
        ListItem shopItem = new ListItem(shopLink);
        shopItem.getStyle().set("cursor", "pointer");

        // Künstler-Link
        Anchor artistLink = new Anchor("/künstler", "Künstler");
        artistLink.getStyle().set("color", "white");
        artistLink.getStyle().set("text-decoration", "none");
        ListItem artistItem = new ListItem(artistLink);
        artistItem.getStyle().set("cursor", "pointer");

        // Items zur Liste hinzufügen
        downList.add(homeItem, shopItem, artistItem);
        downList.getStyle().set("color", StringLoch.WHITE);
        downList.getStyle().set("padding", "0");
        downList.getStyle().set("list-style-type", "none");
        downList.getStyle().set("margin", "0");

        lower2.add(lower2Heading, downList);
        lower2.getStyle().set("color", StringLoch.RED);

        // ---- Bereich 3: Hilfe ----
        Text lower3Heading = new Text("Hilfe");
        UnorderedList downList2 = new UnorderedList();
        downList2.add(new ListItem("Datenschutz"));
        downList2.add(new ListItem("FAQ"));
        downList2.add(new ListItem("Nutzungsbedingungen"));
        downList2.add(new ListItem("Urheberrechtsrichtlinie"));

        downList2.getStyle().set("color", StringLoch.WHITE);
        downList2.getStyle().set("padding", "0");
        downList2.getStyle().set("list-style-type", "none");
        downList2.getStyle().set("margin", "0");

        lower3.add(lower3Heading, downList2);
        lower3.getStyle().set("color", StringLoch.RED);

        // ---- Bereich 4: Newsletter ----
        Text lower4Heading = new Text("Newsletter");

        EmailField input = new EmailField();
        input.setPlaceholder("E-Mail eingeben...");
        input.setClearButtonVisible(true);
        input.setWidth("250px");

        Span subscribeText = new Span("Subscribe");
        subscribeText.getStyle().set("color", "#F42929");
        subscribeText.getStyle().set("cursor", "pointer");
        subscribeText.getStyle().set("text-decoration", "underline");

        //TODO: Implementierung des Newsletter-Abonnements mit mehr Abfrage damit Kunde keinen zu großen Blödsinn eingeben kann
        subscribeText.addClickListener(event -> {
            String email = input.getValue();

            if (email.isEmpty() || !email.contains("@")) {

                Notification notification = new Notification();
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.setDuration(3000);
                notification.setPosition(Notification.Position.MIDDLE);

                notification.getElement().getStyle()
                            .set("background-color", "#ffcc00")
                            .set("color", "black")
                            .set("border-radius", "10px")
                            .set("padding", "10px");

                notification.add(new com.vaadin.flow.component.html.Span(" ⚠ Bitte eine gültige E-Mail-Adresse eingeben."));
                notification.open();

            } else {
                // Erstelle ein Bestätigungs-Icon
                Icon successIcon = VaadinIcon.CHECK_CIRCLE_O.create();
                successIcon.getStyle().set("color", "white").set("margin-right", "8px");

                Notification notification = new Notification();
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                notification.setDuration(3000);
                notification.setPosition(Notification.Position.MIDDLE);

                // Nachricht mit Icon kombinieren
                HorizontalLayout layout = new HorizontalLayout(successIcon, new com.vaadin.flow.component.html.Span("Danke für die Anmeldung, " + email + "!"));
                notification.add(layout);
                notification.open();

                input.clear();
            }
        });

        lower4.add(lower4Heading, input, subscribeText);
        lower4.getStyle().set("color", "#F42929");

        footerContent.setWidth("100%");
        footerContent.add(lower1, lower2, lower3, lower4);
        footerContent.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        Hr hr = new Hr();
        hr.getStyle().set("border-top", "2px solid white");

        HorizontalLayout lowerDown = new HorizontalLayout();
        lowerDown.getStyle().set("color", "white");

        Span year = new Span("© Copy Right 2025 - GDN | All rights reserved");
        year.getStyle().set("font-size", "14px");
        year.getStyle().set("margin-left", "20px");

        lowerDown.add(year);
        lowerPanel.add(footerContent, hr, lowerDown);
        layoutRow2.add(lowerPanel);

        getContent().add(layoutRow2);
    }

}