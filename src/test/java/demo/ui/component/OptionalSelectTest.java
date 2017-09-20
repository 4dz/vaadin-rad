package demo.ui.component;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class OptionalSelectTest {

    @Test
    public void shouldBeUncheckedInitially() {
        OptionalSelect<Long> optionalSelect = new OptionalSelect<>();
        assertThat(optionalSelect.checkBox.getValue(), equalTo(false));
        assertThat(optionalSelect.comboBox.isEnabled(), equalTo(false));
    }

    @Test
    public void shouldEnableComboBox_WhenCheckBoxIsChecked() {
        OptionalSelect<Long> optionalSelect = new OptionalSelect<>();
        optionalSelect.checkBox.setValue(true);
        assertThat(optionalSelect.comboBox.isEnabled(), equalTo(true));
        assertThat(optionalSelect.comboBox.getSelectedItem().isPresent(), equalTo(false));
    }

    @Test
    public void shouldAddOptionAndCaptionToComboBox() {
        OptionalSelect<Long> optionalSelect = new OptionalSelect<>();
        optionalSelect.addOption(907L, "Sample Caption");
        optionalSelect.initContent();

        optionalSelect.checkBox.setValue(true);
        optionalSelect.doSetValue(907L);
        assertThat(optionalSelect.comboBox.getSelectedItem().isPresent(), equalTo(true));
        assertThat(optionalSelect.comboBox.getItemCaptionGenerator().apply(optionalSelect.comboBox.getSelectedItem().get()), equalTo("Sample Caption"));
    }

    @Test
    public void shouldReturnNullValue_WhenOptionalSelectValueRequested_AndCheckBoxNotChecked() {
        OptionalSelect<Long> optionalSelect = new OptionalSelect<>();
        optionalSelect.addOption(907L, "Sample Caption");
        optionalSelect.initContent();

        assertThat(optionalSelect.getValue(), nullValue());
    }

    @Test
    public void shouldReturnNullValue_WhenOptionalSelectValueRequested_AndCheckBoxChecked_AndNothingSelected() {
        OptionalSelect<Long> optionalSelect = new OptionalSelect<>();
        optionalSelect.addOption(907L, "Sample Caption");
        optionalSelect.initContent();

        optionalSelect.checkBox.setValue(true);

        assertThat(optionalSelect.getValue(), nullValue());
    }

    @Test
    public void shouldReturnComboValue_WhenOptionalSelectValueRequested_AndCheckBoxChecked_AndOptionSelected() {
        OptionalSelect<Long> optionalSelect = new OptionalSelect<>();
        optionalSelect.addOption(907L, "Sample Caption");
        optionalSelect.initContent();

        optionalSelect.checkBox.setValue(true);
        optionalSelect.doSetValue(907L);

        assertThat(optionalSelect.getValue(), equalTo(907L));
    }
}