package at.gdn.frontend.views.shop;

import at.gdn.backend.entities.Product;
import at.gdn.backend.security.AuthenticatedUser;
import at.gdn.frontend.StringLoch;
import at.gdn.frontend.views.MainLayout;
import at.gdn.frontend.views.anmelden.AnmeldeCheckout;
import at.gdn.frontend.views.anmelden.AnmeldenView;
//import at.gdn.backend.entities.ProductInventory;
import at.gdn.backend.entities.ShoppingCartNew;
import at.gdn.frontend.views.checkout.CheckoutView;
import at.gdn.frontend.views.checkout.CheckoutViewGuest;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.IntStream;

@Route(value = "/shopping_cart", layout = MainLayout.class)
@PageTitle("Warenkorb | Gesindel der Nacht")
@AnonymousAllowed
//@Menu(order = 4)

public class ShoppingCartView extends Composite<VerticalLayout> {
    private AuthenticatedUser authenticatedUser;
    private final ShoppingCartNew shoppingCart;
    private final Grid <Product> cartGrid = new Grid<>(Product .class);
    private final Span totalLabel = new Span("Gesamt: 0.00 €");

    public ShoppingCartView(@Autowired ShoppingCartNew shoppingCart, @Autowired AuthenticatedUser authenticatedUser) {
        this.shoppingCart = shoppingCart;
        this.authenticatedUser = authenticatedUser;

        // Debug: Check if session is persisting
        System.out.println("ShoppingCartNew instance: " + shoppingCart.hashCode());
        System.out.println("Cart size at view initialization: " + shoppingCart.getProducts().size());
        setupLayout();
    }

    private void setupLayout() {
        getContent().setWidth("100%");
        getContent().setSpacing(true);
        getContent().setPadding(true);

        // Banner hinzufügen
        getContent().add(getImageContainer());

        // Warenkorb-Bereich
        HorizontalLayout cartLayout = createCartLayout();
        getContent().add(cartLayout);

        // Footer
        initializeFooterLayout();
    }

    private HorizontalLayout createCartLayout() {
        HorizontalLayout cartLayout = new HorizontalLayout();
        cartLayout.getStyle()
                .set("margin-top", "3vh")
                .set("width", "100%")
                .set("padding", "10px")
                .set("display", "flex")
                .set("flex-wrap", "wrap") // Ermöglicht Zeilenumbrüche für kleinere Bildschirme
                .set("justify-content", "center"); // Zentriert die Inhalte dynamisch

        VerticalLayout productList = createProductList();
        VerticalLayout summaryLayout = createSummaryLayout();


        cartLayout.add(productList, summaryLayout);
        return cartLayout;
    }


    private VerticalLayout createProductList() {
        VerticalLayout productList = new VerticalLayout();
        productList.getStyle()
                .set("width", "65%") // Verhindert, dass es zu schmal wird
                .set("max-width", "950px") // Maximale Breite für große Bildschirme
                .set("padding", "10px")
                .set("border-radius", "8px")
                .set("background", "white")
                .set("color", "black")
                .set("margin", "auto")
                .set("margin-right", "20px");

        // Debugging: Ensure products exist in session cart
        System.out.println("Fetching products from cart... Size: " + shoppingCart.getProducts().size());


        HorizontalLayout headerBar = createHeaderBar();

        //Liste alle Produkte in Warenkorb
        VerticalLayout productContainer = new VerticalLayout();
        for(Product product : shoppingCart.getProducts()){
            HorizontalLayout productRow = createProductRow(product);
            productContainer.add(productRow);
        }

        //HorizontalLayout productRow = createProductRow();
        //productList.add(headerBar, productRow);
        productList.add(headerBar, productContainer);
        return productList;
    }


    private HorizontalLayout createHeaderBar() {
        HorizontalLayout headerBar = new HorizontalLayout();
        headerBar.getStyle()
                .set("background-color", "red")
                .set("color", "white")
                .set("padding", "10px")
                .set("font-weight", "bold")
                .set("width", "100%")
                .set("max-width", "950px") // Maximalbreite für Kontrolle
                .set("border-radius", "8px")
                .set("display", "grid")
                .set("grid-template-columns", "auto auto auto auto") // Flexibles Grid
                .set("text-align", "center");

        Span imageHeader = new Span(""); // Leere Spalte für das Bild
        Span productHeader = new Span("Produkt");
        Span priceHeader = new Span("Preis");
        Span quantityHeader = new Span("Anzahl");

        headerBar.add(imageHeader, productHeader, priceHeader, quantityHeader);
        return headerBar;
    }


