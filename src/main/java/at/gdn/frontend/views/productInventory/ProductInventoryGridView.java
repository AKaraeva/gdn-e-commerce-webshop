package at.gdn.frontend.views.productInventory;
import at.gdn.backend.entities.Category;
import at.gdn.backend.entities.Product;
import at.gdn.backend.persistence.repository.ProductRepository;
import at.gdn.backend.valueobjects.ProductImage;
import at.gdn.frontend.StringLoch;
import at.gdn.frontend.views.MainLayout;
import at.gdn.backend.service.ProductInventoryService1;
import at.gdn.frontend.views.shop.ShopView;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
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
import com.vaadin.flow.component.textfield.NumberField;
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
import org.springframework.data.domain.Pageable;
import org.vaadin.lineawesome.LineAwesomeIcon;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@PageTitle("Produktliste| Gesindel der Nacht")
@Route(value = "inventory/list", layout = MainLayout.class)
@Menu(order = 4, icon = LineAwesomeIconUrl.CART_PLUS_SOLID)
@RolesAllowed(value = {"ROLE_ADMIN", "ROLE_OPERATOR" })
//@AnonymousAllowed
public class ProductInventoryGridView extends Composite<VerticalLayout> {

    private final ProductInventoryService1 productService;
    private final ProductRepository productRepository;

    private final Grid<Product> productGrid = new Grid<>(Product.class, false);

    @Autowired
    public ProductInventoryGridView(ProductInventoryService1 productService, ProductRepository productRepository) {
        this.productService = productService;
        this.productRepository = productRepository;

        init();
        initializeFooterLayout();
    }


