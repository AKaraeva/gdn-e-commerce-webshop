package at.gdn.frontend.views.checkout;

import at.gdn.backend.entities.OrderItems;
import at.gdn.backend.entities.Product;
import at.gdn.backend.entities.User;
import at.gdn.backend.persistence.repository.OrderItemsRepository;
import at.gdn.backend.richtypes.Firstname;
import at.gdn.backend.service.OrderService;
import at.gdn.backend.service.UserService;
import at.gdn.backend.entities.ShoppingCartNew;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.theme.lumo.LumoUtility;

import at.gdn.frontend.StringLoch;
import at.gdn.frontend.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
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
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.vaadin.lineawesome.LineAwesomeIconUrl;
import com.vaadin.flow.theme.lumo.LumoUtility.FlexDirection;
import com.vaadin.flow.theme.lumo.LumoUtility.FlexWrap;
import com.vaadin.flow.theme.lumo.LumoUtility.FontSize;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.Height;
import com.vaadin.flow.theme.lumo.LumoUtility.JustifyContent;
import com.vaadin.flow.theme.lumo.LumoUtility.ListStyleType;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.MaxWidth;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vaadin.flow.theme.lumo.LumoUtility.Position;
import com.vaadin.flow.theme.lumo.LumoUtility.TextColor;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.Background;
import com.vaadin.flow.theme.lumo.LumoUtility.BorderRadius;
import com.vaadin.flow.theme.lumo.LumoUtility.BoxSizing;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.Flex;


@PageTitle("Checkout | Gesindel der Nacht")
@Route(value = "shoppingcart/checkout", layout = MainLayout.class)
@Menu(order = 5, icon = LineAwesomeIconUrl.CART_PLUS_SOLID)
@AnonymousAllowed

public class CheckoutView extends Composite<VerticalLayout> {
    private static final Set<String> states = new LinkedHashSet<>();
    private static final Set<String> countries = new LinkedHashSet<>();
    private final Span totalPriceLabel = new Span("Gesamt: 0.00 €");
    @Autowired
    private final UserService userService;
    private final ShoppingCartNew shoppingCart;
    //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    @Autowired
    private final OrderService orderService;

    @Autowired
    private final OrderItemsRepository orderItemsRepository;


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

