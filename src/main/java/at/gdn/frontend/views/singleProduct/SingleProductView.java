package at.gdn.frontend.views.singleProduct;

import at.gdn.backend.entities.Product;
import at.gdn.backend.service.ProductService;
import at.gdn.backend.valueobjects.ProductImage;
import at.gdn.frontend.StringLoch;
import at.gdn.frontend.views.MainLayout;
import at.gdn.frontend.views.home.HomeView;
import at.gdn.frontend.views.shop.ShopView;
import at.gdn.frontend.views.shop.ShoppingCartView;
import at.gdn.backend.entities.ProductInventory;
import at.gdn.backend.entities.ShoppingCartNew;
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
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@PageTitle("Produkteansicht  | Gesindel der Nacht")
@Route(value = "product/:productId", layout = MainLayout.class)
//@Menu(order = 1, icon = LineAwesomeIconUrl.PENCIL_ALT_SOLID)
@AnonymousAllowed
public class SingleProductView extends Composite<VerticalLayout> implements BeforeEnterObserver {

    private final ProductService productService;
    private final ShoppingCartNew shoppingCart;

    private final Map<String, String> colorImageMap = new HashMap<>(); // Farb-Bild-Zuordnung

   /* private final String[] images = {
            "/images/blau.jpg",
            "/images/schwarz.jpg",
            "/images/t-shirt_rot.jpg",
            "/images/weiss.jpg"
    };*/

    public SingleProductView(@Autowired ProductService productService,
                             @Autowired ShoppingCartNew shoppingCart) {
        this.productService = productService;
        this.shoppingCart = shoppingCart;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        Optional<Long> productId = beforeEnterEvent.getRouteParameters().get("productId").map(Long::parseLong);
        if (productId.isPresent()) {
            productService.getProductById(productId.get()).ifPresent(product -> {

                initializeColorImageMap(); // Farben mit Bildern verknüpfen
                getContent().setSpacing(true);
                getContent().setPadding(true);
                getContent().setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

                VerticalLayout routVertical = createRout();
                HorizontalLayout imageScroller = createImage(product.getProductImage());
                VerticalLayout productInfo = createProduct(product);
                HorizontalLayout productLayout = new HorizontalLayout(imageScroller, productInfo);
                Hr hr = new Hr();
                Tabs tabs = createTab();
                //TODO: Beschreibung des Produkts in DB speichern
                Div content = new Div(new Span(
                        "Materialzusammensetzung: 90% Baumwolle, 10% Polyester\n" +
                                "Pflegehinweise: Maschinenwäsche\n" +
                                "Verschlusstyp: Pull-On\n" +
                                "Kragenform: Crew-Ausschnitt"
                ));

                content.getStyle()
                       .set("line-height", "1.6")
                       .set("font-size", "16px")
                       .set("color", "#333")
                       .set("margin-top", "15px");

                getContent().add(routVertical, productLayout, hr, tabs, content);

                initializeFooterLayout();
            });
        } else {
            //TODO: Übelegen ob es überhaupt möglich ist
            Notification.show("Kein Produkt ausgewählt!", 3000, Notification.Position.MIDDLE);
        }
    }

    private int currentIndex = 0;

    private Image imageDisplay;

   /* public SingleProductView() {
        initializeColorImageMap(); // Farben mit Bildern verknüpfen

        getContent().setSpacing(true);
        getContent().setPadding(true);
        getContent().setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        VerticalLayout routVertical = createRout();
        HorizontalLayout imageScroller = createImage();
        VerticalLayout productInfo = createProduct();
        HorizontalLayout productLayout = new HorizontalLayout(imageScroller, productInfo);
        Hr hr = new Hr();
        Tabs tabs = createTab();
        Div content = new Div(new Span("Längere Beschreibung fehlt - LG Ertu"));

        getContent().add(routVertical, productLayout, hr, tabs, content);
        initializeFooterLayout();
    }*/

    // Farben mit passenden Bildern verknüpfen
    private void initializeColorImageMap() {
        colorImageMap.put("blue", "/images/blau.jpg");
        colorImageMap.put("black", "/images/schwarz.jpg");
        colorImageMap.put("red", "/images/t-shirt_rot.jpg");
        colorImageMap.put("white", "/images/weiss.jpg");
    }

    private VerticalLayout createRout() {
        HorizontalLayout rout = new HorizontalLayout();
        VerticalLayout routVertical = new VerticalLayout();
//        RouterLink homeLink = new RouterLink("Home", HomeView.class);
//        RouterLink shopLink = new RouterLink("Shop", ShopView.class);
//        rout.add(homeLink, new Span(">"), shopLink);
        Span breadcrumb = new Span(rout);
        routVertical.add(breadcrumb);
        routVertical.setHorizontalComponentAlignment(FlexComponent.Alignment.START);
        routVertical.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.START);
        return routVertical;
    }

    //    private HorizontalLayout createImage(List<ProductImage> images){
