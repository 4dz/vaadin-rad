package demo.ui.view;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import javax.annotation.PostConstruct;

/*
 * Dashboard MainLayout is a simple HorizontalLayout that wraps the menu on the
 * left and creates a simple container for the navigator on the right.
 */
@SpringComponent
@UIScope
@SuppressWarnings("serial")
public class MainLayout extends MHorizontalLayout {

    private final ComponentContainer content;
    private final DashboardMenu dashboardMenu;

    public MainLayout(DashboardMenu dashboardMenu) {
        this.dashboardMenu = dashboardMenu;
        withFullSize().withStyleName("mainview").withSpacing(false);
        content = new MCssLayout().withStyleName("view-content").withFullSize();
    }

    @PostConstruct
    public void init() {
        withComponent(dashboardMenu).expand(content);
    }

    public void setContent(Component component) {
        if(content.getComponentCount() != 0) {
            content.replaceComponent(content.iterator().next(), component);
        } else {
            content.addComponent(component);
        }
    }
}