package at.gdn.frontend.views;
import at.gdn.backend.entities.User;
import at.gdn.backend.persistence.repository.UserRepository;
import at.gdn.backend.security.AuthenticatedUser;

import at.gdn.backend.service.UserManagementService1;
import at.gdn.frontend.views.changeUserPassword.ChangeUserPasswordDialog;
import at.gdn.frontend.views.shop.ShoppingCartView;
import at.gdn.frontend.views.productInventory.ProductInventoryGridView;
import at.gdn.frontend.views.anmelden.AnmeldenView;
import at.gdn.frontend.views.home.HomeView;
import at.gdn.frontend.views.kunstler.KunstlerView;
import at.gdn.frontend.views.shop.ShopView;
import at.gdn.frontend.views.singleProduct.SingleProductView;
import at.gdn.frontend.views.userManagement.UserManagementGridView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.BoxSizing;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.FlexDirection;
import com.vaadin.flow.theme.lumo.LumoUtility.FontSize;
import com.vaadin.flow.theme.lumo.LumoUtility.FontWeight;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.Height;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vaadin.flow.theme.lumo.LumoUtility.TextColor;
import com.vaadin.flow.theme.lumo.LumoUtility.Whitespace;
import com.vaadin.flow.theme.lumo.LumoUtility.Width;
import at.gdn.frontend.StringLoch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.vaadin.lineawesome.LineAwesomeIcon;

//import com.vaadin.flow.router.Layout;


/**
 * The main view is a top-level placeholder for other views.
 */
//@Layout
public class MainLayout extends AppLayout {

    private AuthenticatedUser authenticatedUser;

    private final UserManagementService1 userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public MainLayout(@Autowired AuthenticatedUser authenticatedUser, UserManagementService1 userService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticatedUser = authenticatedUser;

        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;


        addToNavbar(createHeaderContent());
        //getStyle().setHeight("1000px");

    }

    /**
     * A simple navigation item component, based on ListItem element.
     */
    public static class MenuItemInfo extends ListItem {

        private final Class<? extends Component> view;

        public MenuItemInfo(String menuTitle, Component icon, Class<? extends Component> view) {
            this.view = view;
            RouterLink link = new RouterLink();
            // Use Lumo classnames for various styling
            link.addClassNames(Display.FLEX, Gap.XSMALL, Height.MEDIUM, AlignItems.CENTER, Padding.Horizontal.SMALL,
                    TextColor.BODY);
            link.setRoute(view);

            Span text = new Span(menuTitle);
            // Use Lumo classnames for various styling
            text.addClassNames(FontWeight.MEDIUM, FontSize.MEDIUM, Whitespace.NOWRAP);

            if (icon != null) {
                link.add(icon);
            }
            link.add(text);
            add(link);
        }

        public Class<?> getView() {
            return view;
        }

    }



    private Component createHeaderContent() {
        Header header = new Header();
        header.addClassNames(BoxSizing.BORDER, Display.FLEX, FlexDirection.COLUMN, Width.FULL);
        header.getStyle().set("background-color", StringLoch.BLACK);

        // Hauptcontainer: horizontale Anordnung mit Abstand zwischen linker und rechter Seite
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidthFull();
        horizontalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        horizontalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        horizontalLayout.setPadding(true);

        // LINKER BEREICH: Logo, Schriftzug und Navigation
        HorizontalLayout leftSide = new HorizontalLayout();
        leftSide.setAlignItems(FlexComponent.Alignment.CENTER);
        leftSide.getStyle().set("gap", "20px"); // Abstand zwischen den Elementen im linken Bereich

        // Logo
        Avatar logo = new Avatar();
        logo.setImage(StringLoch.LOGO_IMAGE);
        logo.getStyle().set("margin-left", "20px");
        logo.setWidth("100px");
        logo.setHeight("100px");

        // Schriftzug "Gesindel der Nacht" als kleineres Heading
        H4 headerText = new H4("Gesindel der Nacht");
        headerText.getStyle().set("color", StringLoch.WHITE);
        headerText.getStyle().set("font-size", "1.8 rem");  // kleinere Schriftgröße
        headerText.getStyle().set("margin-right", "30px");

        // Navigations-Buttons: Shop, Home, Künstler
        HorizontalLayout navItems = new HorizontalLayout();
        navItems.setAlignItems(FlexComponent.Alignment.CENTER);
        navItems.getStyle().set("gap", "30px"); // Abstand zwischen den Buttons

        Button home = new Button("Home");
        home.getStyle().set("color", "white");
        home.getStyle().set("background-color", "transparent");
        home.getStyle().set("cursor", "pointer");
        home.addClickListener(event -> UI.getCurrent().navigate(HomeView.class));

        Button shop = new Button("Shop");
        shop.getStyle().set("color", "white");
        shop.getStyle().set("background-color", "transparent");
        shop.getStyle().set("cursor", "pointer");
        shop.addClickListener(event -> UI.getCurrent().navigate(ShopView.class));

        Button design = new Button("Künstler");
        design.getStyle().set("color", "white");
        design.getStyle().set("background-color", "transparent");
        design.getStyle().set("cursor", "pointer");
        design.addClickListener(event -> UI.getCurrent().navigate(KunstlerView.class));

        navItems.add(home,shop, design);

        // Füge Logo, Schriftzug und Navigation zusammen in den linken Bereich ein
        leftSide.add(logo, headerText, navItems);

        // RECHTER BEREICH: Icons-Panel
        HorizontalLayout iconPanel = new HorizontalLayout();
        iconPanel.setAlignItems(FlexComponent.Alignment.CENTER);
        iconPanel.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        iconPanel.getStyle().set("padding-right", "80px");
        iconPanel.getStyle().set("gap", "60px");
//
//        Icon heart = new Icon(VaadinIcon.HEART_O);
//        heart.setColor(StringLoch.WHITE);
//        heart.getStyle().set("width", "20px");
//        heart.getStyle().set("height", "20px");
////        heart.addClickListener(event->UI.getCurrent().navigate(SingleProductView.class));


        Icon cart = new Icon(VaadinIcon.CART_O);
        cart.setColor(StringLoch.WHITE);
        cart.getStyle().set("width", "20px");
        cart.getStyle().set("height", "20px");
        cart.getStyle().set("cursor", "pointer");
        cart.addClickListener(event -> UI.getCurrent().navigate(ShoppingCartView.class));

        SvgIcon lock = LineAwesomeIcon.KEY_SOLID.create();
        lock.setColor(StringLoch.WHITE);
        lock.getStyle().set("width", "20px");
        lock.getStyle().set("height", "20px");
        lock.getStyle().set("cursor", "pointer");
        lock.getStyle().set("transition", "all 0.3s ease");

        // Überprüfen, ob der eingeloggte Benutzer die Rolle "Customer" hat
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_CUSTOMER"))) {
            lock.addClickListener((event -> {
                ChangeUserPasswordDialog dialog = new ChangeUserPasswordDialog(userService, userRepository, passwordEncoder);
                dialog.open(); // Öffne den Dialog nur für Customer
            }));
            iconPanel.add(lock); // Nur für Customer wird das Icon hinzugefügt
        }


