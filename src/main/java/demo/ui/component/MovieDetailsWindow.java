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

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringComponent
@UIScope
public class MovieDetailsWindow extends Window {
    @Autowired
    EventBus.UIEventBus dashboardEventBus;

    private final Label synopsis = new Label();

    private final VerticalLayout content;

    private MovieDetailsWindow() {
        addStyleName("moviedetailswindow");
        Responsive.makeResponsive(this);

        center();
        addCloseShortcut(ShortcutAction.KeyCode.ESCAPE);
        setResizable(false);
        setClosable(false);
        setHeight(90.0f, Unit.PERCENTAGE);

        content = new VerticalLayout();
        content.setSizeFull();
        setContent(content);
        content.setMargin(false);
        content.setSpacing(false);

    }

    private Component buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Unit.PERCENTAGE);
        footer.setSpacing(false);

        Button ok = new Button("Close");
        ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
        ok.addClickListener(event -> close());
        ok.focus();
        footer.addComponent(ok);
        footer.setComponentAlignment(ok, Alignment.TOP_RIGHT);
        return footer;
    }

    private Component buildMovieDetails(final Movie movie,
                                        final Date startTime, final Date endTime) {
        HorizontalLayout details = new HorizontalLayout();
        details.setWidth(100.0f, Unit.PERCENTAGE);
        details.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        details.setMargin(true);

        final Image coverImage = new Image(null, new ExternalResource(
                movie.getThumbUrl()));
        coverImage.addStyleName("cover");
        details.addComponent(coverImage);

        Component detailsForm = buildDetailsForm(movie, startTime, endTime);
        details.addComponent(detailsForm);
        details.setExpandRatio(detailsForm, 1);

        return details;
    }

    private Component buildDetailsForm(final Movie movie, final Date startTime,
                                       final Date endTime) {
        FormLayout fields = new FormLayout();
        fields.setSpacing(false);
        fields.setMargin(false);

        Label label;
        SimpleDateFormat df = new SimpleDateFormat();
        if (startTime != null) {
            df.applyPattern("dd-mm-yyyy");
            label = new Label(df.format(startTime));
            label.setSizeUndefined();
            label.setCaption("Date");
            fields.addComponent(label);

            df.applyPattern("hh:mm a");
            label = new Label(df.format(startTime));
            label.setSizeUndefined();
            label.setCaption("Starts");
            fields.addComponent(label);
        }

        if (endTime != null) {
            label = new Label(df.format(endTime));
            label.setSizeUndefined();
            label.setCaption("Ends");
            fields.addComponent(label);
        }

        label = new Label(movie.getDuration() + " minutes");
        label.setSizeUndefined();
        label.setCaption("Duration");
        fields.addComponent(label);

        synopsis.setData(movie.getSynopsis());
        synopsis.setCaption("Synopsis");
        updateSynopsis(movie, false);
        fields.addComponent(synopsis);

        final Button more = new Button("More…");
        more.addStyleName(ValoTheme.BUTTON_LINK);
        fields.addComponent(more);
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
        Panel detailsWrapper = new Panel(buildMovieDetails(movie, startTime, endTime));
        detailsWrapper.setSizeFull();
        detailsWrapper.addStyleName(ValoTheme.PANEL_BORDERLESS);
        detailsWrapper.addStyleName("scroll-divider");
        content.addComponent(detailsWrapper);
        content.setExpandRatio(detailsWrapper, 1f);
        content.addComponent(buildFooter());

        UI.getCurrent().addWindow(this);
        this.focus();
    }
}
