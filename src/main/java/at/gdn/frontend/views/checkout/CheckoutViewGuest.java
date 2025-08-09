package at.gdn.frontend.views.checkout;

import at.gdn.backend.entities.Product;
import at.gdn.backend.service.UserService;
import at.gdn.frontend.StringLoch;
import at.gdn.frontend.views.MainLayout;
import at.gdn.backend.entities.ShoppingCartNew;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility.*;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;


@PageTitle("Checkout für Gäste | Gesindel der Nacht")
@Route(value = "shoppingcart/checkoutGuest", layout = MainLayout.class)
@Menu(order = 5, icon = LineAwesomeIconUrl.CART_PLUS_SOLID)
@AnonymousAllowed



public class CheckoutViewGuest extends Composite<VerticalLayout> {
    private static final Set<String> states = new LinkedHashSet<>();
    private static final Set<String> countries = new LinkedHashSet<>();
    private final Span totalPriceLabel = new Span("Gesamt: 0.00 €");

    private final UserService userService;
    private final ShoppingCartNew shoppingCart;

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


    static {
        states.addAll(Arrays.asList(
                "Aargau", "Appenzell Ausserrhoden", "Appenzell Innerrhoden",
                "Baden-Württemberg", "Basel-Landschaft", "Basel-Stadt", "Bayern", "Berlin", "Bern", "Brandenburg", "Bremen", "Burgenland",
                "Freiburg",
                "Genf", "Glarus", "Graubünden",
                "Hamburg", "Hessen",
                "Jura",
                "Kärnten",
                "Luzern",
                "Mecklenburg-Vorpommern",
                "Neuenburg", "Niedersachsen", "Niederösterreich", "Nordrhein-Westfalen", "Nidwalden",
                "Oberösterreich", "Obwalden",
                "Rheinland-Pfalz",
                "Saarland", "Sachsen", "Sachsen-Anhalt", "Salzburg", "Schaffhausen", "Schleswig-Holstein", "Schwyz", "Solothurn", "St. Gallen", "Steiermark",
                "Tessin", "Thurgau", "Thüringen", "Tirol",
                "Uri",
                "Vorarlberg",
                "Waadt", "Wallis", "Wien",
                "Zug", "Zürich"
        ));

        countries.addAll(Arrays.asList("Austria", "Germany", "Switzerland"));
    }

    public CheckoutViewGuest(@Autowired UserService userService, @Autowired ShoppingCartNew shoppingCart) {
        this.userService = userService;
        this.shoppingCart = shoppingCart;

        getStyle().setHeight("1000px");

        addClassNames("checkout-view", AlignItems.START, JustifyContent.START);
        addClassNames(Display.FLEX, FlexDirection.COLUMN, Height.FULL);

        Main content = new Main();
        content.addClassNames(Display.GRID, Gap.XLARGE, AlignItems.START,
                JustifyContent.CENTER, MaxWidth.SCREEN_MEDIUM,
                Margin.Horizontal.AUTO, Padding.Bottom.LARGE, Padding.Horizontal.LARGE);

        content.add(createCheckoutForm());

        getContent().add(getImageContainer());

        getContent().add(content);
        initializeFooterLayout();

    }

    private Component createCheckoutForm() {
        Section checkoutForm = new Section();
        checkoutForm.addClassNames(Display.FLEX, FlexDirection.COLUMN, Flex.GROW);

        Paragraph note = new Paragraph("");
        note.addClassNames(Margin.Bottom.XLARGE, Margin.Top.NONE, TextColor.SECONDARY);
        checkoutForm.add(note);

        checkoutForm.add(createLayout());

        return checkoutForm;

    }

