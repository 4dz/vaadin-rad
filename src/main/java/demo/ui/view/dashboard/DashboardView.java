package demo.ui.view.dashboard;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.themes.ValoTheme;
import demo.data.DataProvider;
import demo.domain.DashboardNotification;
import demo.ui.component.TopTenMoviesTable;
import demo.ui.event.DashboardEvent.CloseOpenWindowsEvent;
import demo.ui.event.DashboardEvent.NotificationsCountUpdatedEvent;
import demo.ui.view.dashboard.DashboardEdit.DashboardEditListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

import javax.annotation.PostConstruct;

@SpringView(name="")
@SuppressWarnings("serial")
public class DashboardView extends Panel implements View,
        DashboardEditListener {

    private final EventBus.ViewEventBus dashboardEventBus;
    private final DataProvider dataProvider;

    private static final String EDIT_ID = "dashboard-edit";
    private static final String TITLE_ID = "dashboard-title";

    private MLabel titleLabel;
    private NotificationsButton notificationsButton;
    private MCssLayout dashboardPanels;
    private final MVerticalLayout root;
    private Window notificationsWindow;

    @Autowired
    public DashboardView(EventBus.ViewEventBus dashboardEventBus, DataProvider dataProvider) {
        this.dashboardEventBus = dashboardEventBus;
        this.dataProvider = dataProvider;

        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();

        root = new MVerticalLayout().withFullSize().withStyleName("dashboard-view");

        //root.setSpacing(false);

        setContent(root);
        Responsive.makeResponsive(root);
    }

    @PostConstruct
    public void init() {
        // All the open sub-windows should be closed whenever the root layout
        // gets clicked.
        root.addLayoutClickListener(e -> dashboardEventBus.publish(DashboardView.this, new CloseOpenWindowsEvent()));
        root.addComponent(buildHeader());
        root.expand(buildContent());
    }

    private Component buildHeader() {
        titleLabel = new MLabel("Dashboard").withId(TITLE_ID).withUndefinedSize().withStyleName(ValoTheme.LABEL_H1, ValoTheme.LABEL_NO_MARGIN);
        notificationsButton = buildNotificationsButton();

        return new MHorizontalLayout(
                titleLabel, new MHorizontalLayout(notificationsButton, buildEditButton()).withStyleName("toolbar"))
                .withStyleName("viewheader");
    }

    private NotificationsButton buildNotificationsButton() {
        NotificationsButton result = new NotificationsButton();
        result.addClickListener(this::openNotificationsPopup);
        return result;
    }

    private Component buildEditButton() {
        MButton result = new MButton().withId(EDIT_ID).withIcon(VaadinIcons.EDIT)
                .withStyleName("icon-edit",ValoTheme.BUTTON_ICON_ONLY)
                .withDescription("Edit Dashboard");

        result.addClickListener(event -> getUI().addWindow(
                new DashboardEdit(DashboardView.this, titleLabel.getValue())));

        return result;
    }

    private Component buildContent() {
        dashboardPanels = new MCssLayout(buildNotes(), buildTop10TitlesByRevenue()).withStyleName("dashboard-panels");
        Responsive.makeResponsive(dashboardPanels);

        return dashboardPanels;
    }

    private Component buildNotes() {
        TextArea notes = new TextArea("Notes");
        notes.setValue("Please try:\n· Filtering the transactions\n· Review the movie catalog\n· Make a booking\n· Login as guest, and note the Transactions view is not available");
        notes.setSizeFull();
        notes.addStyleName(ValoTheme.TEXTAREA_BORDERLESS);

        return createContentWrapper(notes, "notes");
    }

    private Component buildTop10TitlesByRevenue() {
        return createContentWrapper(new TopTenMoviesTable(dataProvider.getTotalMovieRevenues(10)), "top10-revenue");
    }

    private Component createContentWrapper(final Component content, String styleName) {

        final MCssLayout slot = new MCssLayout().withFullWidth().withStyleName("dashboard-panel-slot");

        MCssLayout card = new MCssLayout().withFullWidth().withStyleName(ValoTheme.LAYOUT_CARD);

        MHorizontalLayout toolbar = new MHorizontalLayout().withFullWidth().withStyleName("dashboard-panel-toolbar");

        //toolbar.setSpacing(false);

        MLabel caption = new MLabel(content.getCaption()).withStyleName(ValoTheme.LABEL_H4,ValoTheme.LABEL_COLORED,ValoTheme.LABEL_NO_MARGIN);

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

        toolbar.expand(caption).add(tools);
        toolbar.setComponentAlignment(caption, Alignment.MIDDLE_LEFT);

        card.addComponents(toolbar, content);
        slot.addComponent(card);

        slot.addStyleName(styleName);
        return slot;
    }

    private void openNotificationsPopup(final ClickEvent event) {


        MLabel title = new MLabel("Notifications").withStyleName(ValoTheme.LABEL_H3,ValoTheme.LABEL_NO_MARGIN);
        MVerticalLayout notificationsLayout = new MVerticalLayout(title);

        dashboardEventBus.publish(this, new NotificationsCountUpdatedEvent());

        for (DashboardNotification notification : dataProvider.getNotifications()) {

            MLabel titleLabel = new MLabel(
                    String.format("%s %s %s", notification.getFirstName(), notification.getLastName(), notification.getAction()))
                    .withStyleName("notification-title");


            notificationsLayout.addComponent(new MVerticalLayout(titleLabel,
                    new MLabel(notification.getPrettyTime()).withStyleName("notification-time"),
                    new MLabel(notification.getContent()).withStyleName("notification-content"))
                    .withMargin(false).withSpacing(false)
                    .withStyleName("notification-item"));
        }


        MButton showAll = new MButton("View All Notifications",
                e -> Notification.show("Not implemented in this demo"))
                .withStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED,ValoTheme.BUTTON_SMALL);

        MHorizontalLayout footer = new MHorizontalLayout(showAll).withStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR)
                .withFullWidth().withSpacing(false);

        footer.setComponentAlignment(showAll, Alignment.TOP_CENTER);
        notificationsLayout.addComponent(footer);

        if (notificationsWindow == null) {
            notificationsWindow = new MWindow("", notificationsLayout).withWidth(300.0f, Unit.PIXELS).withStyleName("notifications")
                    .withClosable(false).withResizable(false).withDraggable(false);

            notificationsWindow.addCloseShortcut(KeyCode.ESCAPE);
        }

        if (!notificationsWindow.isAttached()) {
            notificationsWindow.setPositionY(event.getClientY() - event.getRelativeY() + 40);
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
        root.forEach(c -> c.setVisible(!maximized));

        dashboardPanels.setVisible(true);
        dashboardPanels.forEach(p -> p.setVisible(!maximized));

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
            addStyleNames("notifications", ValoTheme.BUTTON_ICON_ONLY);
            dashboardEventBus.subscribe(this);
        }

        @EventBusListenerMethod
        public void updateNotificationsCount(final NotificationsCountUpdatedEvent event) {
            setUnreadCount(dataProvider.getUnreadNotificationsCount());
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