package at.gdn.frontend.views.shop;

import at.gdn.backend.entities.Product;
import at.gdn.frontend.views.singleProduct.SingleProductView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility.*;

public class ShopItemCard extends ListItem {

    private boolean isAdmin; // Variable, um den Admin-Status zu speichern

    //public ShopItemCard(ShopView.ShopItem item) {
    public ShopItemCard(Product product) {
        addClassNames(Background.CONTRAST_5, Display.FLEX, FlexDirection.COLUMN, AlignItems.START, Padding.MEDIUM,
                BorderRadius.LARGE);

        getStyle().set("width", "100%"); // Karten passen sich an Grid-Spalten an
        getStyle().set("max-width", "200px"); // Maximale Breite der Karten
        getStyle().set("box-shadow", "0px 4px 10px rgba(0, 0, 0, 0.1)"); // Schatten für bessere Optik
        getStyle().set("margin-bottom", "10px"); // Abstand zwischen Reihen
        getStyle().set("margin-right", "5px"); // Entfernt zusätzlichen Abstand


        Div div = new Div();
        div.addClassNames(Background.CONTRAST, Display.FLEX, AlignItems.CENTER, JustifyContent.CENTER,
                Margin.Bottom.MEDIUM, Overflow.HIDDEN, BorderRadius.MEDIUM, Width.FULL);
        div.setHeight("180px");
        div.getStyle().set("background-color", "white");

        Image image = new Image();
        image.setWidth("100px");
        image.setHeight("auto"); // Automatische Höhe, damit das Bild nicht verzerrt wird
        image.getStyle().set("object-fit", "cover"); // Bild füllt den Container aus
        image.getStyle().set("border-radius", "10px"); // Leichte Abrundung für schöneres Design
        image.getStyle().set("padding", "50px");

        //Pfad zum Bild
        if (product.getProductImage() != null && !product.getProductImage().isEmpty()) {
            image.setSrc(product.getProductImage().get(0).getImageName());
        } else {
            image.setSrc("images/not_found.webp");
        }

        div.add(image);

        Span header = new Span();
        header.addClassNames(FontSize.MEDIUM, FontWeight.SEMIBOLD);
        //header.setText(item.getName());
        header.setText(product.getProductName());
        header.getStyle().set("display", "block");

        Span subtitle = new Span();
        subtitle.addClassNames(FontSize.SMALL, TextColor.SECONDARY);
        //subtitle.setText(item.getDescription());
        subtitle.setText(product.getProductDescription());
        subtitle.getStyle().set("display", "block");

        //Paragraph description = new Paragraph("€ " + item.getPrice());
        Paragraph description = new Paragraph("€ " + String.format("%.2f", product.getProductPrice().doubleValue()));
        description.addClassName(Margin.Vertical.MEDIUM);

        /*Button addToCart = new Button("In den Warenkorb");
        addToCart.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addToCart.getStyle().set("width", "100%");*/

        //RouterLink to ProductDetailView
        RouterLink detailsLink = new RouterLink("", SingleProductView.class,
        new RouteParameters("productId", String.valueOf(product.getId().id())));

        detailsLink.add(div, header, subtitle, description);

        add(detailsLink);
    }
}