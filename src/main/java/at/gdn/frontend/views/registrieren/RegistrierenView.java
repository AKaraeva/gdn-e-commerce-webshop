package at.gdn.frontend.views.registrieren;

import at.gdn.backend.entities.Country;
import at.gdn.backend.entities.User;
import at.gdn.backend.enums.UserRole;
import at.gdn.backend.richtypes.Firstname;
import at.gdn.backend.richtypes.Lastname;
import at.gdn.backend.richtypes.Username;
import at.gdn.backend.service.UserService;
import at.gdn.backend.valueobjects.Address;
import at.gdn.frontend.StringLoch;
import at.gdn.frontend.views.MainLayout;
import at.gdn.frontend.views.anmelden.AnmeldenView;
import at.gdn.frontend.views.checkout.CheckoutViewGuest;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.BeanValidator;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import jakarta.validation.constraints.Email;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import static at.gdn.frontend.StringLoch.FIELD_WIDTH;

@PageTitle("Registrieren | Gesindel der Nacht")
@Route(value = "/registrieren", layout = MainLayout.class)
@Menu(order = 2, icon = LineAwesomeIconUrl.PENCIL_RULER_SOLID)
@AnonymousAllowed
public class RegistrierenView extends Composite<VerticalLayout> {
    private final VerticalLayout nameLayout;
    private final EmailField emailField;
    private final PasswordField passwordField;
    private final PasswordField confirmPasswordField;
    private Button activeButton = null;


    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private Binder<User> binder;

    public RegistrierenView(UserService userService) {

        this.userService = userService;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.binder = new Binder<>(User.class);

        // Tabs section
        Tabs tabs = createTabs();
        getContent().add(tabs);

        // Title Section
//        H1 title = createTitle();
//        getContent().add(title);

        // Name Section
        this.nameLayout = createNameFields();
        getContent().add(nameLayout);


        // Email Section
        this.emailField = createEmailField();
        getContent().add(emailField);


        // Password Section
        this.passwordField = createPasswordField("Passwort");
        this.confirmPasswordField = createPasswordField("Passwort erneut eingeben");
        getContent().add(passwordField);
        getContent().add(confirmPasswordField);

        // Button Section
        HorizontalLayout buttonLayout = createButtonLayout();
        getContent().add(buttonLayout);

        //AGB Section
        Paragraph agbText = createAGBText();
        getContent().add(agbText);

        configureLayout();
        initializeFooterLayout();

        // Stelle sicher, dass sich das Hauptlayout korrekt verhält
        getContent().setSizeFull();
        getContent().getStyle().set("display", "flex");
        getContent().getStyle().set("flex-direction", "column");
        getContent().getStyle().set("justify-content", "space-between");
        getContent().getStyle().set("margin", "0");
        getContent().getStyle().set("padding", "0");

    }

    private void configureLayout() {
        getContent().addClassName(Padding.XLARGE);
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setAlignItems(FlexComponent.Alignment.CENTER);


    }

    private H1 createTitle() {
        H1 title = new H1("Registrieren");
        title.setWidth("max-content");
        return title;
    }

    private Tabs createTabs() {
        Tabs tabs = new Tabs();
        Tab anmeldenTab = new Tab("Anmelden");
        Tab registrierenTab = new Tab("Registrieren");

        // Add tabs to the Tabs component
        tabs.add(registrierenTab, anmeldenTab);
        tabs.setWidth("min-content");
        tabs.getStyle().set("padding-top", "20px");

        // Correct event listener for tab selection
        tabs.addSelectedChangeListener(event -> {
            if (event.getSelectedTab() == registrierenTab) {
                UI.getCurrent().navigate(RegistrierenView.class);
            } else if (event.getSelectedTab() == anmeldenTab) {
                UI.getCurrent().navigate(AnmeldenView.class);
            }
        });

        return tabs;
    }