    private HorizontalLayout createProductRow(Product product) {
        HorizontalLayout productRow = new HorizontalLayout();
        productRow.getStyle()
                .set("width", "100%")
                .set("max-width", "950px") // Passt sich an die Header-Breite an
                .set("border-bottom", "1px solid #ccc")
                .set("align-items", "center")
                .set("padding", "10px")
                .set("display", "grid")
                .set("grid-template-columns", "auto auto auto auto") // Flexible Spaltenbreite
                .set("text-align", "center");

        VerticalLayout imageColumn = new VerticalLayout();
        imageColumn.setAlignItems(FlexComponent.Alignment.CENTER);
        Image productImage = new Image(product.getProductImage().get(0).getImageName(), "T-Shirt");
        productImage.getStyle().set("width", "50px").set("height", "50px");
        imageColumn.add(productImage);

        VerticalLayout productColumn = new VerticalLayout();
        productColumn.setAlignItems(FlexComponent.Alignment.CENTER);
        Span productName = new Span(product.getProductName());
        productName.getStyle().set("font-weight", "bold");
        productColumn.add(productName);

        VerticalLayout priceColumn = new VerticalLayout();
        priceColumn.setAlignItems(FlexComponent.Alignment.CENTER);
        Span productPrice = new Span("€ " + String.format("%.2f", product.getProductPrice().doubleValue()));
        productPrice.getStyle().set("color", "#333");
        priceColumn.add(productPrice);

        VerticalLayout quantityColumn = new VerticalLayout();
        quantityColumn.setAlignItems(FlexComponent.Alignment.CENTER);
        ComboBox<Integer> quantityComboBox = new ComboBox<>();
        quantityComboBox.setItems(IntStream.rangeClosed(0, 10).boxed().toList());
        quantityComboBox.setValue(product.getProductQuantity());
        quantityComboBox.setWidth("60px");
        quantityComboBox.addValueChangeListener(event -> {
            product.setProductQuantity(event.getValue());
            updateCart();
        });
        quantityColumn.add(quantityComboBox);

        productRow.add(imageColumn, productColumn, priceColumn, quantityColumn);
        return productRow;
    }

    private void updateCart() {
        totalLabel.setText("Gesamt: " + shoppingCart.getTotalPrice() + " €");
    }

    private VerticalLayout createSummaryLayout() {
        VerticalLayout summaryLayout = new VerticalLayout();
        summaryLayout.setWidth("90%"); // Flexibel, aber nicht zu breit
        summaryLayout.getStyle()
                .set("max-width", "300px") // Verhindert, dass es zu groß wird
                .set("background", "#1a1a1a")
                .set("color", "white")
                .setBorderRadius("6px")
                .set("padding", "15px")
                .set("height", "auto")
                .set("overflow-y", "auto")
                .set("margin", "auto"); // Immer mittig

        summaryLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        Span title = new Span("WARENKORB");
        title.getStyle()
                .set("font-size", "1.3rem")
                .set("font-weight", "bold")
                .set("margin-bottom", "8px")
                .set("text-decoration", "underline");

        totalLabel.setText("Gesamt: " + shoppingCart.getTotalPrice() + " €");

        Button checkoutButton = new Button("Check Out");
        checkoutButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        checkoutButton.getStyle()
                .set("margin-top", "10px");

        if (authenticatedUser != null && authenticatedUser.getAuthenticatedUser() != null) {

            checkoutButton.addClickListener(event -> UI.getCurrent().navigate(CheckoutView.class));
        }
        else{
            checkoutButton.addClickListener(event -> UI.getCurrent().navigate(AnmeldeCheckout.class));
        }

        summaryLayout.add(title, totalLabel, checkoutButton);
        return summaryLayout;
    }


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

        // Fixierter Schriftzug "WARENKORB" (Unverändert, nur fest im Bild)
        Div textOverlay = new Div();
        textOverlay.setText("WARENKORB");
        textOverlay.getStyle()
                .set("position", "absolute") // Fixiert im Bild
                .set("top", "50%")  // Beibehaltung der Originalposition von oberen Rand (40%)
                .set("left", "50%")  // Beibehaltung der Originalposition
                .set("transform", "translate(-50%, -50%)")  // Perfekt zentriert
                .set("color", "white")  // Originalfarbe
                .set("letter-spacing", "1px")  // Original-Stil
                .set("font-size", "calc(1.5vw + 1.5vh + 1.5vmin)");  // Originalgröße bleibt

        // Beide Elemente in den Container setzen
        container.add(coverImage, textOverlay);
        return container;
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
//        spacer.getStyle().set("flex-grow", "1");
//        getContent().add(spacer);
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