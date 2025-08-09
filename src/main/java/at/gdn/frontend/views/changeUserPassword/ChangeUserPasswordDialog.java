package at.gdn.frontend.views.changeUserPassword;

import at.gdn.backend.entities.User;
import at.gdn.backend.persistence.repository.UserRepository;
import at.gdn.backend.service.UserManagementService1;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
public class ChangeUserPasswordDialog extends Dialog {

    private final UserManagementService1 userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ChangeUserPasswordDialog(UserManagementService1 userService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

        // Initiale Dialog-Konfiguration
        setWidth("400px");
        setModal(true);
        setCloseOnEsc(true);
        setCloseOnOutsideClick(false);
        // Stil hinzufügen
        getElement().getStyle()
                .set("border-radius", "10px")
                .set("background-color", "black") // Schwarzer Hintergrund
                .set("color", "white") // Weiße Schrift
                .set("box-shadow", "0 4px 10px rgba(0, 0, 0, 0.4)");


        // Titel des Dialogs
        H1 headerText = new H1("Passwort ändern");
        headerText.getStyle().set("margin", "0").set("font-size", "var(--lumo-font-size-xl)").set("color", "var(--lumo-primary-text-color)");


// Erstes Passwortfeld: Neues Passwort
        PasswordField passwordField = new PasswordField("Neues Passwort");
        passwordField.setWidthFull(); // Volle Breite des Eingabefeldes
        passwordField.setPlaceholder("Geben Sie Ihr neues Passwort ein");

// Zweites Passwortfeld: Passwort wiederholen
        PasswordField confirmPasswordField = new PasswordField("Passwort wiederholen");
        confirmPasswordField.setWidthFull(); // Volle Breite des Eingabefeldes
        confirmPasswordField.setPlaceholder("Bestätigen Sie Ihr neues Passwort");


// Fehlermeldung
        Span errorMessage = new Span();
        errorMessage.getStyle()
                .set("color", "var(--lumo-error-text-color)")
                .set("font-size", "var(--lumo-font-size-s)");

// Speichern-Button
        Button saveButton = new Button("Speichern", event -> {
            String newPassword = passwordField.getValue();
            String confirmPassword = confirmPasswordField.getValue();

            // Prüfen, ob die Felder leer sind
            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                errorMessage.setText("Beide Passwortfelder müssen ausgefüllt werden.");
                return;
            }

            // Übereinstimmung der Passwörter prüfen
            if (!newPassword.equals(confirmPassword)) {
                errorMessage.setText("Die Passwörter stimmen nicht überein.");
                return;
            }

            // Wenn alles korrekt ist, Passwortänderung ausführen
            handleChangePassword(newPassword);


        });

        Button cancelButton = new Button("Abbrechen", event -> close());

        // Layout für die Buttons
        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);
        buttonLayout.setWidthFull();
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        buttonLayout.getStyle().set("margin-top", "var(--lumo-space-m)");

        // Hauptlayout
        VerticalLayout layout = new VerticalLayout(headerText, passwordField, confirmPasswordField, errorMessage, buttonLayout);
        layout.setSpacing(false);
        layout.setPadding(false);
        layout.getStyle().set("padding", "var(--lumo-space-l)");

        add(layout);
    }


    private void handleChangePassword(String newPassword) {
        if (newPassword == null || newPassword.isEmpty()) {
            Notification.show("Bitte gib ein valides Passwort ein!", 3000, Notification.Position.MIDDLE);
            return;
        }

        // Authentifizierten Benutzer abrufen (über SecurityContextHolder)
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmailAddress(currentUserEmail)
                .orElse(null);

        if (currentUser == null) {
            Notification.show("User nicht gefunden", 3000, Notification.Position.MIDDLE);
            log.error("User nicht gefunden | email: {}", currentUserEmail);
            return;
        }

        // Passwort verschlüsseln und speichern
        String encodedPassword = passwordEncoder.encode(newPassword);
        currentUser.setEncodedPassword(encodedPassword);
        userRepository.save(currentUser);

        // Bestätigung anzeigen
        Notification.show("Passwort erfolgreich geändert!", 3000, Notification.Position.TOP_CENTER).addThemeName("success");
        close();
    }
}