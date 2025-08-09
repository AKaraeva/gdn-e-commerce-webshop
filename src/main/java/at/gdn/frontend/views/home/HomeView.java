package at.gdn.frontend.views.home;

import at.gdn.frontend.StringLoch;
import at.gdn.frontend.views.MainLayout;
import at.gdn.frontend.views.shop.ShopView;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
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
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Home | Gesindel der Nacht")
@Route(value = "", layout = MainLayout.class)
@Menu(order = 0, icon = LineAwesomeIconUrl.HOME_SOLID)
@AnonymousAllowed
public class HomeView extends Composite<VerticalLayout> {

    public HomeView() {
        HorizontalLayout layoutRow = new HorizontalLayout();
        VerticalLayout layoutColumn2 = new VerticalLayout();
        HorizontalLayout layoutRow2 = new HorizontalLayout();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setSpacing(false);
        getContent().setPadding(false);

        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.setHeight("min-content");
        layoutRow.setSpacing(false);
        layoutRow.setPadding(false);
        layoutRow.setSizeFull();

        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
        layoutColumn2.setSpacing(true);
        layoutColumn2.setPadding(true);

        layoutRow2.addClassName(Gap.MEDIUM);
        layoutRow2.setWidth("100%");
        layoutRow2.setHeight("min-content");
        layoutRow2.getStyle().set("color", StringLoch.WHITE);
        layoutRow2.getStyle().set("background-color",StringLoch.BLACK);
        layoutColumn2.setAlignItems(FlexComponent.Alignment.CENTER);

        //Button row für Auswahlansicht
        H4 middleText = new H4("Schau dir die Auswahl an");
        HorizontalLayout middleLayout = getButtonRow();
        layoutColumn2.add(middleText,middleLayout);
//        middleLayout.getStyle().set("cursor", "pointer");

        //container für Footer
        Div container = getImageContainer();
        layoutRow.add(container);

        //Lower panel
        getContent().add(layoutRow);
        getContent().add(layoutColumn2);
        getContent().add(layoutRow2);
        initializeFooterLayout(); // Footer aufruf
    }

    private VerticalLayout getButton(String name,String img){
        VerticalLayout getLayout = new VerticalLayout();
        Button getButton = new Button();
        getButton.getStyle().set("background-color","transparent");
        getButton.getStyle().set("border","4px solid black");
        Image buttonImage = new Image(img,"Shop");
        getButton.setWidth("200px");
        getButton.setHeight("340px");
        buttonImage.setWidth("180px");
        buttonImage.setHeight("180px");
        Text nameButton = new Text(name);
        //nameButton.getStyle().set("color","black");
        getButton.setIcon(buttonImage);
        //TODO: Button click event to navigate to filterByCategory method
        getButton.addClickListener(e->{
            UI.getCurrent().navigate(ShopView.class);
        });
        getLayout.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        getLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        getLayout.add(getButton,nameButton);
        return getLayout;
    }

    private Div getImageContainer(){
        Div container = new Div();
        container.getStyle().set("position", "relative");
        container.setSizeFull();

        Image coverImage = new Image(StringLoch.COVER_IMAGE,"landing");
//        coverImage.setWidth("100%");
//        coverImage.setHeight("100vh");
        coverImage.setWidth("100%");
        coverImage.getStyle().set("object-fit", "cover");
        coverImage.getStyle().set("object-position", "center");
        coverImage.getStyle().set("height", "auto");
        coverImage.getStyle().set("min-height", "400px"); // Mindesthöhe, kannst du anpassen
        coverImage.getStyle().set("max-height", "100vh");
        coverImage.addClassName("cover-image");

        Div square = new Div();
        square.setMaxWidth("500px");
        square.setMaxHeight("300px");
        square.getStyle().set("background-color", StringLoch.BLACK);
        square.getStyle().set("position", "absolute");
        square.getStyle().set("bottom", "5%");
        square.getStyle().set("right", "5%");

        Text squareTop = new Text("Jetzt neu!");
        H1 squareMiddle = new H1("Entdecke unsere neue Kollektion");
        Button squareButton = new Button("Buy Now");
        squareMiddle.getStyle().set("color",StringLoch.RED);
        squareButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_ERROR);
        squareButton.setWidth("180px");
        squareButton.setHeight("70px");
        squareButton.getStyle().set("cursor", "pointer");
        squareButton.addClickListener(e->{
            UI.getCurrent().navigate(ShopView.class);
        });

        VerticalLayout squareLayout = new VerticalLayout();
        squareLayout.getStyle().set("color",StringLoch.WHITE);
        squareLayout.add(squareTop,squareMiddle,squareButton);
        square.add(squareLayout);

        container.add(coverImage,square);

        return container;
    }

    private HorizontalLayout getButtonRow(){
        HorizontalLayout middleLayout = new HorizontalLayout();
        VerticalLayout shirt = getButton("Kleidung",StringLoch.SHIRT_IMAGE);
        shirt.getStyle().set("cursor", "pointer");
        VerticalLayout accesories = getButton("Accessoires",StringLoch.ACCESSORIES_IMAGE);
        accesories.getStyle().set("cursor", "pointer");
        VerticalLayout pen = getButton("Extra",StringLoch.PEN_IMAGE);
        pen.getStyle().set("cursor", "pointer");
        middleLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        middleLayout.add(shirt,accesories,pen);
        return middleLayout;
    }


    //----------Footer--------------------
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

//        spacer.getStyle().set("min-height", "20px");

// Verhindere, dass sich das Layout ausdehnt
//        getContent().getStyle().set("flex-grow", "0");

// Min-height von 100vh auf auto setzen
//        getContent().getStyle().set("min-height", "auto");

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
        downList.add(new ListItem("Home"));

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
        downList.add(shopItem, artistItem);
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

        //TODO: Implementierung des Newsletter-Abonnements mit mehr Abfrage damit Kunde keinen alzugroßen Blödsinn eingeben kann
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

    private Text getLink(String more){
        return new Text(more);
    }

}
