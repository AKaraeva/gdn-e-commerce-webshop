package at.gdn.frontend.views.userManagement;

import at.gdn.backend.entities.User;
import at.gdn.backend.enums.UserRole;
import at.gdn.backend.persistence.repository.UserRepository;
import at.gdn.backend.richtypes.Firstname;
import at.gdn.backend.richtypes.Lastname;
import at.gdn.backend.service.UserManagementService1;
import at.gdn.frontend.StringLoch;
import at.gdn.frontend.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import jakarta.annotation.security.RolesAllowed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.vaadin.lineawesome.LineAwesomeIcon;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.security.authorization.AuthorityAuthorizationManager.hasRole;


@Slf4j
@PageTitle("Userliste | Gesindel der Nacht")
@Route(value = "management/userlist", layout = MainLayout.class)
@Menu(order = 5, icon = LineAwesomeIconUrl.USERS_SOLID)
@RolesAllowed(value = {"ROLE_ADMIN"})

//@AnonymousAllowed
public class UserManagementGridView extends Composite<VerticalLayout> {


    private final UserManagementService1 userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Grid<User> userGrid = new Grid<>(User.class, false);

    @Autowired
    public UserManagementGridView(UserManagementService1 userService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

        init(); // Grid-Initialisierung
        initializeFooterLayout(); // Footer-Layout
    }

