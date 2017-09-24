package demo.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import demo.ui.event.DashboardEvent;
import demo.ui.view.LoginLayout;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.security.shared.VaadinSharedSecurity;

/**
 * UI for the login screen.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@SpringUI(path = "/login")
@Theme("dashboard")
public class LoginUI extends UI {

    private final VaadinSharedSecurity vaadinSecurity;
    private final EventBus.UIEventBus eventBus;

    @Autowired
    public LoginUI(VaadinSharedSecurity vaadinSecurity, EventBus.UIEventBus eventBus) {
        this.vaadinSecurity = vaadinSecurity;
        this.eventBus = eventBus;
        eventBus.subscribe(this);
    }

    @Override
    protected void init(VaadinRequest request) {
        getPage().setTitle("Vaadin Shared Security Demo Login");

        Responsive.makeResponsive(this);
        addStyleNames(ValoTheme.UI_WITH_MENU, "loginview");

        setContent(new LoginLayout(eventBus));

        setSizeFull();
    }

    @EventBusListenerMethod
    public void userLoginRequested(final DashboardEvent.UserLoginRequestedEvent event) {

//        User user = dataProvider.authenticate(event.getUserName(), event.getPassword());
//        VaadinSession.getCurrent().setAttribute(User.class.getName(), user);
//        updateContent();

        try {
            vaadinSecurity.login(event.getUserName(), event.getPassword(), event.isRememberMe());
        } catch (AuthenticationException ex) {
            eventBus.publish(this, new DashboardEvent.UserLoginFailedEvent(ex.getMessage()));
        } catch (Exception ex) {
            Notification.show("An unexpected error occurred", ex.getMessage(), Notification.Type.ERROR_MESSAGE);
            LoggerFactory.getLogger(getClass()).error("Unexpected error while logging in", ex);
        }
    }
}