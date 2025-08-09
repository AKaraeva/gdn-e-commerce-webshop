package at.gdn.frontend.views.produktbearbeiten;

import at.gdn.frontend.StringLoch;
import at.gdn.frontend.views.MainLayout;
import at.gdn.frontend.views.anmelden.AnmeldenView;
import at.gdn.frontend.views.home.HomeView;
import at.gdn.frontend.views.shop.ShopView;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.concurrent.LinkedTransferQueue;

@PageTitle("Produkt bearbeiten")
@Route(value="/produktbearbeiten", layout = MainLayout.class)
@Menu(order = 1, icon = LineAwesomeIconUrl.PENCIL_ALT_SOLID)
@AnonymousAllowed // muss am Ende wieder weg
public class TestProduktBearbeiten extends Composite<VerticalLayout> {

    private Button activeButton = null;

    public TestProduktBearbeiten() {

        getContent().setSpacing(true);
        getContent().setPadding(true);
        getContent().setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        getContent().add(createMainContent());

        initializeFooterLayout();

    }

    private HorizontalLayout createMainContent() {
        // Bild + Kurze Info
        Image productImage = new Image("/images/t-shirt_blau.jpg", "T-Shirt mit Gesindel Logo");
        productImage.setWidth("200px");
        H3 productTitle = new H3("T-Shirt");
        Span productText = new Span("Mit Gesindel Logo");
        Span productPriceLabel = new Span("€25,00");

        VerticalLayout imageSection = new VerticalLayout(productImage, productTitle, productText, productPriceLabel);
        imageSection.setAlignItems(FlexComponent.Alignment.CENTER);
        imageSection.setSpacing(true);

        // Formular für die Bearbeitung
        FormLayout formLayout = new FormLayout();

        // Felder
        com.vaadin.flow.component.textfield.TextField bezeichnungField =
                new com.vaadin.flow.component.textfield.TextField("Bezeichnung");
        bezeichnungField.setValue("T-Shirt");

        com.vaadin.flow.component.textfield.TextField groesseField =
                new com.vaadin.flow.component.textfield.TextField("Größe");
        groesseField.setValue("M");

        com.vaadin.flow.component.textfield.TextField farbeField =
                new com.vaadin.flow.component.textfield.TextField("Farbe");
        farbeField.setValue("Blau");

        com.vaadin.flow.component.textfield.TextField materialField =
                new com.vaadin.flow.component.textfield.TextField("Beschaffenheit");
        materialField.setValue("100% Wolle");

        com.vaadin.flow.component.textfield.TextField preisField =
                new com.vaadin.flow.component.textfield.TextField("Preis");
        preisField.setValue("25,00");

        // Buttons
        HorizontalLayout buttonLayout = createButtonLayout();
        getContent().add(buttonLayout);

        // Zusammenbauen des Formulars
        formLayout.add(
                bezeichnungField,
                groesseField,
                farbeField,
                materialField,
                preisField,
                buttonLayout
        );

        VerticalLayout formSection = new VerticalLayout(formLayout);
        formSection.setPadding(true);

        // Haupt-Layout
        HorizontalLayout mainContent = new HorizontalLayout(imageSection, formSection);
        mainContent.setWidthFull();
        mainContent.setAlignItems(FlexComponent.Alignment.START);
        mainContent.setPadding(true);
        mainContent.setSpacing(true);

        return mainContent;
    }

    // buttons
    private HorizontalLayout createButtonLayout() {
        Button cancelButton = createStyledButton("abbrechen");
        cancelButton.setWidth("150px");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelButton.addClickListener(event -> UI.getCurrent().navigate(AnmeldenView.class));

        Button registerButton = createStyledButton("speichern");
        registerButton.setWidth("150px");
        registerButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        //registerButton.addClickListener(event -> saveUser());

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

    //Footer------------------------------------------------
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

        // Footer-Layout
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

        downList.add(new ListItem("Künstler"));

        // Items zur Liste hinzufügen
        downList.add(homeItem, shopItem);
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

        subscribeText.addClickListener(event -> {
            String email = input.getValue();
            if (email.isEmpty() || !email.contains("@")) {
                Notification.show("Bitte eine gültige E-Mail-Adresse eingeben.", 3000, Notification.Position.MIDDLE);
            } else {
                Notification.show("Danke für die Anmeldung, " + email + "!", 3000, Notification.Position.MIDDLE);
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
