package demo.ui.component;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.vaadin.spring.events.EventBus;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ProfilePreferencesWindowTest {

    @Mock
    EventBus.UIEventBus eventBus;

    @Test
    public void shouldAddLayoutWhenConstructing() {
        ProfilePreferencesWindow window = new ProfilePreferencesWindow(eventBus);
        assertThat(window.getComponentCount(), equalTo(1));
    }
}