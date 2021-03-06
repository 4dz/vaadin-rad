package demo.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.server.*;
import com.vaadin.server.Page.BrowserWindowResizeEvent;
import com.vaadin.server.Page.BrowserWindowResizeListener;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import demo.data.DataProvider;
import demo.ui.event.DashboardEvent.BrowserResizeEvent;
import demo.ui.event.DashboardEvent.CloseOpenWindowsEvent;
import demo.ui.event.DashboardEvent.UserLoggedOutEvent;
import demo.ui.view.AccessDeniedView;
import demo.ui.view.ErrorView;
import demo.ui.view.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.security.util.SecurityExceptionUtils;

import java.util.Locale;

@Theme("dashboard")
//@Widgetset("demo.DashboardWidgetSet")
@Title("QuickTickets Dashboard")
@SpringUI
@SpringViewDisplay
@SuppressWarnings("serial")
public final class DashboardUI extends UI implements ViewDisplay {

    /*
     * This field stores an access to the dummy backend layer. In real
     * applications you most likely gain access to your beans trough lookup or
     * injection; and not in the UI but somewhere closer to where they're
     * actually accessed.
     */
    private final EventBus.UIEventBus eventBus;
    private final MainLayout mainLayout;
    private final SpringViewProvider springViewProvider;

    @Autowired
    public DashboardUI(EventBus.UIEventBus eventBus, MainLayout mainLayout, SpringViewProvider springViewProvider) {
        this.eventBus = eventBus;
        this.mainLayout = mainLayout;
        this.springViewProvider = springViewProvider;
    }

    @Override
    protected void init(final VaadinRequest request) {
        setLocale(Locale.US);

        // Let's register a custom error handler to make the 'access denied' messages a bit friendlier.
        setErrorHandler(new DefaultErrorHandler() {
            @Override
            public void error(com.vaadin.server.ErrorEvent event) {
                if (SecurityExceptionUtils.isAccessDeniedException(event.getThrowable())) {
                    Notification.show("Sorry, you don't have access to do that.");
                } else {
                    super.error(event);
                }
            }
        });

        springViewProvider.setAccessDeniedViewClass(AccessDeniedView.class);
        getNavigator().setErrorView(ErrorView.class);

        eventBus.subscribe(this);
        Responsive.makeResponsive(this);
        addStyleName(ValoTheme.UI_WITH_MENU);

        updateContent();

        // Some views need to be aware of browser resize events so a
        // BrowserResizeEvent gets fired to the event bus on every occasion.
        Page.getCurrent().addBrowserWindowResizeListener(
                new BrowserWindowResizeListener() {
                    @Override
                    public void browserWindowResized(
                            final BrowserWindowResizeEvent event) {
                        eventBus.publish(this, new BrowserResizeEvent());
                    }
                });

    }

    /**
     * Updates the correct content for this UI based on the current user status.
     * If the user is logged in with appropriate privileges, main view is shown.
     * Otherwise login view is shown.
     */
    private void updateContent() {

        setContent(mainLayout);
        getNavigator().navigateTo(getNavigator().getState());

//        eventBus.publish(this, new DashboardEvent.ProfileUpdatedEvent());
    }

    @EventBusListenerMethod
    public void userLoggedOut(final UserLoggedOutEvent event) {
        // When the user logs out, current VaadinSession gets closed and the
        // page gets reloaded on the login screen. Do notice the this doesn't
        // invalidate the current HttpSession.
        VaadinSession.getCurrent().close();
        Page.getCurrent().reload();
    }

    @EventBusListenerMethod
    public void closeOpenWindows(final CloseOpenWindowsEvent event) {
        for (Window window : getWindows()) {
            window.close();
        }
    }

    @Override
    public void showView(View view) {
        mainLayout.setContent((Component)view);
    }
}