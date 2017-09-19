package demo.ui.view;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/*
 * Dashboard MainLayout is a simple HorizontalLayout that wraps the menu on the
 * left and creates a simple container for the navigator on the right.
 */
@SpringComponent
@UIScope
@SuppressWarnings("serial")
public class MainLayout extends HorizontalLayout {

    private final ComponentContainer content;

    @Autowired
    DashboardMenu dashboardMenu;

    public MainLayout() {
        setSizeFull();
        addStyleName("mainview");
        setSpacing(false);


        content = new CssLayout();
        content.addStyleName("view-content");
        content.setSizeFull();
    }

    @PostConstruct
    public void init() {
        addComponent(dashboardMenu);
        addComponent(content);
        setExpandRatio(content, 1.0f);
    }

    public void setContent(Component component) {
        if(content.getComponentCount() != 0) {
            content.replaceComponent(content.iterator().next(), component);
        } else {
            content.addComponent(component);
        }
    }
}