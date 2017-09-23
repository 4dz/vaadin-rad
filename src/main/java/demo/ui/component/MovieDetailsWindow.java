package demo.ui.component;

import com.vaadin.event.ShortcutAction;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import demo.domain.Movie;
import demo.ui.event.DashboardEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MPanel;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringComponent
@UIScope
public class MovieDetailsWindow extends Window {

    private final EventBus.UIEventBus dashboardEventBus;

    private final MLabel synopsis = new MLabel();

    private final MVerticalLayout content;

    @Autowired
    public MovieDetailsWindow(EventBus.UIEventBus dashboardEventBus) {
        this.dashboardEventBus = dashboardEventBus;

        addStyleName("moviedetailswindow");
        Responsive.makeResponsive(this);

        center();
        addCloseShortcut(ShortcutAction.KeyCode.ESCAPE);
        setResizable(false);
        setClosable(false);
        setHeight(90.0f, Unit.PERCENTAGE);

        content = new MVerticalLayout().withFullSize();

        setContent(content);
//        content.setMargin(false);
//        content.setSpacing(false);

    }

    private Component buildFooter() {
        MButton ok = new MButton("Close").withStyleName(ValoTheme.BUTTON_PRIMARY);
        MHorizontalLayout footer = new MHorizontalLayout(ok)
                .withStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR)
                .withFullWidth()
                .withComponentAlignment(ok, Alignment.TOP_RIGHT);

        //footer.setSpacing(false);
        ok.addClickListener(event -> close());
        ok.focus();

        return footer;
    }

    private Component buildMovieDetails(final Movie movie, final Date startTime, final Date endTime) {
        final Image coverImage = new Image(null, new ExternalResource(movie.getThumbUrl()));
        coverImage.addStyleName("cover");

        return new MHorizontalLayout(coverImage).withFullWidth()
                .withStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING)
                .expand(buildDetailsForm(movie, startTime, endTime));
    }

    private Component buildDetailsForm(final Movie movie, final Date startTime, final Date endTime) {
        MFormLayout fields = new MFormLayout();
//        fields.setSpacing(false);
//        fields.setMargin(false);

        SimpleDateFormat date = new SimpleDateFormat("dd-mm-yyyy");
        SimpleDateFormat time = new SimpleDateFormat("hh:mm a");
        if (startTime != null) {
            fields.addComponent(new MLabel("Date", date.format(startTime)).withUndefinedSize());
            fields.addComponent(new MLabel("Starts", time.format(startTime)).withUndefinedSize());
        }

        if (endTime != null) {
            fields.addComponent(new MLabel("Ends", time.format(endTime)).withUndefinedSize());
        }

        fields.addComponent(new MLabel("Duration", movie.getDuration() + " minutes").withUndefinedSize());

        synopsis.setData(movie.getSynopsis());
        synopsis.setCaption("Synopsis");
        synopsis.setSizeFull();
        updateSynopsis(movie, false);

        final MButton more = new MButton("More…")
            .withStyleName(ValoTheme.BUTTON_LINK);

        fields.addComponents(synopsis, more);

        more.addClickListener(event -> {
            updateSynopsis(null, true);
            event.getButton().setVisible(false);
            MovieDetailsWindow.this.focus();
        });

        return fields;
    }

    private void updateSynopsis(final Movie m, final boolean expand) {
        String synopsisText = synopsis.getData().toString();
        if (m != null) {
            synopsisText = m.getSynopsis();
            synopsis.setData(m.getSynopsis());
        }
        if (!expand) {
            synopsisText = synopsisText.length() > 300 ? synopsisText
                    .substring(0, 300) + "…" : synopsisText;

        }
        synopsis.setValue(synopsisText);
    }

    public void open(final Movie movie, final Date startTime, final Date endTime) {
        dashboardEventBus.publish(this, new DashboardEvent.CloseOpenWindowsEvent());
        //Window w = new MovieDetailsWindow(movie, startTime, endTime);
        setCaption(movie.getTitle());
        content.removeAllComponents();
        MPanel detailsWrapper = new MPanel(buildMovieDetails(movie, startTime, endTime)).withFullSize().withStyleName(ValoTheme.PANEL_BORDERLESS, "scroll-divider");
        content.expand(detailsWrapper);
        content.addComponent(buildFooter());

        UI.getCurrent().addWindow(this);
        this.focus();
    }
}
