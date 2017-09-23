package demo.ui.view.dashboard;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.themes.ValoTheme;
import demo.data.DataProvider;
import demo.domain.DashboardNotification;
import demo.domain.MovieRevenue;
import demo.ui.DashboardUI;
import demo.ui.component.TopTenMoviesTable;
import demo.ui.event.DashboardEvent.CloseOpenWindowsEvent;
import demo.ui.event.DashboardEvent.NotificationsCountUpdatedEvent;
import demo.ui.view.dashboard.DashboardEdit.DashboardEditListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@SpringView(name="")
@SuppressWarnings("serial")
public final class DashboardView extends Panel implements View,
        DashboardEditListener {

    private final EventBus.ViewEventBus dashboardEventBus;
    private final DataProvider dataProvider;

    public static final String EDIT_ID = "dashboard-edit";
    public static final String TITLE_ID = "dashboard-title";

    private Label titleLabel;
    private NotificationsButton notificationsButton;
    private CssLayout dashboardPanels;
    private final VerticalLayout root;
    private Window notificationsWindow;

    @Autowired
    public DashboardView(EventBus.ViewEventBus dashboardEventBus, DataProvider dataProvider) {
        this.dashboardEventBus = dashboardEventBus;
        this.dataProvider = dataProvider;

        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();

        root = new VerticalLayout();
        root.setSizeFull();
        root.setSpacing(false);
        root.addStyleName("dashboard-view");
        setContent(root);
        Responsive.makeResponsive(root);


    }

    @PostConstruct
    public void init() {
        // All the open sub-windows should be closed whenever the root layout
        // gets clicked.
        root.addLayoutClickListener(new LayoutClickListener() {
            @Override
            public void layoutClick(final LayoutClickEvent event) {
                dashboardEventBus.publish(this, new CloseOpenWindowsEvent());
            }
        });

        root.addComponent(buildHeader());

        Component content = buildContent();
        root.addComponent(content);
        root.setExpandRatio(content, 1);
    }

    private Component buildHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");

        titleLabel = new Label("Dashboard");
        titleLabel.setId(TITLE_ID);
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(titleLabel);

        notificationsButton = buildNotificationsButton();
        Component edit = buildEditButton();
        HorizontalLayout tools = new HorizontalLayout(notificationsButton, edit);
        tools.addStyleName("toolbar");
        header.addComponent(tools);

        return header;
    }

    private NotificationsButton buildNotificationsButton() {
        NotificationsButton result = new NotificationsButton();
        result.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                openNotificationsPopup(event);
            }
        });
        return result;
    }

    private Component buildEditButton() {
        Button result = new Button();
        result.setId(EDIT_ID);
        result.setIcon(VaadinIcons.EDIT);
        result.addStyleName("icon-edit");
        result.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        result.setDescription("Edit Dashboard");
        result.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                getUI().addWindow(
                        new DashboardEdit(DashboardView.this, titleLabel
                                .getValue()));
            }
        });
        return result;
    }

    private Component buildContent() {
        dashboardPanels = new CssLayout();
        dashboardPanels.addStyleName("dashboard-panels");
        Responsive.makeResponsive(dashboardPanels);

        dashboardPanels.addComponent(buildNotes());
        dashboardPanels.addComponent(buildTop10TitlesByRevenue());

        return dashboardPanels;
    }

    private Component buildNotes() {
        TextArea notes = new TextArea("Notes");
        notes.setValue("Remember to:\n· Zoom in and out in the Sales view\n· Filter the transactions and drag a set of them to the Reports tab\n· Create a new report\n· Change the schedule of the movie theater");
        notes.setSizeFull();
        notes.addStyleName(ValoTheme.TEXTAREA_BORDERLESS);
        Component panel = createContentWrapper(notes);
        panel.addStyleName("notes");
        return panel;
    }

    private Component buildTop10TitlesByRevenue() {
        List<MovieRevenue> movieRevenues = dataProvider.getTotalMovieRevenues(10);
        Collections.sort(movieRevenues, (rev1, rev2) -> rev2.getRevenue().compareTo(rev1.getRevenue()));

        Component contentWrapper = createContentWrapper(new TopTenMoviesTable(movieRevenues));
        contentWrapper.addStyleName("top10-revenue");
        return contentWrapper;
    }

    private Component createContentWrapper(final Component content) {
        final CssLayout slot = new CssLayout();
        slot.setWidth("100%");
        slot.addStyleName("dashboard-panel-slot");

        CssLayout card = new CssLayout();
        card.setWidth("100%");
        card.addStyleName(ValoTheme.LAYOUT_CARD);

        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.addStyleName("dashboard-panel-toolbar");
        toolbar.setWidth("100%");
        toolbar.setSpacing(false);

        Label caption = new Label(content.getCaption());
        caption.addStyleName(ValoTheme.LABEL_H4);
        caption.addStyleName(ValoTheme.LABEL_COLORED);
        caption.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        content.setCaption(null);

        MenuBar tools = new MenuBar();
        tools.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
        MenuItem max = tools.addItem("", VaadinIcons.EXPAND, selectedItem -> {
            if (!slot.getStyleName().contains("max")) {
                selectedItem.setIcon(VaadinIcons.COMPRESS);
                toggleMaximized(slot, true);
            } else {
                slot.removeStyleName("max");
                selectedItem.setIcon(VaadinIcons.EXPAND);
                toggleMaximized(slot, false);
            }
        });
        max.setStyleName("icon-only");
        MenuItem root = tools.addItem("", VaadinIcons.COG, null);
        root.addItem("Configure", selectedItem -> Notification.show("Not implemented in this demo"));
        root.addSeparator();
        root.addItem("Close", selectedItem -> Notification.show("Not implemented in this demo"));

        toolbar.addComponents(caption, tools);
        toolbar.setExpandRatio(caption, 1);
        toolbar.setComponentAlignment(caption, Alignment.MIDDLE_LEFT);

        card.addComponents(toolbar, content);
        slot.addComponent(card);
        return slot;
    }

    private void openNotificationsPopup(final ClickEvent event) {
        VerticalLayout notificationsLayout = new VerticalLayout();

        Label title = new Label("Notifications");
        title.addStyleName(ValoTheme.LABEL_H3);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        notificationsLayout.addComponent(title);

        Collection<DashboardNotification> notifications = DashboardUI
                .getDataProvider().getNotifications();
        dashboardEventBus.publish(this, new NotificationsCountUpdatedEvent());

        for (DashboardNotification notification : notifications) {
            VerticalLayout notificationLayout = new VerticalLayout();
            notificationLayout.setMargin(false);
            notificationLayout.setSpacing(false);
            notificationLayout.addStyleName("notification-item");

            Label titleLabel = new Label(notification.getFirstName() + " "
                    + notification.getLastName() + " "
                    + notification.getAction());
            titleLabel.addStyleName("notification-title");

            Label timeLabel = new Label(notification.getPrettyTime());
            timeLabel.addStyleName("notification-time");

            Label contentLabel = new Label(notification.getContent());
            contentLabel.addStyleName("notification-content");

            notificationLayout.addComponents(titleLabel, timeLabel,
                    contentLabel);
            notificationsLayout.addComponent(notificationLayout);
        }

        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth("100%");
        footer.setSpacing(false);
        Button showAll = new Button("View All Notifications",
                event1 -> Notification.show("Not implemented in this demo"));
        showAll.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        showAll.addStyleName(ValoTheme.BUTTON_SMALL);
        footer.addComponent(showAll);
        footer.setComponentAlignment(showAll, Alignment.TOP_CENTER);
        notificationsLayout.addComponent(footer);

        if (notificationsWindow == null) {
            notificationsWindow = new Window();
            notificationsWindow.setWidth(300.0f, Unit.PIXELS);
            notificationsWindow.addStyleName("notifications");
            notificationsWindow.setClosable(false);
            notificationsWindow.setResizable(false);
            notificationsWindow.setDraggable(false);
            notificationsWindow.addCloseShortcut(KeyCode.ESCAPE);
            notificationsWindow.setContent(notificationsLayout);
        }

        if (!notificationsWindow.isAttached()) {
            notificationsWindow.setPositionY(event.getClientY()
                    - event.getRelativeY() + 40);
            getUI().addWindow(notificationsWindow);
            notificationsWindow.focus();
        } else {
            notificationsWindow.close();
        }
    }

    @Override
    public void enter(final ViewChangeEvent event) {
        notificationsButton.updateNotificationsCount(null);
    }

    @Override
    public void dashboardNameEdited(final String name) {
        titleLabel.setValue(name);
    }

    private void toggleMaximized(final Component panel, final boolean maximized) {
        for (Iterator<Component> it = root.iterator(); it.hasNext();) {
            it.next().setVisible(!maximized);
        }
        dashboardPanels.setVisible(true);

        for (Iterator<Component> it = dashboardPanels.iterator(); it.hasNext();) {
            Component c = it.next();
            c.setVisible(!maximized);
        }

        if (maximized) {
            panel.setVisible(true);
            panel.addStyleName("max");
        } else {
            panel.removeStyleName("max");
        }
    }

    public final class NotificationsButton extends Button {
        private static final String STYLE_UNREAD = "unread";
        public static final String ID = "dashboard-notifications";

        public NotificationsButton() {
            setIcon(VaadinIcons.BELL);
            setId(ID);
            addStyleName("notifications");
            addStyleName(ValoTheme.BUTTON_ICON_ONLY);
            dashboardEventBus.subscribe(this);
        }

        @EventBusListenerMethod
        public void updateNotificationsCount(
                final NotificationsCountUpdatedEvent event) {
            setUnreadCount(DashboardUI.getDataProvider().getUnreadNotificationsCount());
        }

        public void setUnreadCount(final long count) {
            setCaption(String.valueOf(count));

            String description = "Notifications";
            if (count > 0) {
                addStyleName(STYLE_UNREAD);
                description += " (" + count + " unread)";
            } else {
                removeStyleName(STYLE_UNREAD);
            }
            setDescription(description);
        }
    }

}