    //---------- BANNER-Image ----------
    private Div getImageContainer() {
        Div container = new Div();
        container.setSizeFull();
        container.getStyle().set("position", "relative"); // Wichtig für fixierte Position des Textes

        // Banner-Bild (Cover Image)
        Image coverImage = new Image(StringLoch.BANNER_IMAGE, "Banner");
        coverImage.setWidth("100%");
        coverImage.getStyle()
                  .set("height", "30vh")
                  .set("object-fit", "cover")
                  .set("display", "block");

        // Fixierter Schriftzug "CHECKOUT" (Unverändert, nur fest im Bild)
        Div textOverlay = new Div();
        textOverlay.setText("CHECKOUT");
        textOverlay.getStyle()
                   .set("position", "absolute") // Fixiert im Bild
                   .set("top", "50%")  // Beibehaltung der Originalposition von oberen Rand (40%)
                   .set("left", "50%")  // Beibehaltung der Originalposition
                   .set("transform", "translate(-50%, -50%)")  // Perfekt zentriert
                   .set("color", "white")  // Originalfarbe
                   .set("letter-spacing", "1px")  // Original-Stil
                   .set("font-size", "calc(1.5vw + 1.5vh + 1.5vmin)");  // Originalgröße bleibt


        container.add(coverImage, textOverlay);
        return container;
    }


    private Div createLayout() { // Neues übergeordnetes Div-Element
        Div layout = new Div();
        layout.addClassNames(Display.FLEX); // Flexbox aktivieren

        Aside aside = createAside();

        // Hauptinhalt (z.B. eine Div) - Hier müssen Sie Ihre Hauptinhalte einfügen
        Div mainContent = new Div();
        mainContent.setText("Hauptinhalt hier"); // Beispieltext

        layout.add(shippingAddress(), aside); // Reihenfolge beachten: Hauptinhalt zuerst, dann Aside


        return layout;
    }

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    // Eingabeformular für die Lieferadresse des Benutzers
    private Section shippingAddress() {
        Section personalDetails = new Section();
        personalDetails.addClassNames(Display.FLEX,
                FlexDirection.COLUMN, Margin.Bottom.XLARGE, Margin.Top.MEDIUM, Margin.Right.XLARGE);

        H3 header = new H3("Lieferadresse");
        header.addClassNames(Margin.Bottom.MEDIUM, Margin.Top.SMALL);

        // Username-Feld
        TextField username = new TextField("Username");
        username.setRequiredIndicatorVisible(true);
        username.setPattern("[\\p{L} \\-]+");
        username.setErrorMessage("Bitte einen gültigen Namen eingeben.");
        username.addClassNames(Margin.Bottom.SMALL);

        // Vorname-Feld
        TextField firstname = new TextField("Vorname");
        firstname.setRequiredIndicatorVisible(true);
        firstname.setPattern("[\\p{L} \\-]+");
        firstname.setErrorMessage("Vorname darf nicht leer sein.");
        firstname.addClassNames(Margin.Bottom.SMALL);

        // Nachname-Feld
        TextField lastname = new TextField("Nachname");
        lastname.setRequiredIndicatorVisible(true);
        lastname.setPattern("[\\p{L} \\-]+");
        lastname.setErrorMessage("Nachname darf nicht leer sein.");
        lastname.addClassNames(Margin.Bottom.SMALL);

        // Land-Auswahl
        ComboBox<String> countrySelect = new ComboBox<>("Land");
        countrySelect.setRequiredIndicatorVisible(true);
        countrySelect.setItems(countries);
        countrySelect.setErrorMessage("Bitte ein Land auswählen.");
        countrySelect.addClassNames(Margin.Bottom.SMALL);

        // Adresse-Feld
        TextArea address = new TextArea("Straße | Gasse");
        address.setMaxLength(200);
        address.setRequiredIndicatorVisible(true);
        address.setErrorMessage("Straße darf nicht leer sein.");
        address.addClassNames(Margin.Bottom.SMALL);

        // Hausnummer
        TextField houseNumber = new TextField("Hausnummer");
        houseNumber.setRequiredIndicatorVisible(true);
        houseNumber.setPattern("[\\d\\s\\-/]+");
        houseNumber.setErrorMessage("Hausnummer darf nicht leer sein.");
        houseNumber.addClassNames(Margin.Bottom.SMALL);

        TextField doorNumber = new TextField("Türnummer");
        doorNumber.setRequiredIndicatorVisible(true);
        doorNumber.setPattern("[\\d\\s\\-/]+");
        doorNumber.addClassNames(Margin.Bottom.SMALL);
        doorNumber.setErrorMessage("Türnummer darf nicht leer sein.");
        doorNumber.addClassNames(Margin.Bottom.SMALL);

        // PLZ und Stadt
        TextField postalCode = new TextField("PLZ | ZIP Code");
        postalCode.setRequiredIndicatorVisible(true);
        postalCode.setPattern("[\\d \\p{L}]*");
        postalCode.setErrorMessage("Postleitzahl darf nicht leer sein.");
        postalCode.addClassNames(Margin.Bottom.SMALL);

        TextField city = new TextField("Stadt");
        city.setRequiredIndicatorVisible(true);
        city.setErrorMessage("Stadt darf nicht leer sein.");
        city.addClassNames(Flex.GROW, Margin.Bottom.SMALL);

        // Email-Feld
        EmailField email = new EmailField("E-Mail-Adressse");
        email.setRequiredIndicatorVisible(true);
        email.setErrorMessage("Bitte eine gültige E-Mail-Adresse eingeben.");
        email.addClassNames(Margin.Bottom.SMALL);

        // Telefonnummer
        TextField phone = new TextField("Telefonnummer");
        phone.setRequiredIndicatorVisible(true);
        phone.setPattern("[\\d \\-\\+]+");
        phone.setErrorMessage("Bitte eine gültige Telefonnummer eingeben.");
        phone.addClassNames(Margin.Bottom.SMALL);

        // Layout für PLZ und Stadt
        Div subSection = new Div();
        subSection.addClassNames(Display.FLEX, FlexWrap.WRAP, Gap.MEDIUM);
        subSection.add(postalCode, city);

        // Bestätigungsbutton mit Validierung und Feld-Reset
        Button submitButton = new Button("Weiter", event -> {
            if (validateFields(username, firstname, lastname, countrySelect, address, houseNumber, postalCode, city, email, phone)) {
                Notification.show("Formular erfolgreich validiert!");

                // Alle Felder nach erfolgreicher Validierung leeren
                clearFields(username, firstname, lastname, countrySelect, address, houseNumber, postalCode, city, email, phone);
            } else {
                Notification.show("Bitte alle Pflichtfelder korrekt ausfüllen.", 3000, Notification.Position.MIDDLE);
            }
        });


        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        submitButton.addClassNames(Margin.Top.MEDIUM);

        personalDetails.add(header, username, firstname, lastname, countrySelect, address, houseNumber, subSection, email, phone, submitButton);
        return personalDetails;
    }

// TODO: überlegen wie die Validierung der Eingaben erfolgen soll
//   -----   Methode zur Überprüfung der Eingaben in den Feldern.

