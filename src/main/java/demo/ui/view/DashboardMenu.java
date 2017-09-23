package demo.ui.view;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.themes.ValoTheme;
import demo.domain.User;
import demo.ui.DashboardUI;
import demo.ui.component.ProfilePreferencesWindow;
import demo.ui.event.DashboardEvent.NotificationsCountUpdatedEvent;
import demo.ui.event.DashboardEvent.PostViewChangeEvent;
import demo.ui.event.DashboardEvent.ProfileUpdatedEvent;
import demo.ui.event.DashboardEvent.UserLoggedOutEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

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

    @Autowired
    public DashboardMenu(EventBus.UIEventBus dashboardEventBus, ProfilePreferencesWindow profilePreferencesWindow) {
        this.dashboardEventBus = dashboardEventBus;
        this.profilePreferencesWindow = profilePreferencesWindow;

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
        final CssLayout menuContent = new CssLayout();
        menuContent.addStyleNames(
                "sidebar",
                ValoTheme.DRAG_AND_DROP_WRAPPER_NO_VERTICAL_DRAG_HINTS,
                ValoTheme.DRAG_AND_DROP_WRAPPER_NO_HORIZONTAL_DRAG_HINTS,
                ValoTheme.MENU_PART
        );

        menuContent.setWidthUndefined();
        menuContent.setHeight("100%");

        menuContent.addComponents(buildTitle(), buildUserMenu(), buildToggleButton(), buildMenuItems());

        return menuContent;
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
        return (User) VaadinSession.getCurrent()
                .getAttribute(User.class.getName());
    }

    private Component buildUserMenu() {
        final MenuBar settings = new MenuBar();
        settings.addStyleName("user-menu");

        settingsItem = settings.addItem("",
                new ThemeResource("img/profile-pic-300px.jpg"), null);
        updateUserName(null);
        settingsItem.addItem("Edit Profile", new Command() {
            @Override
            public void menuSelected(final MenuItem selectedItem) {
                profilePreferencesWindow.open(getCurrentUser(), false);
            }
        });
        settingsItem.addItem("Preferences", new Command() {
            @Override
            public void menuSelected(final MenuItem selectedItem) {
                profilePreferencesWindow.open(getCurrentUser(), true);
            }
        });
        settingsItem.addSeparator();
        settingsItem.addItem("Sign Out", new Command() {
            @Override
            public void menuSelected(final MenuItem selectedItem) {
                dashboardEventBus.publish(this, new UserLoggedOutEvent());
            }
        });
        return settings;
    }

    private Component buildToggleButton() {
        Button valoMenuToggleButton = new Button("Menu", new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                if (getCompositionRoot().getStyleName()
                        .contains(STYLE_VISIBLE)) {
                    getCompositionRoot().removeStyleName(STYLE_VISIBLE);
                } else {
                    getCompositionRoot().addStyleName(STYLE_VISIBLE);
                }
            }
        });
        valoMenuToggleButton.setIcon(VaadinIcons.LIST);
        valoMenuToggleButton.addStyleName("valo-menu-toggle");
        valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_SMALL);
        return valoMenuToggleButton;
    }

    private Component buildMenuItems() {
        CssLayout menuItemsLayout = new CssLayout();
        menuItemsLayout.addStyleName("valo-menuitems");

        for (final DashboardViewType view : DashboardViewType.values()) {
            Component menuItemComponent = new ValoMenuItemButton(view);

            if (view == DashboardViewType.DASHBOARD) {
                notificationsBadge = new Label();
                notificationsBadge.setId(NOTIFICATIONS_BADGE_ID);
                menuItemComponent = buildBadgeWrapper(menuItemComponent,
                        notificationsBadge);
            }

            menuItemsLayout.addComponent(menuItemComponent);
        }
        return menuItemsLayout;

    }

    private Component buildBadgeWrapper(final Component menuItemButton,
                                        final Component badgeLabel) {
        CssLayout dashboardWrapper = new CssLayout(menuItemButton);
        dashboardWrapper.addStyleName("badgewrapper");
        dashboardWrapper.addStyleName(ValoTheme.MENU_ITEM);
        badgeLabel.addStyleName(ValoTheme.MENU_BADGE);
        badgeLabel.setWidthUndefined();
        badgeLabel.setVisible(false);
        dashboardWrapper.addComponent(badgeLabel);
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
    public void updateNotificationsCount(
            final NotificationsCountUpdatedEvent event) {
        long unreadNotificationsCount = DashboardUI.getDataProvider().getUnreadNotificationsCount();
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
            addClickListener((ClickListener) event -> UI.getCurrent().getNavigator()
                    .navigateTo(view.getViewName()));

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