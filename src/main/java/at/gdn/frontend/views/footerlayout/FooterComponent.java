package at.gdn.frontend.views.footerlayout;

import at.gdn.frontend.StringLoch;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

public class FooterComponent extends VerticalLayout {

    public FooterComponent() {
        // Grundlayout für den Footer (flexibel, passt sich der Seite an)
        setSizeFull();
        getStyle().set("display", "flex");
        getStyle().set("flex-direction", "column");
        getStyle().set("min-height", "100vh");
        getStyle().set("flex-grow", "1");
        setSpacing(false);
        setPadding(false);

        // Spacer, damit der Footer immer unten bleibt
        Div spacer = new Div();
        spacer.getStyle().set("flex-grow", "1");
        add(spacer);

        // Footer-Bereich
        HorizontalLayout footerContainer = new HorizontalLayout();
        footerContainer.setWidth("100%");
        footerContainer.setHeight("min-content");
        footerContainer.getStyle().set("background-color", "black");
        footerContainer.getStyle().set("color", "white");
        footerContainer.getStyle().set("margin-top", "auto");
        footerContainer.setPadding(true);

        VerticalLayout lowerPanel = new VerticalLayout();
        lowerPanel.setWidthFull();
        lowerPanel.setAlignItems(Alignment.CENTER);

        // Footer-Inhalt
        HorizontalLayout footerContent = new HorizontalLayout();
        footerContent.setWidth("100%");
        footerContent.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        footerContent.setPadding(true);

        // ---- Bereich 1: Name & Impressum ----
        VerticalLayout lower1 = new VerticalLayout();
        H4 lowerName = new H4("Gesindel der Nacht");
        Text lowerText = new Text("IMPRESSUM");
        lowerName.getStyle().set("color", StringLoch.WHITE);
        lower1.add(lowerName, lowerText);
        lower1.getStyle().set("color", StringLoch.WHITE);

        // ---- Bereich 2: Links ----
        VerticalLayout lower2 = new VerticalLayout(new Text("Links"));
        UnorderedList downList = new UnorderedList();
        downList.add(new ListItem(new Anchor("/", "Home")));
        downList.add(new ListItem(new Anchor("/künstler", "Künstler")));
        downList.getStyle().set("color", StringLoch.WHITE);
        downList.getStyle().set("padding", "0");
        downList.getStyle().set("list-style-type", "none");
        lower2.add(downList);
        lower2.getStyle().set("color", StringLoch.RED);

        // ---- Bereich 3: Hilfe ----
        VerticalLayout lower3 = new VerticalLayout(new Text("Hilfe"));
        UnorderedList downList2 = new UnorderedList();
        downList2.add(new ListItem("Datenschutz"));
        downList2.add(new ListItem("FAQ"));
        downList2.add(new ListItem("Nutzungsbedingungen"));
        downList2.add(new ListItem("Urheberrechtsrichtlinie"));
        lower3.add(downList2);
        lower3.getStyle().set("color", StringLoch.RED);

        // ---- Bereich 4: Newsletter ----
        VerticalLayout lower4 = new VerticalLayout(new Text("Newsletter"));
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

        lower4.add(input, subscribeText);
        lower4.getStyle().set("color", "#F42929");

        // Footer-Elemente in das Hauptlayout hinzufügen
        footerContent.add(lower1, lower2, lower3, lower4);
        footerContent.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        Hr hr = new Hr();
        hr.getStyle().set("border-top", "2px solid white");

        // Copyright-Bereich
        HorizontalLayout lowerDown = new HorizontalLayout();
        lowerDown.getStyle().set("color", "white");

        Span year = new Span("© Copy Right 2025 - GDN | All rights reserved");
        year.getStyle().set("font-size", "14px");
        year.getStyle().set("margin-left", "20px");

        lowerDown.add(year);
        lowerPanel.add(footerContent, hr, lowerDown);
        footerContainer.add(lowerPanel);

        // Footer zur Hauptklasse hinzufügen
        add(footerContainer);
    }
}