    // ------------- Initialisierung -----------------
    private void init() {
        // Erstelle das Grid mit einem MULTI-Selection-Modus
        userGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        HorizontalLayout toolbar = new HorizontalLayout();

        // Erstelle die "Alle auswählen"-Checkbox
        Checkbox selectAllCheckbox = new Checkbox();
        selectAllCheckbox.addClassName("product-inventory-grid-view-checkbox-1");


        // Füge den ValueChangeListener für die Checkbox hinzu
        selectAllCheckbox.addValueChangeListener(event -> {
            if (userGrid != null && userGrid.getSelectionModel() instanceof GridMultiSelectionModel<User> multiSelectionModel) {
                // Prüfen, ob Checkbox aktiviert wurde
                if (Boolean.TRUE.equals(event.getValue())) {
                    // Multi-Select über den DataProvider
                    userGrid.getDataProvider()
                            .fetch(new Query<>()) // Geladene Daten im Grid abrufen
                            .forEach(multiSelectionModel::select); // Alle sichtbaren Einträge auswählen
                } else {
                    multiSelectionModel.deselectAll(); // Selektion aufheben
                }
            } else {
                log.error("Multi-SelectionModel ist nicht verfügbar. Bitte MultiSelection aktivieren.");
            }
        });

        Button multiDelete = new Button(LineAwesomeIcon.TRASH_SOLID.create());
        multiDelete.addClickListener(event -> {
            Set<User> selectedUsers = userGrid.getSelectedItems();
            if (!selectedUsers.isEmpty()) {
                userRepository.deleteAll(selectedUsers); // Verwendung von deleteAll() für das gesamte Set
                Notification.show(selectedUsers.size() + " User gelöscht!");
                load(); // Tabelle nach dem Löschen neu laden
            } else {
                Notification.show("Keine User ausgewählt."); // Hinweis für den Benutzer, falls keine Auswahl vorliegt
            }
        });
        multiDelete.setEnabled(false); // Standardmäßig deaktiviert
        multiDelete.getStyle().set("color", "red"); // Nur rotes Icon für den Mülleimer in der Header-Spalte
        multiDelete.getStyle().set("border", "none"); // Ohne Rand für einen einfachen Icon-Style
        multiDelete.getStyle().set("cursor", "pointer"); // Zeige den Zeiger-Cursor an

        //MULTISELECT AKTIVIEREN (MULTISELECT FÜR LÖSCHEN)
        userGrid.addSelectionListener(new SelectionListener<Grid<User>, User>() {
            @Override
            public void selectionChange(SelectionEvent<Grid<User>, User> event) {
                Set<User> userSet = event.getAllSelectedItems();
                // Aktivieren/Deaktivieren des Buttons abhängig von der Tabellenauswahl
                multiDelete.setEnabled((userSet != null) && (!userSet.isEmpty()));
            }
        });


        //User hinzufügen   Button
        Button neuerUser = new Button("User erstellen");
        neuerUser.getStyle().set("background-color", "green");
        neuerUser.getStyle().set("color", "white");
        neuerUser.getStyle().set("border-radius", "5px");
        neuerUser.getStyle().set("cursor", "pointer");


        // Klick-Listener für den Button
            neuerUser.addClickListener(event -> {
            // Dialog erzeugen
            Dialog addUserDialog = new Dialog();
            addUserDialog.setHeaderTitle("User erstellen");
            addUserDialog.setWidth("500px");
            addUserDialog.setHeight("700px");

        TextField firstName = new TextField("Vorname");
        TextField lastName = new TextField("Nachname");
        TextField emailAddress = new TextField("E-Mail");

        TextField encodedPassword = new TextField("Password");
                // **Dropdown UserRoles**
                ComboBox<String> userRoles = new ComboBox<>("Rolle");
                userRoles.setAllowCustomValue(false); // Nur Auswahl aus der Liste erlauben
                userRoles.setWidth("100%");
                userRoles.setItems(
                        Arrays.stream(UserRole.values())
                                .map(UserRole::name)
                                .collect(Collectors.toList())
                );


                // Speichern-Button
                Button saveButton = new Button("Speichern", saveEvent -> {
                    // Validierung der Eingaben
                    if (firstName.isEmpty() || lastName.isEmpty() || emailAddress.isEmpty() || userRoles.isEmpty()  || encodedPassword.isEmpty()) {
                        Notification.show("Bitte alle Felder ausfüllen.", 3000, Notification.Position.MIDDLE);
                        return;
                    }

                    // Neuen User erstellen
                    User newUser = new User();
                    newUser.setFirstName(new Firstname(firstName.getValue()));
                    newUser.setLastName(new Lastname(lastName.getValue()));
                    newUser.setEmailAddress(emailAddress.getValue());
                    newUser.setEncodedPassword(passwordEncoder.encode(encodedPassword.getValue()));
                    newUser.setUserRole(UserRole.valueOf(userRoles.getValue()));


                    try {
                        // Neuen User speichern
                        userRepository.save(newUser);
                        // Tabelle aktualisieren
                        userGrid.getDataProvider().refreshAll();
                        // Erfolgsmeldung anzeigen
                        Notification.show("User erfolgreich hinzugefügt.", 3000, Notification.Position.MIDDLE);
                        // Dialog schließen
                        addUserDialog.close();

                    } catch (Exception e) {
                        // Fehler behandeln
                        e.printStackTrace();
                        Notification.show("Fehler beim Hinzufügen des Users: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
                    }
                });
                // Abbrechen-Button
                Button cancelButton = new Button("Abbrechen", cancelEvent -> {
                    // Dialog ohne Speicherung schließen
                    addUserDialog.close();
                });

                // Layout für Buttons (unten im Dialog)
                HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);

                // Layout für Eingabefelder und Buttons
                VerticalLayout contentLayout = new VerticalLayout(
                        firstName,
                        lastName,
                        emailAddress,
                        encodedPassword,
                        userRoles,
                        buttonLayout
                );
                contentLayout.setPadding(true);
                contentLayout.setSpacing(true);
                contentLayout.setAlignItems(FlexComponent.Alignment.STRETCH);

                // Dialog-Content hinzufügen und anzeigen
                addUserDialog.add(contentLayout);
                addUserDialog.setCloseOnEsc(true); // Schließen bei "Escape"
                addUserDialog.setCloseOnOutsideClick(false); // Klick außerhalb schließt nicht
                addUserDialog.open();
            });







        // Konfiguration der Spalten
        userGrid.addColumn(user -> user.getId().id()).setHeader("ID").setWidth("50px").setFlexGrow(1);
        userGrid.addColumn( user -> user.getFirstName().firstName()).setHeader("Vorname").setFlexGrow(1).setSortable(false);
        userGrid.addColumn(user -> user.getLastName().lastName()).setHeader("Nachname").setFlexGrow(1).setSortable(false);
        userGrid.addColumn(User::getEmailAddress).setHeader("E-Mail").setFlexGrow(2).setSortable(false);
        userGrid.addColumn(User::getEncodedPassword).setHeader("Encoded Password")
                .setFlexGrow(1)
                .setSortable(false)
                .setWidth("100px")
                .setAutoWidth(true);
        userGrid.addColumn(User::getUserRole).setHeader("Rolle").setFlexGrow(1).setSortable(false);




        userGrid.addComponentColumn(user -> {
            // Bearbeiten Button
            Button editButton = new Button("Edit");
            editButton.getStyle().set("background-color", "blue");
            editButton.getStyle().set("color", "white");
            editButton.getStyle().set("border-radius", "5px");
            editButton.getStyle().set("cursor", "pointer");

            // Klick-Listener für den Button
            editButton.addClickListener(event -> {
                // Dialog erzeugen
                Dialog editDialog = new Dialog();
                editDialog.setHeaderTitle("User bearbeiten");
                editDialog.setWidth("500px");
                editDialog.setHeight("700px");


                // Textfeld für den Firstname
                TextField firstnameField = new TextField("Vorname");
                firstnameField.setValue(user.getFirstName().firstName()); // Wert aus Grid setzen

                // Textfeld für den Lastname
                TextField lastnameField = new TextField("Nachname");
                lastnameField.setValue(user.getLastName().lastName()); // Wert aus Grid setzen

                // Textfeld für die EmailAdresse
                TextField emailField = new TextField("Nachname");
                emailField.setValue(user.getEmailAddress()); // Wert aus Grid setzen


                //Checkbox für Passwort ja/nein


                TextField encodedPassword = new TextField("Passwort");
                encodedPassword.setValue(user.getEncodedPassword());
                encodedPassword.setEnabled(false);


                Checkbox passwordCheckbox = new Checkbox("Passwort ändern");
                passwordCheckbox.addValueChangeListener(event1 -> {
                    if (Boolean.TRUE.equals(event1.getValue())) {
                        encodedPassword.setEnabled(true);

                    } else {
                        encodedPassword.setEnabled(false);
                    }
                });

                // Textfeld für die EmailAdresse
                 // Wert aus Grid setzen


                // **Dropdown UserRoles**
                ComboBox<String> userRoles = new ComboBox<>("Rolle");
                userRoles.setAllowCustomValue(false); // Nur Auswahl aus der Liste erlauben
                userRoles.setWidth("100%");
                userRoles.setItems(
                        Arrays.stream(UserRole.values())
                                .map(UserRole::name)
                                .collect(Collectors.toList())
                );
                userRoles.setValue(user.getUserRole().name());




                // Wert aus Grid setzen

                // Speichern-Button
                Button saveButton = new Button("Speichern", saveEvent -> {
                    // Neue Werte setzen


                    if (firstnameField.isEmpty() || lastnameField.isEmpty() || emailField.isEmpty() || userRoles.isEmpty()  || encodedPassword.isEmpty()) {
                        Notification.show("Bitte alle Felder ausfüllen.", 3000, Notification.Position.MIDDLE);
                        return;
                    }

                    user.setFirstName(Firstname.of(firstnameField.getValue()));
                    user.setLastName(Lastname.of(lastnameField.getValue()));
                    user.setEmailAddress(emailField.getValue());

                    if(passwordCheckbox.getValue() == true){
                        user.setEncodedPassword(passwordEncoder.encode(encodedPassword.getValue()));
                    }

                    user.setUserRole(UserRole.valueOf(userRoles.getValue()));


                    try {
                        // Änderungen speichern
                        userRepository.save(user);

                        // Tabelle aktualisieren

                        userGrid.getDataProvider().refreshAll();
                        userGrid.setItems(userRepository.findAll());
//                        UI.getCurrent().getPage().reload();


                        // Erfolgsmeldung anzeigen
                        Notification.show("User erfolgreich gespeichert.", 3000, Notification.Position.MIDDLE);

                        // Dialog schließen
                        editDialog.close();
//

                    } catch (Exception e) {
                        // Fehler behandeln
                        e.printStackTrace();
                        Notification.show("Fehler beim Speichern des Users: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
                    }
                });


                // Abbrechen-Button
                Button cancelButton = new Button("Abbrechen", cancelEvent -> {
                    // Dialog ohne Speicherung schließen
                    editDialog.close();
                });

                // Layout für Buttons (unten im Dialog)
                HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);

                // Layout für Eingabefelder und Buttons
                VerticalLayout contentLayout = new VerticalLayout(firstnameField, lastnameField, emailField, encodedPassword,passwordCheckbox, userRoles,
                        buttonLayout);
                contentLayout.setPadding(true);
                contentLayout.setSpacing(true);
                contentLayout.setAlignItems(FlexComponent.Alignment.STRETCH);

                // Dialog-Content hinzufügen und anzeigen
                editDialog.add(contentLayout);
                editDialog.setCloseOnEsc(true); // Schließen bei "Escape"
                editDialog.setCloseOnOutsideClick(false); // Klick außerhalb schließt nicht
                editDialog.open();
            });


            Button deleteButton = new Button(LineAwesomeIcon.TRASH_SOLID.create(), event -> {
                userRepository.delete(user); // Löschen des Users
                Notification.show("User gelöscht!"); // Zeigt eine Benachrichtigung
                load(); // Tabelle nach dem Löschen neu laden
            });
            deleteButton.getStyle().set("background-color", "red"); // Setzt die Hintergrundfarbe auf Rot
            deleteButton.getStyle().set("color", "white"); // Setzt die Schriftfarbe auf Weiß
            deleteButton.getStyle().set("border-radius", "5px"); // Optional: Abgerundete Ecken
            deleteButton.getStyle().set("cursor", "pointer"); // Zeige den Cursor als Zeiger an


            // Kombiniert die Buttons editiren und löschen in einer Zelle
            HorizontalLayout cellLayout = new HorizontalLayout(editButton, deleteButton);
            cellLayout.setSpacing(true); // Abstand zwischen den Buttons
            return cellLayout; // Gibt das Layout für die Zelle zurück
        }).setHeader("Aktion");









        //MULTISELECT AKTIVIEREN (MULTISELECT FÜR LÖSCHEN)
        userGrid.setSelectionMode( Grid.SelectionMode.MULTI );
        userGrid.addSelectionListener(new SelectionListener<Grid<User>, User>() {
            @Override
            public void selectionChange(SelectionEvent<Grid<User>, User> event) {
                Set<User> userSet = event.getAllSelectedItems();
                // Aktivieren/Deaktivieren des Buttons abhängig von der Tabellenauswahl
                multiDelete.setEnabled( (userSet != null) && (!userSet.isEmpty()) );
            }
        });



        userGrid.setAllRowsVisible(true);
        toolbar.add(selectAllCheckbox, multiDelete, neuerUser);
        getContent().add(toolbar);
        getContent().add(userGrid);
        load();
    }


    private void deleteMultiSelect() {
        try {
            Set<User> selectedUsers = userGrid.getSelectedItems();
            StringBuilder infos = new StringBuilder();
            selectedUsers.forEach(
                    user -> infos.append( user.toString() ).append("\n")
            );
            userService.deleteUser(selectedUsers);
            load();
            Notification.show("Gelöscht:" + infos.toString());
        } catch (Exception e) {
            log.error("Fehler beim Löschen. Message={}", e.getMessage(), e);
            Notification.show("Fehler: " + e.getMessage());
        }
    }
    private void onDeleteUser(User user) {
        try {
            userService.deleteUser(Set.of(user));
            load();
            Notification.show("Gelöscht: " + user.toString());
        } catch (Exception e) {
            log.error("Fehler beim Löschen. Message={}", e.getMessage(), e);
            Notification.show("Fehler: " + e.getMessage());
        }
    }

    private void load(){
        userGrid.setItems(query -> {
            PageRequest springPageRequest = VaadinSpringDataHelpers.toSpringPageRequest(query);
            return userService.findAll( springPageRequest );
        });
    }
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
        //<theme-editor-local-classname>
        layoutRow.addClassName("product-inventory-grid-view-horizontal-layout-1");
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