package demo.ui.view;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.themes.ValoTheme;
import demo.data.DataProvider;
import demo.domain.CinemaBooking;
import demo.domain.User;
import demo.ui.component.BookingWindow;
import demo.ui.component.ProfilePreferencesWindow;
import demo.ui.event.DashboardEvent.NotificationsCountUpdatedEvent;
import demo.ui.event.DashboardEvent.PostViewChangeEvent;
import demo.ui.event.DashboardEvent.ProfileUpdatedEvent;
import demo.ui.event.DashboardEvent.UserLoggedOutEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.security.shared.VaadinSharedSecurity;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MCssLayout;

import javax.annotation.PostConstruct;

/**
 * A responsive menu component providing user information and the controls for
 * primary navigation between the views.
 */
@SpringComponent
@UIScope
@SuppressWarnings({ "serial", "unchecked" })
public final class DashboardMenu extends CustomComponent {

    public static final String ID = "dashboard-menu";
    public static final String NOTIFICATIONS_BADGE_ID = "dashboard-menu-notifications-badge";
    private static final String STYLE_VISIBLE = "valo-menu-visible";
    private Label notificationsBadge;
    private MenuItem settingsItem;

    private final EventBus.UIEventBus dashboardEventBus;
    private final ProfilePreferencesWindow profilePreferencesWindow;
    private final DataProvider dataProvider;
    private final VaadinSharedSecurity vaadinSecurity;
    private final ApplicationContext applicationContext;
    private final BookingWindow bookingWindow;


    @Autowired
    public DashboardMenu(EventBus.UIEventBus dashboardEventBus,
                         ProfilePreferencesWindow profilePreferencesWindow,
                         BookingWindow bookingWindow,
                         DataProvider dataProvider,
                         VaadinSharedSecurity vaadinSecurity,
                         ApplicationContext applicationContext) {

        this.dashboardEventBus = dashboardEventBus;
        this.profilePreferencesWindow = profilePreferencesWindow;
        this.dataProvider = dataProvider;
        this.vaadinSecurity = vaadinSecurity;
        this.applicationContext = applicationContext;
        this.bookingWindow = bookingWindow;

        setPrimaryStyleName("valo-menu");
        setId(ID);
        setSizeUndefined();
    }

    @PostConstruct
    public void init() {
        // There's only one DashboardMenu per UI so this doesn't need to be
        // unregistered from the UI-scoped DashboardEventBus.
        dashboardEventBus.subscribe(this);
        setCompositionRoot(buildContent());
    }

    private Component buildContent() {
        return new MCssLayout(
                buildTitle(), buildUserMenu(), buildToggleButton(), buildMenuItems()
        ).withStyleName(
                "sidebar",
                ValoTheme.DRAG_AND_DROP_WRAPPER_NO_VERTICAL_DRAG_HINTS,
                ValoTheme.DRAG_AND_DROP_WRAPPER_NO_HORIZONTAL_DRAG_HINTS,
                ValoTheme.MENU_PART)
                .withWidthUndefined()
                .withFullHeight();
    }

