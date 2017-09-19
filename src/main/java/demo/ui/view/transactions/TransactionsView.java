package demo.ui.view.transactions;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.renderers.NumberRenderer;
import com.vaadin.ui.themes.ValoTheme;
import demo.domain.Transaction;
import demo.ui.DashboardUI;
import demo.ui.event.DashboardEvent.BrowserResizeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import javax.annotation.PostConstruct;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@SpringView
@SuppressWarnings("serial")
public final class TransactionsView extends VerticalLayout implements View {

    @Autowired
    private EventBus.ViewEventBus dashboardEventBus;

    private final Grid<Transaction> grid;
    private SingleSelect<Transaction> singleSelect;

    private String filterValue = "";
    private static final DateFormat DATEFORMAT = new SimpleDateFormat(
            "MM/dd/yyyy hh:mm:ss a");
    private static final DecimalFormat DECIMALFORMAT = new DecimalFormat(
            "#.##");
    private static final Set<Column<Transaction, ?>> collapsibleColumns = new LinkedHashSet<>();

    public TransactionsView() {
        setSizeFull();
        addStyleName("transactions");
        setMargin(false);
        setSpacing(false);

        addComponent(buildToolbar());

        grid = buildGrid();
        singleSelect = grid.asSingleSelect();
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
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        Responsive.makeResponsive(header);

        Label title = new Label("Latest Transactions");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(title);


        HorizontalLayout tools = new HorizontalLayout(buildFilter());
        tools.addStyleName("toolbar");
        header.addComponent(tools);

        return header;
    }

    private Component buildFilter() {
        final TextField filter = new TextField();

        // TODO use new filtering API
        filter.addValueChangeListener(event -> {

            Collection<Transaction> transactions = DashboardUI.getDataProvider()
                    .getRecentTransactions(200).stream().filter(transaction -> {
                        filterValue = filter.getValue().trim().toLowerCase();
                        return passesFilter(transaction.getCountry())
                                || passesFilter(transaction.getTitle())
                                || passesFilter(transaction.getCity());
                    }).collect(Collectors.toList());

            ListDataProvider<Transaction> dataProvider = com.vaadin.data.provider.DataProvider
                    .ofCollection(transactions);
            dataProvider.addSortComparator(Comparator
                    .comparing(Transaction::getTime).reversed()::compare);
            grid.setDataProvider(dataProvider);
        });

        filter.setPlaceholder("Filter");
        filter.setIcon(FontAwesome.SEARCH);
        filter.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        filter.addShortcutListener(
                new ShortcutListener("Clear", KeyCode.ESCAPE, null) {
                    @Override
                    public void handleAction(final Object sender,
                                             final Object target) {
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
                transaction -> DATEFORMAT.format(transaction.getTime()));
        time.setId("Time").setHidable(true);

        collapsibleColumns
                .add(grid.addColumn(Transaction::getCountry).setId("Country"));
        collapsibleColumns
                .add(grid.addColumn(Transaction::getCity).setId("City"));
        collapsibleColumns
                .add(grid.addColumn(Transaction::getTheater).setId("Theater"));
        collapsibleColumns
                .add(grid.addColumn(Transaction::getRoom).setId("Room"));
        collapsibleColumns
                .add(grid.addColumn(Transaction::getRoom).setId("Title"));
        collapsibleColumns
                .add(grid.addColumn(Transaction::getSeats, new NumberRenderer())
                        .setId("Seats"));
        grid.addColumn(transaction -> "$"
                + DECIMALFORMAT.format(transaction.getPrice())).setId("Price")
                .setHidable(true);

        grid.setColumnReorderingAllowed(true);

        ListDataProvider<Transaction> dataProvider = com.vaadin.data.provider.DataProvider
                .ofCollection(DashboardUI.getDataProvider()
                        .getRecentTransactions(200));
        dataProvider.addSortComparator(
                Comparator.comparing(Transaction::getTime).reversed()::compare);
        grid.setDataProvider(dataProvider);

        // TODO either add these to grid or do it with style generators here
        // grid.setColumnAlignment("seats", Align.RIGHT);
        // grid.setColumnAlignment("price", Align.RIGHT);

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