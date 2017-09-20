package demo.ui.component;

import java.util.LinkedHashMap;
import java.util.Map;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomField;

/*
 * This component extends a CustomField and implements all the necessary
 * functionality so that it can be used just like any other Field.
 */
@SuppressWarnings({ "serial", "unchecked" })
public final class OptionalSelect<T> extends CustomField<T> {
    private T value;
    private Map<T, String> captions;
    final CheckBox checkBox;
    final ComboBox<T> comboBox;

    @Override
    protected Component initContent() {
        comboBox.setItems(captions.keySet());
        comboBox.setItemCaptionGenerator(item -> captions.get(item));

        HorizontalLayout content = new HorizontalLayout();
        content.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        content.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        content.addComponent(comboBox);
        content.addComponent(checkBox, 0);
        return content;
    }

    public OptionalSelect() {
        captions = new LinkedHashMap<>();

        comboBox = new ComboBox();
        comboBox.setTextInputAllowed(false);
        comboBox.setEmptySelectionAllowed(false);
        comboBox.addStyleName(ValoTheme.COMBOBOX_SMALL);
        comboBox.setWidth(10.0f, Unit.EM);
        comboBox.setEnabled(false);
        comboBox.addValueChangeListener(event -> setValue(event.getValue()));

        checkBox = new CheckBox("Subscribe to newsletter", false);
        checkBox.setValue(comboBox.isEnabled());
        checkBox.addValueChangeListener( event -> {
            if (event.getValue()) {
                comboBox.setEnabled(true);
                if (comboBox.getValue() == null) {
                    if(comboBox.getSelectedItem().isPresent()) {
                        comboBox.setValue(comboBox.getSelectedItem().get());
                    }
                }
            } else {
                comboBox.setEnabled(true);
                setValue(null);
            }
        });

    }

    @Override
    protected void doSetValue(T newValue) {
        this.value = newValue;
        comboBox.setValue(newValue);
        checkBox.setValue(newValue != null);
    }

    public void addOption(final T item, final String caption) {
        captions.put(item, caption);
    }

    @Override
    public T getValue() {
        return value;
    }
}