//        imageDisplay = new Image(images.get(currentIndex).getImageName(), "Produktbild");
//        imageDisplay.setWidth("200px");
//        imageDisplay.setHeight("200px");
//
//        Button leftButton = new Button("<", e -> showPreviousImage(images));
//        Button rightButton = new Button(">", e -> showNextImage(images));
//
//        HorizontalLayout imageScroller = new HorizontalLayout(leftButton, imageDisplay, rightButton);
//        imageScroller.setWidth("400px");
//        imageScroller.setHeight("400px");
//        imageScroller.getStyle().remove("border");
//        imageScroller.setAlignItems(FlexComponent.Alignment.CENTER);
//        return imageScroller;
//    }
    private HorizontalLayout createImage(List<ProductImage> images) {
        imageDisplay = new Image(images.get(currentIndex).getImageName(), "Produktbild");
        imageDisplay.setWidth("200px");
        imageDisplay.setHeight("200px");

        HorizontalLayout imageLayout = new HorizontalLayout(imageDisplay);
        imageLayout.setWidth("400px");
        imageLayout.setHeight("400px");
        imageLayout.getStyle().remove("border");
        imageLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        return imageLayout;
    }


    private VerticalLayout createProduct(Product product) {
        VerticalLayout productInfo = new VerticalLayout();
        Span productName = new Span(new H2(product.getProductName()));
        Span productPrice = new Span("€ " + String.format("%.2f", product.getProductPrice().doubleValue()));
        Span rating = new Span("⭐⭐⭐⭐⭐ | Coustomer review");
        Span productDescription = new Span(product.getProductDescription());

        VerticalLayout sizeOptionMain = new VerticalLayout();
        Span size = new Span("Size");
        HorizontalLayout sizeOptions = new HorizontalLayout(createSize("S"), createSize("M"), createSize("L"), createSize("XL"));
        sizeOptionMain.add(size, sizeOptions);
        sizeOptionMain.setPadding(false);

        if (!product.getProductName().toLowerCase().contains("shirt")) {
            sizeOptionMain.setVisible(false);
        }

        VerticalLayout colorOptionMain = new VerticalLayout();
        Span color = new Span("Color");
        HorizontalLayout colorOptions = createColorOptions(); // Hier wird die neue Methode createColorOptions genutzt

        colorOptionMain.add(color, colorOptions);
        colorOptionMain.setPadding(false);

        IntegerField quantity = new IntegerField();
        quantity.setValue(1);
        quantity.setStepButtonsVisible(true);
        quantity.setMin(0);
        quantity.setMax(9);
        quantity.setWidth("90px");

        Button warenkorb = new Button("In den Warenkorb");
        warenkorb.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_CONTRAST);
        warenkorb.getStyle().set("border", "2px solid #878787");
        warenkorb.addClickListener(event -> {
            product.setProductQuantity(quantity.getValue().intValue());
            shoppingCart.addProduct(product);
            Notification.show("Produkt wurde zum Warenkorb hinzugefügt.", 3000, Notification.Position.MIDDLE);
        });

        HorizontalLayout purchaseSection = new HorizontalLayout(quantity, warenkorb);

        productInfo.add(productName, productPrice, rating, productDescription, sizeOptionMain, colorOptionMain, purchaseSection, new RouterLink("Zum Warenkorb", ShoppingCartView.class));
        return productInfo;
    }

    private HorizontalLayout createColorOptions() {
        HorizontalLayout colorOptions = new HorizontalLayout();

        for (Map.Entry<String, String> entry : colorImageMap.entrySet()) {
            String color = entry.getKey();
            String imagePath = entry.getValue();

            Div circle = new Div();
            circle.setWidth("20px");
            circle.setHeight("20px");
            circle.getStyle().set("background", color).set("border-radius", "50%");
            circle.getStyle().set("cursor", "pointer"); // Klickbare Farbkreise

            // Falls die Farbe weiß ist, eine schwarze Umrandung hinzufügen
            if (color.equals("white")) {
                circle.getStyle().set("border", "1px solid black");
            }

            // Beim Klicken ändert sich das Bild
            circle.addClickListener(event -> updateImageByColor(imagePath));

            colorOptions.add(circle);
        }
        return colorOptions;
    }

    private void updateImageByColor(String imagePath) {
        imageDisplay.setSrc(imagePath);
    }

    private Tabs createTab() {
        Tab descriptionTab = new Tab("Beschreibung");
        Tab reviewTab = new Tab("Rezensionen (5)");
        Tabs tabs = new Tabs(descriptionTab, reviewTab);
        return tabs;
    }

    private Button createSize(String name) {
        Button button = new Button(name);
        button.addThemeVariants(ButtonVariant.LUMO_WARNING);
        button.getStyle().setBackgroundColor("#eacfbc");
        return button;
    }

    private void showPreviousImage(List<ProductImage> images) {
        if (currentIndex > 0) {
            currentIndex--;
        } else {
            currentIndex = images.size() - 1;
        }
        imageDisplay.setSrc(images.get(currentIndex).getImageName());
    }

    private void showNextImage(List<ProductImage> images) {
        if (currentIndex < images.size() - 1) {
            currentIndex++;
        } else {
            currentIndex = 0;
        }
        imageDisplay.setSrc(images.get(currentIndex).getImageName());
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