    private Component buildTitle() {
        Label logo = new Label("QuickTickets <strong>Dashboard</strong>",
                ContentMode.HTML);
        logo.setSizeUndefined();
        HorizontalLayout logoWrapper = new HorizontalLayout(logo);
        logoWrapper.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);
        logoWrapper.addStyleName("valo-menu-title");
        logoWrapper.setSpacing(false);
        return logoWrapper;
    }

    private User getCurrentUser() {
        Object principal = vaadinSecurity.getAuthentication().getPrincipal();
        if(principal!=null) {

            if(principal instanceof User) {
                return (User) principal;
            }
        }
        return null;
    }

    private Component buildUserMenu() {
        final MenuBar settings = new MenuBar();
        settings.addStyleName("user-menu");

        settingsItem = settings.addItem("",
                new ThemeResource("img/profile-pic-300px.jpg"), null);
        updateUserName(null);
        settingsItem.addItem("Edit Profile", selectedItem -> profilePreferencesWindow.open(getCurrentUser(), false));
        settingsItem.addItem("Preferences", selectedItem -> profilePreferencesWindow.open(getCurrentUser(), true));
        settingsItem.addSeparator();
        settingsItem.addItem("Sign Out", selectedItem ->  dashboardEventBus.publish(this, new UserLoggedOutEvent()));
        return settings;
    }

    private Component buildToggleButton() {
        return new MButton("Menu", event -> {
            if (getCompositionRoot().getStyleName().contains(STYLE_VISIBLE)) {
                getCompositionRoot().removeStyleName(STYLE_VISIBLE);
            } else {
                getCompositionRoot().addStyleName(STYLE_VISIBLE);
            }
        }).withIcon(VaadinIcons.LIST)
                .withStyleName("valo-menu-toggle",ValoTheme.BUTTON_BORDERLESS,ValoTheme.BUTTON_SMALL);
    }

    private Component buildMenuItems() {
        MCssLayout menuItemsLayout = new MCssLayout().withStyleName("valo-menuitems");

        for (final DashboardViewType view : DashboardViewType.values()) {
            Secured secured = view.getViewClass().getAnnotation(Secured.class);
            //Secured secured = applicationContext.findAnnotationOnBean(view.getViewName(), Secured.class);
            if(secured==null || vaadinSecurity.hasAnyAuthority(secured.value())) {
                Component menuItemComponent = new ValoMenuItemButton(view);

                if (view == DashboardViewType.DASHBOARD) {
                    notificationsBadge = new MLabel().withId(NOTIFICATIONS_BADGE_ID);
                    menuItemComponent = buildBadgeWrapper(menuItemComponent, notificationsBadge);
                }

                menuItemsLayout.addComponent(menuItemComponent);
            }
        }

        menuItemsLayout.addComponent(
                new MButton().withPrimaryStyleName("valo-menu-item").withIcon(VaadinIcons.BOOK)
                        .withCaption("Booking").withListener((Button.ClickListener) e -> bookingWindow.open()));

        menuItemsLayout.addComponent(
                new MButton().withPrimaryStyleName("valo-menu-item").withIcon(VaadinIcons.POWER_OFF)
                        .withCaption("Logout").withListener((Button.ClickListener) e -> vaadinSecurity.logout()));

        return menuItemsLayout;

    }

    private Component buildBadgeWrapper(final Component menuItemButton, final Component badgeLabel) {
        MCssLayout dashboardWrapper = new MCssLayout(menuItemButton,badgeLabel)
                .withStyleName("badgewrapper",ValoTheme.MENU_ITEM);

        badgeLabel.addStyleName(ValoTheme.MENU_BADGE);
        badgeLabel.setWidthUndefined();
        badgeLabel.setVisible(false);

        return dashboardWrapper;
    }

    @Override
    public void attach() {
        super.attach();
        updateNotificationsCount(null);
    }

    @EventBusListenerMethod
    public void postViewChange(final PostViewChangeEvent event) {
        // After a successful view change the menu can be hidden in mobile view.
        getCompositionRoot().removeStyleName(STYLE_VISIBLE);
    }

    @EventBusListenerMethod
    public void updateNotificationsCount(final NotificationsCountUpdatedEvent event) {
        long unreadNotificationsCount = dataProvider.getUnreadNotificationsCount();
        notificationsBadge.setValue(String.valueOf(unreadNotificationsCount));
        notificationsBadge.setVisible(unreadNotificationsCount > 0);
    }

    @EventBusListenerMethod
    public void updateUserName(final ProfileUpdatedEvent event) {
        User user = getCurrentUser();
        if(user != null) {
            settingsItem.setText(user.getFirstName() + " " + user.getLastName());
        } else {
            settingsItem.setText("");
        }
    }

    public final class ValoMenuItemButton extends Button {

        private static final String STYLE_SELECTED = "selected";

        private final DashboardViewType view;

        public ValoMenuItemButton(final DashboardViewType view) {
            this.view = view;
            setPrimaryStyleName("valo-menu-item");
            setIcon(view.getIcon());
            setCaption(view.getTitle());
            dashboardEventBus.subscribe(this);
            addClickListener(event -> UI.getCurrent().getNavigator().navigateTo(view.getViewName()));

        }

        @EventBusListenerMethod
        public void postViewChange(final PostViewChangeEvent event) {
            removeStyleName(STYLE_SELECTED);
            if (event.getView() == view) {
                addStyleName(STYLE_SELECTED);
            }
        }
    }
}