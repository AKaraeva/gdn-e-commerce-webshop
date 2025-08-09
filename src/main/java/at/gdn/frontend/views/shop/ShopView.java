package at.gdn.frontend.views.shop;

import at.gdn.backend.entities.Product;
import at.gdn.backend.service.ProductService;
import at.gdn.frontend.StringLoch;
import at.gdn.frontend.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@PageTitle("Shop | Gesindel der Nacht")
@Route(value = "/shop", layout = MainLayout.class)
@Menu(order = 2)
@AnonymousAllowed
public class ShopView extends Composite<VerticalLayout> {

    private final ProductService productService;
    private final List<Product> products;
    private OrderedList productContainer = new OrderedList();
    private Button activeButton = null;


    public ShopView(@Autowired ProductService productService) {
        this.productService = productService;
        this.products = productService.getAllProducts();
        constructUI();
        loadProducts();  // Holt die Produkte aus der Datenbank
        initializeFooterLayout();

    }

    private void constructUI() {
        // Produktcontainer für die Karten
        productContainer.addClassNames(
                LumoUtility.Gap.MEDIUM,
                LumoUtility.Display.GRID,
                LumoUtility.ListStyleType.NONE,
                LumoUtility.Margin.NONE,
                LumoUtility.Padding.NONE
        );

        productContainer.getStyle().set("display", "grid");
        productContainer.getStyle().set("row-gap", "5px");
        productContainer.getStyle().set("column-gap", "5px");
        productContainer.getStyle().set("grid-template-columns", "repeat(auto-fit, minmax(230px, 1fr))");
        productContainer.getStyle().set("margin-left", "0.2%");
        productContainer.getStyle().set("margin-right", "0.2%");
        productContainer.getStyle().set("padding", "20px");

        // Wrapper für bessere Steuerung der Linksausrichtung
        Div containerWrapper = new Div(productContainer);
        containerWrapper.getStyle().set("width", "94%");
        containerWrapper.getStyle().set("margin", "0 auto");

        // Layout mit Buttons und Produkten
        VerticalLayout shopLayout = new VerticalLayout(createButtonLayout(), containerWrapper);
        shopLayout.setWidthFull();
        shopLayout.setPadding(false);
        shopLayout.setSpacing(false);

        getContent().add(shopLayout);
    }

    //loads all products from DB
    private void loadProducts() {

        if (this.products.isEmpty()) {
            Notification.show("Keine Produkte verfügbar.", 3000, Notification.Position.MIDDLE);
            return;
        }

        productContainer.removeAll();

        //Für Kartenansicht
        for (Product product : this.products) {
            productContainer.add(new ShopItemCard(product));
        }
    }

    //------------------ Filter-Buttons----------------------
    private HorizontalLayout createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true); // Abstand zwischen Buttons
        buttonLayout.setPadding(true); // Oben/unten Abstand
        buttonLayout.setWidthFull(); // Nimmt die volle Breite des Elternelements ein
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START); // Links ausrichten
        buttonLayout.getStyle().set("max-width", "94%"); // Begrenzung auf die Breite des Grids
        buttonLayout.getStyle().set("margin", "0 auto"); // Zentriert den Button-Container mit dem Grid
        buttonLayout.getStyle().set("margin-left", "2%"); // Verschiebt nach links
        buttonLayout.getStyle().set("margin-right", "2%");
        buttonLayout.getStyle().set("padding", "10px");

        // Buttons erstellen
        Button allButton = createStyledButton("Alle");
        allButton.addClickListener(event -> {
            loadProducts();
        });

        Button wareableButton = createStyledButton("Kleidung");
        wareableButton.addClickListener(event -> {
            filterByCategory("CLOTHING");
        });

        Button extrasButton = createStyledButton("Extras");
        extrasButton.addClickListener(event -> {
            filterByCategory("EXTRAS");
        });

        Button accessoiresButton = createStyledButton("Accessoires");
        accessoiresButton.addClickListener(event -> {
            filterByCategory("Accessoires");
        });

        activeButton = allButton;
        allButton.getStyle().set("background-color", "red");
        allButton.getStyle().set("cursor", "default");


        // Buttons zum Layout hinzufügen
        buttonLayout.add(allButton, wareableButton,accessoiresButton, extrasButton);

        return buttonLayout;
    }

    //---------- Methode Farbe nach klicken des Buttons ändern-------
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

    //---------- Methode zum Filtern nach Kategorie-------
    private void filterByCategory(String category) {

        productContainer.removeAll();

        List<Product> filteredProducts;

        filteredProducts = this.productService.getByCategory(category);

        if (filteredProducts == null || filteredProducts.isEmpty()) {
            productContainer.removeAll();
            Span noProductsText = new Span("Keine Produkte verfügbar.");
            noProductsText.getStyle().set("font-size", "20px");
            noProductsText.getStyle().set("color", "black");
            noProductsText.getStyle().set("text-align", "center");
            noProductsText.getStyle().set("width", "100%");
            productContainer.add(noProductsText);
        }

        //für Kartenansicht
        for (Product product : filteredProducts) {
            productContainer.add(new ShopItemCard(product));
        }
    }

    private Button createEditButton(String text) {

        Button button = new Button(text);
        button.setWidth("200px");
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.getStyle().set("background-color", "red");
        button.getStyle().set("color", "white");

        button.addClickListener(event -> {
//            UI.getCurrent().navigate(AdminView.class);
        });


        return button;
    }

    //------------------ Footer----------------------
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
        layoutColumn2.getStyle().set("flex-grow", "1");
        layoutColumn2.setSpacing(true);
        layoutColumn2.setPadding(true);
        layoutColumn2.setAlignItems(FlexComponent.Alignment.CENTER);

        getContent().add(layoutRow);
        getContent().add(layoutColumn2);

        // Spacer sorgt für flexiblen Abstand
        Div spacer = new Div();
        spacer.getStyle().set("flex-grow", "1");
        getContent().add(spacer);

        // Footer
        HorizontalLayout layoutRow2 = new HorizontalLayout();
        layoutRow2.setWidth("100%");
        layoutRow2.setHeight("min-content");
        layoutRow2.getStyle().set("background-color", "black");
        layoutRow2.getStyle().set("color", "white");
        layoutRow2.getStyle().set("margin-top", "auto");

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

        downList.add(new ListItem("Shop"));

        // Künstler-Link
        Anchor artistLink = new Anchor("/künstler", "Künstler");
        artistLink.getStyle().set("color", "white");
        artistLink.getStyle().set("text-decoration", "none");
        ListItem artistItem = new ListItem(artistLink);
        artistItem.getStyle().set("cursor", "pointer");

        // Items zur Liste hinzufügen
        downList.add(homeItem, artistItem);
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