    private VerticalLayout createNameFields() {
        TextField firstNameField = new TextField("Vorname");
        TextField lastNameField = new TextField("Nachname");

        firstNameField.setWidth(FIELD_WIDTH);
        firstNameField.setRequired(true);

        firstNameField.setI18n(new TextField.TextFieldI18n()
                .setRequiredErrorMessage("Vorname ist erforderlich"));

        lastNameField.setWidth(FIELD_WIDTH);
        lastNameField.setRequired(true);

        lastNameField.setI18n(new TextField.TextFieldI18n()
                .setRequiredErrorMessage("Nachname ist erforderlich"));

        VerticalLayout nameLayout = new VerticalLayout(firstNameField, lastNameField);
        nameLayout.getStyle().set("padding-top", "40px");
        nameLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        nameLayout.setSpacing(true);
        return nameLayout;
    }

    private Paragraph createAGBText() {
        String agbText = "Wenn du ein Konto erstellst, stimmst du unseren Nutzungsbedingungen zu. <br>" +
                "Du erfährst in unserer Datenschutzerklärung, wie wir deine Daten verarbeiten.";
        Paragraph agbParagraph = new Paragraph(agbText);
        agbParagraph.getElement().setProperty("innerHTML", agbText);
        agbParagraph.getStyle().set("font-size", "small");
        agbParagraph.getStyle().set("text-align", "center");
        agbParagraph.getStyle().set("margin-top", "30px");
        return agbParagraph;
    }

    private TextField createUsernameField() {
        TextField usernameField = new TextField("Username");
        usernameField.setWidth("min-content");
        return usernameField;
    }

    private EmailField createEmailField() {
        EmailField emailField = new EmailField("E-Mail Adresse");
        emailField.setWidth(FIELD_WIDTH);

        binder.forField(emailField)
              .asRequired("E-Mail Adresse ist erforderlich")
              .withValidator(email -> email.contains("@") && email.contains("."),
                      "Ungueltige E-Mail Adresse")
              .bind(User::getEmailAddress, User::setEmailAddress);

        return emailField;
    }

    private PasswordField createPasswordField(String label) {
        PasswordField passwordField = new PasswordField(label);
        passwordField.setWidth(FIELD_WIDTH);
        passwordField.setClearButtonVisible(true);

        binder.forField(passwordField)
              .asRequired("Passwort ist erforderlich")
              .withValidator(enocdedPassword -> enocdedPassword.length() >= 8, "Passwort muss mindestens 8 Zeichen lang sein")
              .bind(User::getEncodedPassword, User::setEncodedPassword);
        return passwordField;
    }

    private HorizontalLayout createButtonLayout() {
        Button cancelButton = createStyledButton("Abbrechen");
        cancelButton.setWidth("150px");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelButton.addClickListener(event -> UI.getCurrent().navigate(AnmeldenView.class));

        Button registerButton = createStyledButton("Registrieren");
        registerButton.setWidth("150px");
        registerButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        registerButton.addClickListener(event -> saveUser());

        HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton, registerButton);
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        buttonLayout.setWidthFull();
        buttonLayout.getStyle().set("margin-top", "15px");

