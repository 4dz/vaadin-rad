package demo.ui.view;


import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import demo.ui.event.DashboardEvent;
import demo.ui.event.DashboardEvent.UserLoginRequestedEvent;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

@SuppressWarnings("serial")
public class LoginLayout extends VerticalLayout {

    private final EventBus dashboardEventBus;

    private TextField username;
    private PasswordField password;
    private Label loginFailedLabel;
    private Label loggedOutLabel;

    private CheckBox rememberMe;

    public LoginLayout(EventBus dashboardEventBus, boolean loggedOut) {
        this.dashboardEventBus=dashboardEventBus;
        dashboardEventBus.subscribe(this);

        setSizeFull();
        setMargin(false);
        setSpacing(false);

        Component loginForm = buildLoginForm(loggedOut);
        addComponent(loginForm);
        setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);

        Notification notification = new Notification(
                "Welcome to Dashboard Demo");
        notification
                .setDescription("<span>This application is not real, it only demonstrates an application built with the <a href=\"https://vaadin.com\">Vaadin framework</a>.</span> <span>No username or password is required, just click the <b>Sign In</b> button to continue.</span>");
        notification.setHtmlContentAllowed(true);
        notification.setStyleName("tray dark small closable login-help");
        notification.setPosition(Position.BOTTOM_CENTER);
        notification.setDelayMsec(20000);
        notification.show(Page.getCurrent());
    }

    private Component buildLoginForm(boolean loggedOut) {
        final VerticalLayout loginPanel = new VerticalLayout();
        loginPanel.setSizeUndefined();
        loginPanel.setMargin(false);
        Responsive.makeResponsive(loginPanel);
        loginPanel.addStyleName("login-panel");

        loginPanel.addComponent(buildLabels());
        loginPanel.addComponent(buildFields());
        loginPanel.addComponent(rememberMe = new CheckBox("Remember me", true));

        loginPanel.addComponent(loginFailedLabel = new Label());
        loginPanel.setComponentAlignment(loginFailedLabel, Alignment.BOTTOM_CENTER);
        loginFailedLabel.setSizeUndefined();
        loginFailedLabel.addStyleName(ValoTheme.LABEL_FAILURE);
        loginFailedLabel.setVisible(false);

        if (loggedOut) {
            loggedOutLabel = new Label("You have been logged out!");
            loggedOutLabel.addStyleName(ValoTheme.LABEL_SUCCESS);
            loggedOutLabel.setSizeUndefined();
            loginPanel.addComponent(loggedOutLabel);
            loginPanel.setComponentAlignment(loggedOutLabel, Alignment.BOTTOM_CENTER);
        }

        return loginPanel;
    }

    private Component buildFields() {
        HorizontalLayout fields = new HorizontalLayout();
        fields.addStyleName("fields");

        username = new TextField("Username");
        username.setIcon(VaadinIcons.USER);
        username.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        password = new PasswordField("Password");
        password.setIcon(VaadinIcons.LOCK);
        password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        final Button signin = new Button("Sign In");
        signin.addStyleName(ValoTheme.BUTTON_PRIMARY);
        signin.setClickShortcut(KeyCode.ENTER);
        signin.focus();

        fields.addComponents(username, password, signin);
        fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);

        signin.addClickListener(
                event -> dashboardEventBus.publish(this, new UserLoginRequestedEvent(username.getValue(), password.getValue(), rememberMe.getValue())));
        return fields;
    }

    private Component buildLabels() {
        CssLayout labels = new CssLayout();
        labels.addStyleName("labels");

        Label welcome = new Label("Welcome");
        welcome.setSizeUndefined();
        welcome.addStyleName(ValoTheme.LABEL_H4);
        welcome.addStyleName(ValoTheme.LABEL_COLORED);
        labels.addComponent(welcome);

        Label title = new Label("QuickTickets Dashboard");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H3);
        title.addStyleName(ValoTheme.LABEL_LIGHT);
        labels.addComponent(title);
        return labels;
    }

    @EventBusListenerMethod
    public void userLoginRequested(final DashboardEvent.UserLoginFailedEvent event) {
        username.focus();
        username.selectAll();
        password.setValue("");
        loginFailedLabel.setValue(String.format("Login failed: %s", event.getMessage()));
        loginFailedLabel.setVisible(true);
        if (loggedOutLabel != null) {
            loggedOutLabel.setVisible(false);
        }
    }

}