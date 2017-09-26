package demo.ui.component;

import com.vaadin.server.Page;
import com.vaadin.ui.*;
import demo.data.DataProvider;
import demo.domain.CinemaBooking;
import demo.ui.CurrentUIProvider;
import demo.ui.event.DashboardEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.vaadin.spring.events.EventBus;

import java.util.Iterator;
import java.util.function.Predicate;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BookingWindowTest {
    @Mock
    private EventBus.UIEventBus eventBus;

    @Mock
    private DataProvider dataProvider;

    @Mock
    private CurrentUIProvider currentUIProvider;

    @Mock
    private UI currentUI;

    @Mock
    private Page currentPage;

    @Before
    public void setup() {
        when(currentUIProvider.getCurrentUI()).thenReturn(currentUI);
        doAnswer((Answer<Void>) invocationOnMock -> {
            invocationOnMock.getArgumentAt(0, Window.class).setParent(currentUI);
            return null;
        }).when(currentUI).addWindow(Matchers.any(Window.class));
        when(currentUI.getConnectorTracker()).thenReturn(mock(ConnectorTracker.class));
        when(currentUI.getPage()).thenReturn(currentPage);
    }

    @Test
    public void shouldCloseOtherWindows_WhenBookingWindowOpened() {
        BookingWindow window = new BookingWindow(eventBus, dataProvider, currentUIProvider);
        window.open();
        verify(eventBus).publish(Matchers.eq(window),Matchers.any(DashboardEvent.CloseOpenWindowsEvent.class));
    }

    @Test
    public void shouldDisplayBookingWindow_WhenWindowOpened() {
        BookingWindow window = new BookingWindow(eventBus, dataProvider, currentUIProvider);
        window.open();
        verify(currentUI).addWindow(Matchers.eq(window));
    }

    @Test
    public void shouldCreateCinemaBooking_WhenWindowOpened() {
        BookingWindow window = new BookingWindow(eventBus, dataProvider, currentUIProvider);
        assertThat(window.getCinemaBooking(), nullValue());
        window.open();
        assertThat(window.getCinemaBooking(), not(nullValue()));
    }

    @Test
    public void shouldAddFirstNameFieldToForm_WhenWindowOpened() {
        BookingWindow window = new BookingWindow(eventBus, dataProvider, currentUIProvider);
        window.open();

        TextField field = findComponent(window.getContent(), TextField.class, t->"First Name".equals(t.getCaption()));

        assertThat(field, not(nullValue()));
    }

    @Test
    public void shouldDisplayNotification_WhenSaveButtonClicked() {
        BookingWindow window = new BookingWindow(eventBus, dataProvider, currentUIProvider);
        window.open();

        Button okButton = findComponent(window.getContent(), Button.class, button->"Save".equals(button.getCaption()));
        okButton.click();

        verify(currentPage).showNotification(Matchers.any(Notification.class));
    }


    @Test
    public void shouldSaveBooking_WhenSaveButtonClicked() {
        BookingWindow window = new BookingWindow(eventBus, dataProvider, currentUIProvider);
        window.open();

        Button okButton = findComponent(window.getContent(), Button.class, button->"Save".equals(button.getCaption()));
        okButton.click();

        verify(dataProvider).save(Matchers.any(CinemaBooking.class));
    }

    private static <T> T findComponent(Component parent, Class<T> type, Predicate<T> test) {

        if(type.isInstance(parent) && test.test((T)parent)) {
            return (T)parent;
        }

        if(parent instanceof HasComponents) {
            HasComponents container = (HasComponents) parent;

            Iterator<Component> it = container.iterator();
            while(it.hasNext()) {
                T result = findComponent(it.next(), type, test);
                if(result!=null) {
                    return result;
                }
            }

        }

        return null;
    }

}