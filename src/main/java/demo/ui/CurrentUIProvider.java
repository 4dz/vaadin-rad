package demo.ui;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.UI;

@SpringComponent
@UIScope
public class CurrentUIProvider {
    public UI getCurrentUI() {
        return UI.getCurrent();
    }
}
