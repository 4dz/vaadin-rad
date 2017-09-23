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
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;
import demo.domain.User;
import demo.ui.event.DashboardEvent.CloseOpenWindowsEvent;
import demo.ui.event.DashboardEvent.ProfileUpdatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;

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

    private Component buildProfileTab() {
        HorizontalLayout root = new HorizontalLayout();
        root.setCaption("Profile");
        root.setIcon(VaadinIcons.USER);
        root.setWidth(100.0f, Unit.PERCENTAGE);
        root.setMargin(true);
        root.addStyleName("profile-form");

        VerticalLayout pic = new VerticalLayout();
        pic.setSizeUndefined();
        pic.setSpacing(true);
        Image profilePic = new Image(null,
                new ThemeResource("img/profile-pic-300px.jpg"));
        profilePic.setWidth(100.0f, Unit.PIXELS);
        pic.addComponent(profilePic);

        Button upload = new Button("Change…", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                Notification.show("Not implemented in this demo");
            }
        });
        upload.addStyleName(ValoTheme.BUTTON_TINY);
        pic.addComponent(upload);

        root.addComponent(pic);

        FormLayout details = new FormLayout();
        details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        root.addComponent(details);
        root.setExpandRatio(details, 1);

        firstNameField = new TextField("First Name");
        details.addComponent(firstNameField);
        lastNameField = new TextField("Last Name");
        details.addComponent(lastNameField);

        titleField = new ComboBox<>("Title",
                Arrays.asList("Mr.", "Mrs.", "Ms."));
        titleField.setPlaceholder("Please specify");
        details.addComponent(titleField);

        sexField = new RadioButtonGroup<>("Sex", Arrays.asList(true, false));
        sexField.setItemCaptionGenerator(item -> item ? "Male" : "Female");
        sexField.addStyleName("horizontal");
        details.addComponent(sexField);

        Label section = new Label("Contact Info");
        section.addStyleName(ValoTheme.LABEL_H4);
        section.addStyleName(ValoTheme.LABEL_COLORED);
        details.addComponent(section);

        emailField = new TextField("Email");
        emailField.setWidth("100%");
        emailField.setRequiredIndicatorVisible(true);
        // TODO add validation that not empty, use binder
        details.addComponent(emailField);

        locationField = new TextField("Location");
        locationField.setWidth("100%");
        locationField
                .setComponentError(new UserError("This address doesn't exist"));
        details.addComponent(locationField);

        phoneField = new TextField("Phone");
        phoneField.setWidth("100%");
        details.addComponent(phoneField);

        newsletterField = new OptionalSelect<>();
        newsletterField.addOption(0, "Daily");
        newsletterField.addOption(1, "Weekly");
        newsletterField.addOption(2, "Monthly");
        details.addComponent(newsletterField);

        section = new Label("Additional Info");
        section.addStyleName(ValoTheme.LABEL_H4);
        section.addStyleName(ValoTheme.LABEL_COLORED);
        details.addComponent(section);

        websiteField = new TextField("Website");
        websiteField.setPlaceholder("http://");
        websiteField.setWidth("100%");
        details.addComponent(websiteField);

        bioField = new TextArea("Bio");
        bioField.setWidth("100%");
        bioField.setRows(4);
        details.addComponent(bioField);

        return root;
    }

    private Component buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Unit.PERCENTAGE);
        footer.setSpacing(false);

        Button ok = new Button("OK");
        ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
        ok.addClickListener((ClickListener) event -> {
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
        footer.addComponent(ok);
        footer.setComponentAlignment(ok, Alignment.TOP_RIGHT);
        return footer;
    }

    public void open(final User user,
                            final boolean preferencesTabActive) {
        dashboardEventBus.publish(this, new CloseOpenWindowsEvent());
        //Window w = new ProfilePreferencesWindow(user, preferencesTabActive);

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