    public CheckoutView(@Autowired UserService userService,
                        @Autowired OrderService orderService,
                        @Autowired OrderItemsRepository orderItemsRepository,
                        @Autowired ShoppingCartNew shoppingCart) {
        this.userService = userService;
        this.orderService = orderService;
        this.orderItemsRepository = orderItemsRepository;
        this.shoppingCart = shoppingCart;

        getStyle().setHeight("1000px");

        addClassNames("checkout-view", LumoUtility.AlignItems.START, LumoUtility.JustifyContent.START);
        addClassNames(Display.FLEX, FlexDirection.COLUMN, Height.FULL);

        Main content = new Main();
        content.addClassNames(Display.GRID, Gap.XLARGE, AlignItems.START
                , JustifyContent.CENTER, MaxWidth.SCREEN_MEDIUM,
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
        Image coverImage = new Image(StringLoch.COVER_IMAGE, "Banner");
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


    private Section shippingAddress() {
        Section personalDetails = new Section();
        personalDetails.addClassNames(Display.FLEX,
                FlexDirection.COLUMN, Margin.Bottom.XLARGE, Margin.Top.MEDIUM, Margin.Right.XLARGE);


        H3 header = new H3("Lieferadresse");
        header.addClassNames(Margin.Bottom.MEDIUM, Margin.Top.SMALL);

        TextField username = new TextField("Username");
        username.setRequiredIndicatorVisible(true);
        username.setPattern("[\\p{L} \\-]+");
        username.addClassNames(Margin.Bottom.SMALL);
        if (userService.findUserNameByEmail(getAuthentication().getName()).isEmpty() == false) {
            username.setValue(userService.findUserNameByEmail(getAuthentication().getName()).get().username());
        }

        TextField firstname = new TextField("Vorname");
        firstname.setRequiredIndicatorVisible(true);
        firstname.setPattern("[\\p{L} \\-]+");
        firstname.addClassNames(Margin.Bottom.SMALL);
        firstname.setValue(userService.findFirstNameByEmail(getAuthentication().getName()).get().firstName());

        TextField lastname = new TextField("Nachname");
        lastname.setRequiredIndicatorVisible(true);
        lastname.setPattern("[\\p{L} \\-]+");
        lastname.addClassNames(Margin.Bottom.SMALL);
        lastname.setValue(userService.findLastNameByEmail(getAuthentication().getName()).get().lastName());


        ComboBox<String> countrySelect = new ComboBox<>("Land");
        countrySelect.setRequiredIndicatorVisible(true);
        countrySelect.addClassNames(Margin.Bottom.SMALL);
//        countrySelect.setValue(userService.findCountryByEmail(getAuthentication().getName()).get().getCityName());


        TextArea address = new TextArea("Straße | Gasse");
        address.setMaxLength(200);
        address.setRequiredIndicatorVisible(true);
        address.addClassNames(Margin.Bottom.SMALL);
        if (userService.findStreetByEmail(getAuthentication().getName()).isEmpty() == false) {
            address.setValue(userService.findStreetByEmail(getAuthentication().getName()).get().toString());
        }


        TextField houseNumber = new TextField("Hausnummer");
        houseNumber.setRequiredIndicatorVisible(true);
        houseNumber.setPattern("[\\p{L} \\-]+");
        houseNumber.addClassNames(Margin.Bottom.SMALL);
        if (userService.findHouseNumberByEmail(getAuthentication().getName()).isEmpty() == false) {
            houseNumber.setValue(userService.findHouseNumberByEmail(getAuthentication().getName()).get().toString());
        }


        TextField doorNumber = new TextField("Türnummer");
        doorNumber.setRequiredIndicatorVisible(true);
        doorNumber.setPattern("[\\p{L} \\-]+");
        doorNumber.addClassNames(Margin.Bottom.SMALL);
        if (userService.findDoorNumberByEmail(getAuthentication().getName()).isEmpty() == false) {
            doorNumber.setValue(userService.findDoorNumberByEmail(getAuthentication().getName()).get().toString());
        }


        //PLZ und Bundesland
        Div subSection = new Div();
        subSection.addClassNames(Display.FLEX, FlexWrap.WRAP, Gap.MEDIUM);

        TextField postalCode = new TextField("PLZ | ZIP Code");
        postalCode.setRequiredIndicatorVisible(true);
        postalCode.setPattern("[\\d \\p{L}]*");
        postalCode.addClassNames(Margin.Bottom.SMALL);
        if (userService.findPostalCodeByEmail(getAuthentication().getName()).isEmpty() == false) {
            postalCode.setValue(userService.findPostalCodeByEmail(getAuthentication().getName()).get().toString());
        }


        TextField city = new TextField("Stadt");
        city.setRequiredIndicatorVisible(true);
        city.addClassNames(Flex.GROW, Margin.Bottom.SMALL);
        if (userService.findCityByEmail(getAuthentication().getName()).isEmpty() == false) {
            city.setValue(userService.findCityByEmail(getAuthentication().getName()).get().toString());
        }

        subSection.add(postalCode, city);


        ComboBox<String> stateSelect = new ComboBox<>("State");
        stateSelect.setRequiredIndicatorVisible(true);

        stateSelect.setItems(states);
        stateSelect.setVisible(false);
        countrySelect.setItems(countries);

        EmailField email = new EmailField("E-Mail-Adressse");
        email.setRequiredIndicatorVisible(true);
        email.addClassNames(Margin.Bottom.SMALL);
        email.setValue(getAuthentication().getName());


        TextField phone = new TextField("Telefonnummer");
        phone.setRequiredIndicatorVisible(true);
        phone.setPattern("[\\d \\-\\+]+");
        phone.addClassNames(Margin.Bottom.SMALL);
        if (userService.findCityByEmail(getAuthentication().getName()).isEmpty() == false) {
            phone.setValue(

                    (userService.findAreaCodeByEmail(getAuthentication().getName()).get().toString()) + " "
                            + (userService.findCountryCodeByEmail(getAuthentication().getName()).get().toString()) + " "
                            + (userService.findSerialNumberByEmail(getAuthentication().getName()).get()));
        }


        personalDetails.add(header, username, firstname, lastname, countrySelect, address, houseNumber, doorNumber, postalCode, city, email, phone);
        return personalDetails;
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

            // Speichert die Bestellung in der Datenbank
            List<OrderItems> orderItems = shoppingCart.getProducts().stream()
                    .map(product -> OrderItems.builder()
                            .product(product)
                            .quantity(product.getProductQuantity())
                            .price(product.getProductPrice())
                            .build())
                    .toList();

            orderItemsRepository.saveAll(orderItems);

            orderService.createOrder(
                    userService.findUserByEmail(getAuthentication(). getName()).get().getId(),
                    orderItems
            );

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

        payment.add(pay, cancel);

        return payment;
    }

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

        for (Product product : shoppingCart.getProducts()) {
            ul.add(createListItem(product.getProductName(), "x" + product.getProductQuantity(), "€ " + product.getProductPrice()));
        }

        // Total Price Section
        totalPriceLabel.setText("Gesamt: € " + String.format("%.2f", shoppingCart.getTotalPrice()));
        totalPriceLabel.addClassNames(FontSize.XLARGE, TextColor.PRIMARY);

        //ul.add(createListItem("T-Shirt", "blue", "25.00 €"));

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