        return buttonLayout;
    }


    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setWidth("150px");
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.getStyle().set("background-color", "black");
        button.getStyle().set("color", "white");


        button.addClickListener(event -> {
            if (activeButton != null) {
                // Setzt den vorherigen Button auf Schwarz zurück
                activeButton.getStyle().set("background-color", "black");
                activeButton.getStyle().set("color", "white");
                activeButton.getStyle().set("cursor", "pointer");
            }

            // Neuen Button aktiv setzen
            button.getStyle().set("background-color", "red");
            button.getStyle().set("color", "white");
            button.getStyle().set("cursor", "default");

            // Speichert den neuen aktiven Button
            activeButton = button;
        });

        return button;
    }

    private void clearFields() {
        nameLayout.getChildren().forEach(component -> {
            TextField field = (TextField) component;
            field.clear();
        });
        emailField.clear();
        passwordField.clear();
        confirmPasswordField.clear();

    }

    private void saveUser() {
        User user = new User();

        try {
            binder.writeBean(user);

            if (userService.emailExists(user.getEmailAddress())) {
                Notification notification = Notification.show("E-Mail Adresse ist bereits registriert!",
                        3000, Notification.Position.TOP_END);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.getElement().getThemeList().add("custom-transparent"); // Eigene Klasse hinzufügen
                notification.open();

                clearFields();
                return;
            }

            if (passwordField.getValue().equals(confirmPasswordField.getValue())) {
                TextField firstNameField = (TextField) nameLayout.getComponentAt(0);
                TextField lastNameField = (TextField) nameLayout.getComponentAt(1);

                user.setFirstName(Firstname.of(firstNameField.getValue()));
                user.setLastName(Lastname.of(lastNameField.getValue()));
                user.setEncodedPassword(passwordEncoder.encode(passwordField.getValue()));
                user.setUserRole(UserRole.CUSTOMER);
                userService.saveUser(user);

                Notification notification = new Notification("Registrierung erfolgreich!", 3000, Notification.Position.TOP_END);
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                notification.getElement().getThemeList().add("custom-transparent");
                notification.open();

                // Weiterleitung nach Verzögerung
                UI.getCurrent().setPollInterval(500); // Poll-Intervall setzen, falls nötig
                new Thread(() -> {
                    try {
                        Thread.sleep(3000); // 3 Sekunden warten
                        UI.getCurrent().access(() -> UI.getCurrent().navigate("/shop"));
                    } catch (InterruptedException ignored) {
                    }
                }).start();

            } else {
                // Wenn die Passwörter nicht übereinstimmen, passiert jetzt einfach nichts.
                clearFields(); // Falls du möchtest, dass die Felder trotzdem geleert werden.
            }
        } catch (Exception e) {
            Notification notification = Notification.show("Registrierung fehlgeschlagen!",
                    3000, Notification.Position.TOP_END);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.getElement().getThemeList().add("custom-transparent");
            notification.open();
        }
    }

    //----------------Footer-------------------------
    private void initializeFooterLayout() {
        getContent().setSizeFull();
        getContent().getStyle().set("display", "flex");
        getContent().getStyle().set("flex-direction", "column");
        getContent().getStyle().set("min-height", "100vh");
        getContent().getStyle().set("flex-grow", "1");

//        getContent().getStyle().set("width", "100vw");
//        getContent().getStyle().set("max-width", "100%");

        getContent().getStyle().set("margin", "0");
        getContent().getStyle().set("padding", "0");

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
        layoutColumn2.getStyle().set("flex-grow", "1");
        layoutColumn2.setSpacing(true);
        layoutColumn2.setPadding(true);
        layoutColumn2.setAlignItems(FlexComponent.Alignment.CENTER);

        getContent().add(layoutRow);
        getContent().add(layoutColumn2);

        // Spacer sorgt für flexiblen Abstand
        Div spacer = new Div();
//        spacer.getStyle().set("flex-grow", "1");
//        spacer.getStyle().set("min-height", "auto");
//        getContent().add(spacer);
        spacer.getStyle().set("min-height", "20px");
        // Verhindere, dass sich das Layout ausdehnt
        getContent().getStyle().set("flex-grow", "0");
        // Min-height von 100vh auf auto setzen
        getContent().getStyle().set("min-height", "auto");

        // Footer-Layout
        HorizontalLayout layoutRow2 = new HorizontalLayout();
        layoutRow2.setWidth("100vw");  // Nutze 100vw, um die gesamte Bildschirmbreite zu nutzen
        layoutRow2.setMaxWidth("100%");  // Falls 100vw zu groß wäre, begrenze es auf 100%
        layoutRow2.setHeight("min-content");
        layoutRow2.getStyle().set("background-color", "black");
        layoutRow2.getStyle().set("color", "white");
        layoutRow2.getStyle().set("margin-top", "auto");

        // Verhindert, dass es sich verkleinert oder verschoben wird
        layoutRow2.getStyle().set("flex-shrink", "0");
        layoutRow2.getStyle().set("align-self", "stretch");

        // Entferne zusätzliches Padding oder Margin
        layoutRow2.getStyle().set("padding", "0");
        layoutRow2.getStyle().set("margin", "0");

        // Falls Flexbox-Probleme auftreten, icherstellen, dass der Container richtig gestreckt ist
        layoutRow2.getStyle().set("box-sizing", "border-box");

        VerticalLayout lowerPanel = new VerticalLayout();

        // Footer-Inhalt
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
        downList.add(homeItem, shopItem, artistLink);
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
