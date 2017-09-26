package demo.ui.component;

import com.vaadin.annotations.PropertyId;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import demo.data.DataProvider;
import demo.domain.CinemaBooking;
import demo.domain.Movie;
import demo.ui.CurrentUIProvider;
import demo.ui.event.DashboardEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MPanel;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;

@SpringComponent
@UIScope
public class BookingWindow extends Window {

    private final MVerticalLayout content;
    private final EventBus.UIEventBus dashboardEventBus;
    private final DataProvider dataProvider;
    private final BeanValidationBinder<CinemaBooking> fieldGroup;
    private final CurrentUIProvider currentUIProvider;

    @PropertyId("firstName")
    private TextField firstNameField;

    @PropertyId("lastName")
    private TextField lastNameField;

    @PropertyId("showingDate")
    private DateTimeField showingDate;

    @PropertyId("numberSeats")
    private ComboBox<Integer> seatsComboBox;

    @PropertyId("movie")
    private ComboBox<MovieAdapter> movieComboBox;


    @Autowired
    public BookingWindow(EventBus.UIEventBus eventBus, DataProvider dataProvider, CurrentUIProvider currentUIProvider) {
        this.dashboardEventBus = eventBus;
        this.dataProvider = dataProvider;
        this.currentUIProvider = currentUIProvider;

        addStyleNames("profile-window");
        Responsive.makeResponsive(this);
        center();
        addCloseShortcut(ShortcutAction.KeyCode.ESCAPE);

        setModal(true);
        setResizable(false);
        setHeight(90.0f, Unit.PERCENTAGE);
        setCaption("New Booking");
        setWidthUndefined();

        fieldGroup = new BeanValidationBinder<>(CinemaBooking.class);

        content = new MVerticalLayout().withFullSize();

        setContent(content);
    }

    public void open() {
        dashboardEventBus.publish(this, new DashboardEvent.CloseOpenWindowsEvent());

        content.removeAllComponents();
        MPanel formWrapper = new MPanel(buildForm()).withFullSize().withStyleName(ValoTheme.PANEL_BORDERLESS, "scroll-divider");
        content.expand(formWrapper);
        content.addComponent(buildFooter());

        fieldGroup.setBean(new CinemaBooking());

        //UI.getCurrent().addWindow(this);
        currentUIProvider.getCurrentUI().addWindow(this);
        this.focus();
    }

    private Component buildForm() {
        initialiseFields();

        return new MFormLayout(
                firstNameField, lastNameField, movieComboBox,
                showingDate, seatsComboBox

        ).withStyleName(ValoTheme.FORMLAYOUT_LIGHT);
    }

    static class MovieAdapter {
        Movie movie;
        public MovieAdapter(Movie m) { this.movie = m; }
        public String toString() { return movie.getTitle(); }
        public Movie getMovie() { return movie; }
    }

    private void initialiseFields() {
        firstNameField = new TextField("First Name");
        lastNameField = new TextField("Last Name");
        showingDate = new DateTimeField("Showing");

        seatsComboBox = new ComboBox<>("Number of Seats");
        seatsComboBox.setItems(Arrays.asList(1,2,3,4,5));

        movieComboBox = new ComboBox<>("Movie");
        movieComboBox.setItems(dataProvider.getMovies().stream().map(MovieAdapter::new));

        fieldGroup.bind(firstNameField, "firstName");
        fieldGroup.bind(lastNameField, "firstName");
        fieldGroup.bind(seatsComboBox, "numberSeats");
        fieldGroup.bind(movieComboBox, b -> new MovieAdapter(b.getMovie()), (b,a) -> b.setMovie(a.getMovie()));
        fieldGroup.bind(showingDate, b -> LocalDateTime.ofInstant(b.getShowingDate().toInstant(), ZoneId.systemDefault()), (b,t) -> b.setShowingDate(Date.from(t.atZone(ZoneId.systemDefault()).toInstant())));

    }

    private Component buildFooter() {
        MButton ok = new MButton("Save").withStyleName(ValoTheme.BUTTON_PRIMARY);
        MHorizontalLayout footer = new MHorizontalLayout(ok)
                .withStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR)
                .withFullWidth()
                .withComponentAlignment(ok, Alignment.TOP_RIGHT);

        ok.addClickListener(event -> saveAndClose());
        ok.focus();

        return footer;
    }

    private void saveAndClose() {
        if(fieldGroup.validate().isOk()) {
            // Updated user should also be persisted to database. But
            // not in this demo.

            dataProvider.save(getCinemaBooking());

            Notification success = new Notification("Booking Added successfully");
            success.setDelayMsec(2000);
            success.setStyleName("bar success small");
            success.setPosition(Position.BOTTOM_CENTER);
            success.show(currentUIProvider.getCurrentUI().getPage());

            close();
        } else {
            Notification.show("Error while updating profile",
                    Notification.Type.ERROR_MESSAGE);
        }

    }

    public CinemaBooking getCinemaBooking() {
        return fieldGroup.getBean();
    }

}