        Icon user = new Icon(VaadinIcon.USER);
        user.setColor(StringLoch.WHITE);
        user.getStyle().set("width", "20px");
        user.getStyle().set("height", "20px");
        user.getStyle().set("cursor", "pointer");
        user.addClickListener(event -> UI.getCurrent().navigate(AnmeldenView.class));

        Icon logout = new Icon(VaadinIcon.SIGN_OUT);
        logout.setColor(StringLoch.WHITE);
        logout.getStyle().set("width", "20px");
        logout.getStyle().set("height", "20px");
        logout.getStyle().set("cursor", "pointer");

        if (authenticatedUser.getAuthenticatedUser() != null) {
            logout.addClickListener(event -> authenticatedUser.logout());
            iconPanel.add(logout);
        }
        else{
            iconPanel.add(user);
        }

        iconPanel.add(cart);


        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"))) {
            SvgIcon bag = LineAwesomeIcon.SHOPPING_BAG_SOLID.create();
            bag.setColor(StringLoch.WHITE);
            bag.getStyle().set("width", "20px");
            bag.getStyle().set("height", "20px");
            bag.getStyle().set("cursor", "pointer");
            bag.getStyle().set("transition", "all 0.3s ease");
            bag.addClickListener(event -> UI.getCurrent().navigate(ProductInventoryGridView.class));

            SvgIcon management = LineAwesomeIcon.COG_SOLID.create();
            management.setColor(StringLoch.WHITE);
            management.getStyle().set("width", "24px");
            management.getStyle().set("height", "24px");
            management.getStyle().set("cursor", "pointer");
            management.addClickListener(event -> UI.getCurrent().navigate(UserManagementGridView.class));

            iconPanel.add(bag, management);
        }


//        Icon checkout = new Icon(VaadinIcon.INVOICE);
        /* SvgIcon bag = LineAwesomeIcon.SHOPPING_BAG_SOLID.create();
        bag.setColor(StringLoch.WHITE);
        bag.getStyle().set("width", "20px");
        bag.getStyle().set("height", "20px");
        bag.getStyle().set("cursor", "pointer");
        bag.getStyle().set("transition", "all 0.3s ease");
        bag.addClickListener(event -> UI.getCurrent().navigate(ProductInventoryGridView.class));


        SvgIcon management = LineAwesomeIcon.COG_SOLID.create();
        management.setColor(StringLoch.WHITE);
        management.getStyle().set("width", "24px");
        management.getStyle().set("height", "24px");
        management.getStyle().set("cursor", "pointer");
        management.addClickListener(event -> UI.getCurrent().navigate(UserManagementGridView.class));*/

        /*Icon checkout = new Icon(VaadinIcon.CHECK_CIRCLE);
        checkout.setColor(StringLoch.RED);
        checkout.getStyle().set("width", "20px");
        checkout.getStyle().set("height", "20px");
        checkout.getStyle().set("cursor", "pointer");
        checkout.addClickListener(event -> UI.getCurrent().navigate(CheckoutView.class));*/




        // Gesamtaufbau: linker Bereich (Logo, Schriftzug, Navigation) und rechter Bereich (Icons)
        horizontalLayout.add(leftSide, iconPanel);
        header.add(horizontalLayout);
        return header;
    }
}