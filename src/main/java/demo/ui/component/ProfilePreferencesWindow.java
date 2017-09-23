package demo.ui.component;

import com.vaadin.annotations.PropertyId;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.UserError;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;
import demo.domain.User;
import demo.ui.event.DashboardEvent.CloseOpenWindowsEvent;
import demo.ui.event.DashboardEvent.ProfileUpdatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Arrays;

@SpringComponent
@UIScope
@SuppressWarnings("serial")
public class ProfilePreferencesWindow extends Window {

    public static final String ID = "profilepreferenceswindow";

    private final EventBus.UIEventBus dashboardEventBus;

    private final BeanValidationBinder<User> fieldGroup;
    /*
     * Fields for editing the User object are defined here as class members.
     * They are later bound to a FieldGroup by calling
     * fieldGroup.bindMemberFields(this). The Fields' values don't need to be
     * explicitly set, calling fieldGroup.setItemDataSource(user) synchronizes
     * the fields with the user object.
     */
    @PropertyId("firstName")
    private TextField firstNameField;
    @PropertyId("lastName")
    private TextField lastNameField;
    @PropertyId("title")
    private ComboBox<String> titleField;
    @PropertyId("male")
    private RadioButtonGroup<Boolean> sexField;
    @PropertyId("email")
    private TextField emailField;
    @PropertyId("location")
    private TextField locationField;
    @PropertyId("phone")
    private TextField phoneField;
    @PropertyId("newsletterSubscription")
    private OptionalSelect<Integer> newsletterField;
    @PropertyId("website")
    private TextField websiteField;
    @PropertyId("bio")
    private TextArea bioField;

    private final TabSheet detailsWrapper;

    @Autowired
    public ProfilePreferencesWindow(EventBus.UIEventBus dashboardEventBus) {
        this.dashboardEventBus = dashboardEventBus;

        addStyleName("profile-window");
        setId(ID);
        Responsive.makeResponsive(this);

        setModal(true);
        addCloseShortcut(KeyCode.ESCAPE, null);
        setResizable(false);
        setClosable(false);
        setHeight(90.0f, Unit.PERCENTAGE);

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.setMargin(new MarginInfo(true, false, false, false));
        content.setSpacing(false);
        setContent(content);

        detailsWrapper = new TabSheet();
        detailsWrapper.setSizeFull();
        detailsWrapper.addStyleNames(
                ValoTheme.TABSHEET_PADDED_TABBAR,
                ValoTheme.TABSHEET_ICONS_ON_TOP,
                ValoTheme.TABSHEET_CENTERED_TABS);
        content.addComponent(detailsWrapper);
        content.setExpandRatio(detailsWrapper, 1f);

        detailsWrapper.addComponents(buildProfileTab(),buildPreferencesTab());

        content.addComponent(buildFooter());

        fieldGroup = new BeanValidationBinder<>(User.class);
        fieldGroup.bindInstanceFields(this);

    }

    private Component buildPreferencesTab() {
        VerticalLayout root = new VerticalLayout();
        root.setCaption("Preferences");
        root.setIcon(VaadinIcons.COGS);
        root.setSpacing(true);
        root.setMargin(true);
        root.setSizeFull();

        Label message = new Label("Not implemented in this demo");
        message.setSizeUndefined();
        message.addStyleName(ValoTheme.LABEL_LIGHT);
        root.addComponent(message);
        root.setComponentAlignment(message, Alignment.MIDDLE_CENTER);

        return root;
    }

    private Component getProfilePicture() {
        Image profilePic = new Image(null, new ThemeResource("img/profile-pic-300px.jpg"));
        profilePic.setWidth(100.0f, Unit.PIXELS);

        MButton upload = new MButton(
                "Change…",
                event -> Notification.show("Not implemented in this demo"))
                .withStyleName(ValoTheme.BUTTON_TINY);

        return new MVerticalLayout(profilePic, upload).withSizeUndefined();

    }
    private Component buildProfileTab() {


        return new MHorizontalLayout(getProfilePicture())
                .withFullWidth().withCaption("Profile").withIcon(VaadinIcons.USER).withStyleName("profile-form")
                .expand(getProfileForm());
    }

    private Component getProfileForm() {
        initialiseFields();

        return new MFormLayout(
                firstNameField, lastNameField, titleField, sexField,

                new MLabel("Contact Info").withStyleName(ValoTheme.LABEL_H4, ValoTheme.LABEL_COLORED),
                emailField, locationField, phoneField, newsletterField, new MLabel(),

                new MLabel("Additional Info").withStyleName(ValoTheme.LABEL_H4, ValoTheme.LABEL_COLORED),
                websiteField, bioField

        ).withStyleName(ValoTheme.FORMLAYOUT_LIGHT);
    }

    private void initialiseFields() {
        firstNameField = new TextField("First Name");
        lastNameField = new TextField("Last Name");
        titleField = new ComboBox<>("Title", Arrays.asList("Mr.", "Mrs.", "Ms."));
        titleField.setPlaceholder("Please specify");
        sexField = new RadioButtonGroup<>("Sex", Arrays.asList(true, false));
        sexField.setItemCaptionGenerator(item -> item ? "Male" : "Female");
        sexField.addStyleName("horizontal");
        phoneField = new MTextField("Phone").withFullWidth();

        // TODO add validation that not empty, use binder
        emailField = new MTextField("Email").withFullWidth().withRequiredIndicatorVisible(true);
        locationField = new MTextField("Location").withFullWidth();
        locationField.setComponentError(new UserError("This address doesn't exist"));

        newsletterField = new OptionalSelect<Integer>()
                .withOption(0, "Daily")
                .withOption(1, "Weekly")
                .withOption(2, "Monthly");
        websiteField = new MTextField("Website").withPlaceholder("http://").withFullWidth();
        bioField = new TextArea("Bio");
        bioField.setWidth("100%");
        bioField.setRows(4);
    }

    private Component buildFooter() {


        //footer.setSpacing(false);

        MButton ok = new MButton("OK").withStyleName(ValoTheme.BUTTON_PRIMARY);
        ok.addClickListener(event -> {
            if(fieldGroup.validate().isOk()) {
                // Updated user should also be persisted to database. But
                // not in this demo.

                Notification success = new Notification(
                        "Profile updated successfully");
                success.setDelayMsec(2000);
                success.setStyleName("bar success small");
                success.setPosition(Position.BOTTOM_CENTER);
                success.show(Page.getCurrent());

                dashboardEventBus.publish(this, new ProfileUpdatedEvent());
                close();
            } else {
                Notification.show("Error while updating profile",
                        Type.ERROR_MESSAGE);
            }

        });
        ok.focus();

        MHorizontalLayout footer = new MHorizontalLayout(ok)
                .withFullWidth().withStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR).withAlign(ok, Alignment.TOP_RIGHT);
        return footer;
    }

    public void open(final User user, final boolean preferencesTabActive) {
        dashboardEventBus.publish(this, new CloseOpenWindowsEvent());

        if (preferencesTabActive) {
            detailsWrapper.setSelectedTab(1);
        } else {
            detailsWrapper.setSelectedTab(0);
        }

        fieldGroup.setBean(user);

        UI.getCurrent().addWindow(this);
        this.focus();
    }
}