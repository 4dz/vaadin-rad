package demo.ui.view.transactions;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.icons.VaadinIcons;
import static com.vaadin.data.provider.DataProvider.ofCollection;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.components.grid.FooterRow;
import com.vaadin.ui.renderers.NumberRenderer;
import com.vaadin.ui.themes.ValoTheme;
import demo.data.DataProvider;
import demo.domain.Transaction;

import demo.ui.DashboardUI;
import demo.ui.event.DashboardEvent.BrowserResizeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import javax.annotation.PostConstruct;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Secured("ROLE_ADMIN")
@SpringView
@SuppressWarnings("serial")
public class TransactionsView extends VerticalLayout implements View {


    private final EventBus.ViewEventBus dashboardEventBus;
    private final DataProvider dataProvider;

    private final Grid<Transaction> grid;

    private String filterValue = "";
    private static final DateFormat DATEFORMAT = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
    private static final DecimalFormat DECIMALFORMAT = new DecimalFormat("#.##");
    private static final Set<Column<Transaction, ?>> collapsibleColumns = new LinkedHashSet<>();

    @Autowired
    public TransactionsView(EventBus.ViewEventBus dashboardEventBus, DataProvider dataProvider) {
        this.dashboardEventBus = dashboardEventBus;
        this.dataProvider = dataProvider;

        setSizeFull();
        addStyleName("transactions");
        setMargin(false);
        setSpacing(false);

        addComponent(buildToolbar());

        grid = buildGrid();
        //SingleSelect<Transaction> singleSelect = grid.asSingleSelect();
        addComponent(grid);
        setExpandRatio(grid, 1);
    }

    @PostConstruct
    public void init() {
        dashboardEventBus.subscribe(this);
    }

    @Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
        dashboardEventBus.unsubscribe(this);
    }

    private Component buildToolbar() {
        MLabel title = new MLabel("Latest Transactions").withUndefinedSize()
                .withStyleName(ValoTheme.LABEL_H1,ValoTheme.LABEL_NO_MARGIN);

        MHorizontalLayout header = new MHorizontalLayout(title,
                new MHorizontalLayout(buildFilter()).withStyleName("toolbar")).withStyleName("viewheader");
        Responsive.makeResponsive(header);

        return header;
    }

    private Component buildFilter() {
        final MTextField filter = new MTextField()
                .withPlaceholder("Filter").withIcon(VaadinIcons.SEARCH).withStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        filter.addValueChangeListener(event -> {

            Collection<Transaction> transactions = dataProvider
                    .getRecentTransactions(200).stream().filter(transaction -> {
                        filterValue = filter.getValue().trim().toLowerCase();
                        return passesFilter(transaction.getRoom().getTheater().getCountry())
                                || passesFilter(transaction.getMovie().getTitle())
                                || passesFilter(transaction.getRoom().getTheater().getCity());
                    }).collect(Collectors.toList());

            ListDataProvider<Transaction> dataProvider = ofCollection(transactions);
            dataProvider.addSortComparator(Comparator.comparing(Transaction::getTime).reversed()::compare);
            grid.setDataProvider(dataProvider);
        });

        filter.addShortcutListener(
                new ShortcutListener("Clear", KeyCode.ESCAPE, null) {
                    @Override
                    public void handleAction(final Object sender, final Object target) {
                        filter.setValue("");
                    }
                });
        return filter;
    }

    private Grid<Transaction> buildGrid() {
        final Grid<Transaction> grid = new Grid<>();
        grid.setSelectionMode(SelectionMode.SINGLE);
        grid.setSizeFull();

        Column<Transaction, String> time = grid.addColumn(
                transaction -> DATEFORMAT.format(transaction.getTime())).setId("Time").setHidable(true).setCaption("Time");

        collapsibleColumns
                .add(grid.addColumn(transaction -> transaction.getRoom().getTheater().getCountry()).setId("Country").setCaption("Country"));
        collapsibleColumns
                .add(grid.addColumn(transaction -> transaction.getRoom().getTheater().getCity()).setId("City").setCaption("City"));
        collapsibleColumns
                .add(grid.addColumn(transaction -> transaction.getRoom().getTheater().getName()).setId("Theater").setCaption("Theater"));
        collapsibleColumns
                .add(grid.addColumn(transaction -> transaction.getRoom().getRoomName()).setId("Room").setCaption("Room"));
        collapsibleColumns
                .add(grid.addColumn(transaction -> transaction.getMovie().getTitle()).setId("Title").setCaption("Title"));
        collapsibleColumns
                .add(grid.addColumn(Transaction::getSeats, new NumberRenderer()).setId("Seats").setCaption("Seats").setStyleGenerator(item -> "v-align-right"));
        grid.addColumn(transaction -> "$"
                + DECIMALFORMAT.format(transaction.getPrice())).setId("Price").setCaption("Price")
                .setHidable(true).setStyleGenerator(item -> "v-align-right");

        grid.setColumnReorderingAllowed(true);
        List<Transaction> transactions = this.dataProvider.getRecentTransactions(200);
        ListDataProvider<Transaction> dataProvider = ofCollection(transactions);
        dataProvider.addSortComparator(Comparator.comparing(Transaction::getTime).reversed()::compare);
        grid.setDataProvider(dataProvider);

        FooterRow footer = grid.appendFooterRow();
        footer.getCell("Time").setText("Total");
        footer.getCell("Price").setText(DECIMALFORMAT.format(transactions.stream().mapToDouble(Transaction::getPrice).sum()));

        // TODO add when footers implemented in v8
        // grid.setFooterVisible(true);
        // grid.setColumnFooter("time", "Total");
        // grid.setColumnFooter("price", "$" + DECIMALFORMAT
        // .format(DashboardUI.getDataProvider().getTotalSum()));

        // TODO add this functionality to grid?
        // grid.addActionHandler(new TransactionsActionHandler());

        return grid;
    }

    private boolean defaultColumnsVisible() {
        boolean result = true;
        for (Column<Transaction, ?> column : collapsibleColumns) {
            if (column.isHidden() == Page.getCurrent()
                    .getBrowserWindowWidth() < 800) {
                result = false;
            }
        }
        return result;
    }

    @EventBusListenerMethod
    public void browserResized(final BrowserResizeEvent event) {
        // Some columns are collapsed when browser window width gets small
        // enough to make the table fit better.

        if (defaultColumnsVisible()) {
            for (Column<Transaction, ?> column : collapsibleColumns) {
                column.setHidden(
                        Page.getCurrent().getBrowserWindowWidth() < 800);
            }
        }
    }

    private boolean passesFilter(String subject) {
        if (subject == null) {
            return false;
        }
        return subject.trim().toLowerCase().contains(filterValue);
    }

    @Override
    public void enter(final ViewChangeEvent event) {
    }
}