    private boolean validateFields(HasValue<?, String>... fields) {
        boolean isValid = true;
        for (HasValue<?, String> field : fields) {
            if (field.isEmpty()) {
                isValid = false;
            }
        }
        return isValid;
    }

//   -----   Methode zum Zurücksetzen der Felder auf den Standardwert Leerfelder.

    private void clearFields(HasValue<?, String>... fields) {
        for (HasValue<?, String> field : fields) {
            field.clear(); // Setzt das Feld auf den Standardwert zurück
        }
    }

    //    ----------------Buttons in Bestellung-------------------------
    private Footer payment() {
        Footer payment = new Footer();
        payment.addClassNames(
                Display.FLEX,
                FlexDirection.COLUMN,
                AlignItems.START,
                JustifyContent.START,
                Margin.Vertical.MEDIUM
        );

        Button pay = new Button("Pay securely", new Icon(VaadinIcon.LOCK));
        pay.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        pay.getStyle().set("min-width", "180px");
        pay.getStyle().set("margin-top", "30px");

        pay.addClickListener(event -> {
            Notification notification = new Notification();
            notification.setPosition(Notification.Position.MIDDLE);
            notification.setDuration(5000); // 5 Sekunden sichtbar

            // HTML-Inhalt mit Icon, Text und eigenem Styling
            Div content = new Div();
            content.getElement().setProperty("innerHTML",
                    "<div style='background-color: red; color: white; padding: 30px; font-size: 24px; border-radius: 15px; display: flex; align-items: center; width: 400px; text-align: center;'>"
                            + "<span style='margin-right: 15px; font-size: 30px;'>⚠️</span>" // Größeres Icon
                            + "<b>Testphase, zahlen noch nicht möglich</b>"
                            + "</div>"
            );

            notification.add(content);
            notification.open();
        });



        Button cancel = new Button("Cancel order", new Icon(VaadinIcon.TRASH));
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancel.getStyle().set("min-width", "180px");
        cancel.getStyle().set("background-color", "gray");
        cancel.getStyle().set("color", "white");
        cancel.getStyle().set("margin-top", "10px");

//        cancel.addClickListener(event -> clearShoppingCart()); // Hier die Methode aufrufen



        payment.add(pay, cancel);

        return payment;
    }

//    ----------------Aside || Besetllungs-Feld-------------------------
    private Aside createAside() {
        Aside aside = new Aside();
        aside.addClassNames(Background.CONTRAST_5, BoxSizing.CONTENT, Padding.LARGE, BorderRadius.MEDIUM,
                Position.RELATIVE, Margin.Left.LARGE, Margin.Right.LARGE, Margin.Top.LARGE);
        Header headerSection = new Header();
        headerSection.addClassNames(Display.FLEX, AlignItems.CENTER, JustifyContent.BETWEEN, Margin.Bottom.MEDIUM);
        H3 header = new H3("Bestellung");
        header.addClassNames(Margin.NONE);
        Button edit = new Button("Edit");
        edit.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        headerSection.add(header, edit);

        UnorderedList ul = new UnorderedList();
        ul.addClassNames(ListStyleType.NONE, Margin.NONE, Padding.NONE, Display.FLEX, FlexDirection.COLUMN, Gap.MEDIUM);

        //ul.add(createListItem("T-Shirt", "blue", "25.00 €"));
        for (Product product : shoppingCart.getProducts()) {
            ul.add(createListItem(product.getProductName(), "x" + product.getProductQuantity(), "€ " + product.getProductPrice()));
        }

        // Total Price Section
        totalPriceLabel.setText("Gesamt: € " + String.format("%.2f", shoppingCart.getTotalPrice()));
        totalPriceLabel.addClassNames(FontSize.XLARGE, TextColor.PRIMARY);

        aside.add(headerSection, ul, totalPriceLabel);
        aside.getStyle().setWidth("250px").setHeight("100%").setMaxWidth("300px");

        aside.add(payment());
        return aside;
    }


    private ListItem createListItem(String primary, String secondary, String price) {
        ListItem item = new ListItem();
        item.addClassNames(Display.FLEX, JustifyContent.BETWEEN);

        Div subSection = new Div();
        subSection.addClassNames(Display.FLEX, FlexDirection.COLUMN);

        subSection.add(new Span(primary));
        Span secondarySpan = new Span(secondary);
        secondarySpan.addClassNames(FontSize.SMALL, TextColor.SECONDARY);
        subSection.add(secondarySpan);

        Span priceSpan = new Span(price);

        item.add(subSection, priceSpan);
        return item;

    }


    //    ----------------Footer-------------------------

    private void initializeFooterLayout() {
        getContent().setSizeFull();
        getContent().getStyle().set("display", "flex");
        getContent().getStyle().set("flex-direction", "column");
        getContent().getStyle().set("min-height", "100vh");
        getContent().getStyle().set("flex-grow", "1");
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
        spacer.getStyle().set("flex-grow", "1");
        spacer.getStyle().set("min-height", "auto");
        getContent().add(spacer);
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
