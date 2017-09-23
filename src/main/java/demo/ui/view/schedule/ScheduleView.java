package demo.ui.view.schedule;

import com.vaadin.event.LayoutEvents;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.server.WebBrowser;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import demo.data.DataProvider;
import demo.domain.Movie;
import demo.ui.component.MovieDetailsWindow;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;

import javax.annotation.PostConstruct;

@SpringView
public class ScheduleView extends CssLayout implements View {

    private final EventBus.ViewEventBus dashboardEventBus;
    private final DataProvider dataProvider;
    private final MovieDetailsWindow movieDetailsWindow;

    @Autowired
    public ScheduleView(EventBus.ViewEventBus dashboardEventBus, DataProvider dataProvider, MovieDetailsWindow movieDetailsWindow) {
        this.dashboardEventBus = dashboardEventBus;
        this.dataProvider = dataProvider;
        this.movieDetailsWindow = movieDetailsWindow;

        setSizeFull();
        addStyleName("schedule");

    }

    @PostConstruct
    public void init() {
        dashboardEventBus.subscribe(this);
        TabSheet tabs = new TabSheet();
        tabs.setSizeFull();
        tabs.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);

        tabs.addComponent(buildCatalogView());
        addComponent(tabs);
        injectMovieCoverStyles();
    }

    @Override
    public void detach() {
        super.detach();
        // A new instance of ScheduleView is created every time it's navigated
        // to so we'll need to clean up references to it on detach.
        dashboardEventBus.unsubscribe(this);
    }

    private void injectMovieCoverStyles() {
        // Add all movie cover images as classes to CSSInject
        String styles = "";
        for (Movie m : dataProvider.getMovies()) {
            WebBrowser webBrowser = Page.getCurrent().getWebBrowser();

            String bg = "url(VAADIN/themes/" + UI.getCurrent().getTheme()
                    + "/img/event-title-bg.png), url(" + m.getThumbUrl() + ")";

            // IE8 doesn't support multiple background images
            if (webBrowser.isIE() && webBrowser.getBrowserMajorVersion() == 8) {
                bg = "url(" + m.getThumbUrl() + ")";
            }

            styles += ".v-calendar-event-" + m.getId()
                    + " .v-calendar-event-content {background-image:" + bg
                    + ";}";
        }

        Page.getCurrent().getStyles().add(styles);
    }


    private Component buildCatalogView() {
        CssLayout catalog = new CssLayout();
        catalog.setCaption("Catalog");
        catalog.addStyleName("catalog");

        for (final Movie movie : dataProvider.getMovies()) {
            VerticalLayout frame = new VerticalLayout();
            frame.addStyleName("frame");
            frame.setWidthUndefined();
            frame.setMargin(false);
            frame.setSpacing(false);

            Image poster = new Image(null,
                    new ExternalResource(movie.getThumbUrl()));
            poster.setWidth(100.0f, Unit.PIXELS);
            poster.setHeight(145.0f, Unit.PIXELS);
            frame.addComponent(poster);

            Label titleLabel = new Label(movie.getTitle());
            titleLabel.setWidth(120.0f, Unit.PIXELS);
            frame.addComponent(titleLabel);

            frame.addLayoutClickListener((LayoutEvents.LayoutClickListener) event -> {
                if (event.getButton() == MouseEventDetails.MouseButton.LEFT) {
                    movieDetailsWindow.open(movie, null, null);
                }
            });
            catalog.addComponent(frame);
        }
        return catalog;
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {
    }
}