    private void init() {
        // Erstelle das Grid mit einem MULTI-Selection-Modus
        productGrid.setSelectionMode(Grid.SelectionMode.MULTI);

        HorizontalLayout toolbar = new HorizontalLayout();


// Erstelle die "Alle auswählen"-Checkbox
        Checkbox selectAllCheckbox = new Checkbox();
        selectAllCheckbox.addClassName("product-inventory-grid-view-checkbox-1");


// Füge den ValueChangeListener für die Checkbox hinzu
        selectAllCheckbox.addValueChangeListener(event -> {
            if (productGrid != null && productGrid.getSelectionModel() instanceof GridMultiSelectionModel<Product> multiSelectionModel) {
                // Prüfen, ob Checkbox aktiviert wurde
                if (Boolean.TRUE.equals(event.getValue())) {
                    // Multi-Select über den DataProvider
                    productGrid.getDataProvider()
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
            Set<Product> selectedProducts = productGrid.getSelectedItems();
            if (!selectedProducts.isEmpty()) {
                productRepository.deleteAll(selectedProducts); // Verwendung von deleteAll() für das gesamte Set
                Notification.show(selectedProducts.size() + " Produkte gelöscht!");
                load(); // Tabelle nach dem Löschen neu laden
            } else {
                Notification.show("Keine Produkte ausgewählt."); // Hinweis für den Benutzer, falls keine Auswahl vorliegt
            }
        });
        multiDelete.setEnabled(false); // Standardmäßig deaktiviert
        multiDelete.getStyle().set("color", "red"); // Nur rotes Icon für den Mülleimer in der Header-Spalte
        multiDelete.getStyle().set("border", "none"); // Ohne Rand für einen einfachen Icon-Style
        multiDelete.getStyle().set("cursor", "pointer"); // Zeige den Zeiger-Cursor an

        //MULTISELECT AKTIVIEREN (MULTISELECT FÜR LÖSCHEN)
        productGrid.addSelectionListener(new SelectionListener<Grid<Product>, Product>() {
            @Override
            public void selectionChange(SelectionEvent<Grid<Product>, Product> event) {
                Set<Product> productSet = event.getAllSelectedItems();


                // Aktivieren/Deaktivieren des Buttons abhängig von der Tabellenauswahl - IF/ELSE VARIANTE
//                if(personSet != null && personSet.size() > 0) {
//                    delete.setEnabled(true);
//                } else {
//                    delete.setEnabled(false);
//                }
                // Aktivieren/Deaktivieren des Buttons abhängig von der Tabellenauswahl
                multiDelete.setEnabled((productSet != null) && (!productSet.isEmpty()));
            }
        });


        //NEUES PRODUKT BUTTON    // "Neues Produkt"-Button erstellen
        Button neuesProdukt = new Button("Neues Produkt hinzufügen");
        neuesProdukt.getStyle().set("background-color", "green");
        neuesProdukt.getStyle().set("color", "white");
        neuesProdukt.getStyle().set("border-radius", "5px");
        neuesProdukt.getStyle().set("cursor", "pointer");

        // Klick-Listener für den Button
        neuesProdukt.addClickListener(event -> {
            // Dialog erzeugen
            Dialog addProductDialog = new Dialog();
            addProductDialog.setHeaderTitle("Neues Produkt hinzufügen");
            addProductDialog.setWidth("500px");
            addProductDialog.setHeight("700px");

            // Textfeld für den Produktnamen (leer)
            TextField nameField = new TextField("Name");

            // Nummernfeld für den Preis (leer)
            NumberField priceField = new NumberField("Preis");

            // Nummernfeld für die Menge (leer)
            NumberField quantityField = new NumberField("Menge");

            // Textfeld für die Beschreibung (leer)
            TextField descriptionField = new TextField("Beschreibung");

            // Dropdown für das Bild
            ComboBox<String> imageDropdown = new ComboBox<>("Bild");
            imageDropdown.setAllowCustomValue(false); // Nur Auswahl aus der Liste erlauben
            imageDropdown.setWidth("100%");

            // Bilder aus dem Verzeichnis `/images/` laden
            List<String> imageNames = loadImageNames();
            if (imageNames.isEmpty()) {
                System.out.println("Keine Bilder gefunden!");
            } else {
                System.out.println("Bilder in Dropdown hinzufügen: " + imageNames);
            }
            imageDropdown.setItems(imageNames);

            // Speichern-Button
            Button saveButton = new Button("Speichern", saveEvent -> {
                // Validierung der Eingaben
                if (nameField.isEmpty() || priceField.isEmpty() || quantityField.isEmpty() || imageDropdown.isEmpty()) {
                    Notification.show("Bitte alle Felder ausfüllen.", 3000, Notification.Position.MIDDLE);
                    return;
                }

                // Neues Produkt erstellen
                Product newProduct = new Product();
                newProduct.setProductName(nameField.getValue());
                newProduct.setProductPrice(priceField.getValue().intValue());
                newProduct.setProductQuantity(quantityField.getValue().intValue());
                newProduct.setProductDescription(descriptionField.getValue());

                // Produktbild auswählen
                String selectedImage = imageDropdown.getValue();
                newProduct.setProductImage(List.of(new ProductImage("/images/" + selectedImage)));

                try {
                    // Neues Produkt speichern
                    productRepository.save(newProduct);

                    // Tabelle aktualisieren
                    productGrid.getDataProvider().refreshAll();

                    // Erfolgsmeldung anzeigen
                    Notification.show("Produkt erfolgreich hinzugefügt.", 3000, Notification.Position.MIDDLE);

                    // Dialog schließen
                    addProductDialog.close();

                } catch (Exception e) {
                    // Fehler behandeln
                    e.printStackTrace();
                    Notification.show("Fehler beim Hinzufügen des Produkts: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
                }
            });

            // Abbrechen-Button
            Button cancelButton = new Button("Abbrechen", cancelEvent -> {
                // Dialog ohne Speicherung schließen
                addProductDialog.close();
            });

            // Layout für Buttons (unten im Dialog)
            HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);

            // Layout für Eingabefelder und Buttons
            VerticalLayout contentLayout = new VerticalLayout(
                    nameField,
                    descriptionField,
                    priceField,
                    quantityField,
                    imageDropdown,
                    buttonLayout
            );
            contentLayout.setPadding(true);
            contentLayout.setSpacing(true);
            contentLayout.setAlignItems(FlexComponent.Alignment.STRETCH);

            // Dialog-Content hinzufügen und anzeigen
            addProductDialog.add(contentLayout);
            addProductDialog.setCloseOnEsc(true); // Schließen bei "Escape"
            addProductDialog.setCloseOnOutsideClick(false); // Klick außerhalb schließt nicht
            addProductDialog.open();
        });



        // Product Grid initialization
        productGrid.addColumn(product -> product.getId().id()).setHeader("ID")
                .setFlexGrow(1)        // Relative Flex-Breite
                .setWidth("10px")     // Feste Standardbreite
                .setAutoWidth(true);  // Automatische Anpassung basierend
        productGrid.addColumn(Product::getProductName).setSortProperty("productname").setHeader("Name")
                .setFlexGrow(1)        // Relative Flex-Breite
                .setWidth("25px")     // Feste Standardbreite
                .setAutoWidth(true);  // Automatische Anpassung basierend

        productGrid.addColumn(Product::getProductDescription).setHeader("Bezeichnung")
                .setFlexGrow(1)        // Relative Flex-Breite
                .setWidth("100px")     // Feste Standardbreite
                .setAutoWidth(false);  // Automatische Anpassung basierend


        productGrid.addColumn(Product::getProductPrice).setSortProperty("price").setHeader("Preis");
        productGrid.addColumn(Product::getProductQuantity).setSortProperty("quantity").setHeader("Menge");
        productGrid.addComponentColumn(product -> {
            List<ProductImage> images = product.getProductImage();
            if (images == null || images.isEmpty()) {
                return new Span("No images"); // Kein Bild vorhanden
            }
            // Container für Bilder
            HorizontalLayout imageLayout = new HorizontalLayout();
            imageLayout.setSpacing(true);

            int limit = 5; // Maximale Anzahl der anzuzeigenden Bilder
            images.stream()
                    .filter(Objects::nonNull)
                    .limit(limit) // Begrenze auf 5 Bilder
                    .forEach(image -> {
                        if (image.getImageName() != null) {
                            // Beispiel: URL aus dem Namen generieren, oder direkt aus einer getUrl()-Methode
                            String imageUrl = image.getImageName(); // URL-Pfaderzeugung (anpassen)
                            Image img = new Image(imageUrl, "Product Image");
                            img.setMaxWidth("50px"); // Optional: Größe der Bilder einstellen
                            img.setMaxHeight("50px");
                            imageLayout.add(img);
                        }
                    });

            if (images.size() > limit) {
                Span moreImages = new Span("(+ more)"); // Hinweis auf weitere Bilder
                imageLayout.add(moreImages);
            }

            return imageLayout; // Rückgabe des Layouts mit Bildern
        }).setHeader("Image");
        productGrid.addColumn(product -> {
            List<Category> categories = product.getCategories(); // Produktkategorien abrufen
            if (categories == null || categories.isEmpty()) {    // Wenn keine Kategorien vorhanden sind
                return "keine Kategorie";
            }
            // Stream mit Nullprüfung für jedes Category-Objekt
            return categories.stream()
                    .filter(Objects::nonNull) // Sicherstellen, dass 'category' nicht null ist
                    .map(category -> {
                        // Nullprüfung auf 'categoryName'
                        String categoryName = category.getCategoryName();
                        return categoryName != null ? categoryName : "";
                    })
                    .collect(Collectors.joining(", "));
        }).setHeader("Kategorie");
        // "Bearbeiten"-Button in die Grid-Spalte einfügen
        productGrid.addComponentColumn(product -> {
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
                editDialog.setHeaderTitle("Produkt bearbeiten");
                editDialog.setWidth("500px");
                editDialog.setHeight("700px");


                // Textfeld für den Produktnamen
                TextField nameField = new TextField("Name");
                nameField.setValue(product.getProductName()); // Wert aus Grid setzen

                // Nummernfeld für den Preis
                NumberField priceField = new NumberField("Preis");
                priceField.setValue(product.getProductPrice().doubleValue()); // Wert aus Grid setzen

                NumberField quantityField = new NumberField("Menge");
                quantityField.setValue(product.getProductQuantity().doubleValue()); // Wert aus Grid setzen

                TextField descriptionField = new TextField("Beschreibung");
                descriptionField.setValue(product.getProductDescription()); // Wert aus Grid setzen

                // **Dropdown für das Bild**
                ComboBox<String> imageDropdown = new ComboBox<>("Bild");
                imageDropdown.setAllowCustomValue(false); // Nur Auswahl aus der Liste erlauben
                imageDropdown.setWidth("100%");

                // Bilder aus dem Verzeichnis `/images/` laden
                List<String> imageNames = loadImageNames();
                if (imageNames.isEmpty()) {
                    System.out.println("Keine Bilder gefunden!");
                } else {
                    System.out.println("Bilder in Dropdown hinzufügen: " + imageNames);
                }
                imageDropdown.setItems(imageNames);
                imageDropdown.setValue(product.getProductImage().get(0).getImageName());
                imageDropdown.setEnabled(false);

                Checkbox imageCheckbox = new Checkbox("Produktbild ändern");
                imageCheckbox.addValueChangeListener(event1 -> {
                    if (Boolean.TRUE.equals(event1.getValue())) {
                        imageDropdown.setEnabled(true);

                    } else {
                        imageDropdown.setEnabled(false);
                    }
                });


                // Wert aus Grid setzen

                // Speichern-Button
                Button saveButton = new Button("Speichern", saveEvent -> {
                    // Neue Werte setzen

                    if (nameField.isEmpty() || priceField.isEmpty()) {
                        Notification.show("Bitte alle Felder ausfüllen.", 3000, Notification.Position.MIDDLE);
                        return;
                    }

                    // Textfeld für den Produktnamen
                    product.setProductName(nameField.getValue());

                    // Nummernfeld für den Preis
                    product.setProductPrice(priceField.getValue().intValue());

                    // Nummernfeld für die Menge
                    product.setProductQuantity(quantityField.getValue().intValue());

                    // Textfeld für die Beschreibung
                    product.setProductDescription(descriptionField.getValue());

                    if(imageCheckbox.getValue() == true) {
                        // Bild speichern, wenn ausgewählt
                        String selectedImage = imageDropdown.getValue();
                        // Produktbild aktualisieren mit Pfad zu images/
                        product.setProductImage(List.of(new ProductImage("/images/" + selectedImage)));
                    }



                    try {
                        // Änderungen speichern
                        productRepository.save(product);

                        // Tabelle aktualisieren

                        productGrid.getDataProvider().refreshAll();
                        productGrid.setItems(productRepository.findAll());
//                        UI.getCurrent().getPage().reload();

                        //productGrid.getDataCommunicator().refresh(product);

                        // Erfolgsmeldung anzeigen
                        Notification.show("Produkt erfolgreich gespeichert.", 3000, Notification.Position.MIDDLE);

                        // Dialog schließen
                        editDialog.close();
//

                    } catch (Exception e) {
                        // Fehler behandeln
                        e.printStackTrace();
                        Notification.show("Fehler beim Speichern des Produkts: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
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
                VerticalLayout contentLayout = new VerticalLayout(nameField, descriptionField, priceField, quantityField, imageDropdown,imageCheckbox,
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
                productRepository.delete(product); // Löschen des Produkts
                Notification.show("Produkt gelöscht!"); // Zeigt eine Benachrichtigung
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
        productGrid.setSelectionMode( Grid.SelectionMode.MULTI );
        productGrid.addSelectionListener(new SelectionListener<Grid<Product>, Product>() {
            @Override
            public void selectionChange(SelectionEvent<Grid<Product>, Product> event) {
                Set<Product> productSet = event.getAllSelectedItems();
                // Aktivieren/Deaktivieren des Buttons abhängig von der Tabellenauswahl - IF/ELSE VARIANTE
//                if(personSet != null && personSet.size() > 0) {
//                    delete.setEnabled(true);
//                } else {
//                    delete.setEnabled(false);
//                }
                // Aktivieren/Deaktivieren des Buttons abhängig von der Tabellenauswahl
                multiDelete.setEnabled( (productSet != null) && (!productSet.isEmpty()) );
            }
        });




    productGrid.setAllRowsVisible(true);
    toolbar.add(selectAllCheckbox, multiDelete, neuesProdukt);
    getContent().add(toolbar);
    getContent().add(productGrid);
    load();
}


    private void deleteMultiSelect() {
        try {
            Set<Product> seltedProducts = productGrid.getSelectedItems();
            StringBuilder infos = new StringBuilder();
            seltedProducts.forEach(
                    user -> infos.append( user.toString() ).append("\n")
            );
            productService.deleteProduct(seltedProducts);
            load();
            Notification.show("Gelöscht:" + infos.toString());
        } catch (Exception e) {
            log.error("Fehler beim Löschen. Message={}", e.getMessage(), e);
            Notification.show("Fehler: " + e.getMessage());
        }
    }
    private void onDeleteProduct(Product product) {
        try {
            productService.deleteProduct(Set.of(product));
            load();
            Notification.show("Gelöscht: " + product.toString());
        } catch (Exception e) {
            log.error("Fehler beim Löschen. Message={}", e.getMessage(), e);
            Notification.show("Fehler: " + e.getMessage());
        }
    }

    private void load() {
        productGrid.setItems( query -> {
            PageRequest springPageRequest = VaadinSpringDataHelpers.toSpringPageRequest(query);
            return productService.findAll( springPageRequest ); // Page<Product> wird direkt zurückgegeben
        });
    }

    // Methode zum Laden von Bildnamen
    private List<String> loadImageNames() {
        try {
            // ClassLoader verwenden, um Ressourcen aus dem Ordner images/ zu laden
            ClassLoader classLoader = getClass().getClassLoader();
            URL imagesFolderUrl = classLoader.getResource("META-INF/resources/images/");
            if (imagesFolderUrl == null) {
                // Verzeichnis existiert nicht
                return Collections.emptyList();
            }

            // Ressourcen aus URL abrufen
            URI imagesFolderUri = imagesFolderUrl.toURI();
            Path imagesFolderPath = Paths.get(imagesFolderUri);

            // Alle Dateien mit Bild-Endungen sammeln
            return Files.walk(imagesFolderPath, 1) // Rekursionsebene 1 (nur direktes Verzeichnis)
                    .filter(Files::isRegularFile) // Nur Dateien (keine Verzeichnisse)
                    .map(Path::getFileName) // Nur Dateinamen
                    .map(Path::toString) // Zu String (z. B. "bild.jpg")
                    .filter(name -> name.matches(".*\\.(png|jpg|jpeg|gif)")) // Nur Bilder
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList(); // Fehlerfall: leere Liste zurückgeben
        }
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