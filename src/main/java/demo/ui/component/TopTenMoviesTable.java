package demo.ui.component;

import com.vaadin.data.ValueProvider;
import com.vaadin.ui.Grid;
import com.vaadin.ui.renderers.NumberRenderer;
import com.vaadin.ui.themes.ValoTheme;
import demo.domain.MovieRevenue;
import demo.ui.DashboardUI;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TopTenMoviesTable extends Grid<MovieRevenue> {

    public TopTenMoviesTable(List<MovieRevenue> movieRevenues) {
        setCaption("Top 10 Titles by Revenue");

        addStyleName(ValoTheme.TABLE_BORDERLESS);
        addStyleName(ValoTheme.TABLE_NO_STRIPES);
        addStyleName(ValoTheme.TABLE_NO_VERTICAL_LINES);
        addStyleName(ValoTheme.TABLE_SMALL);
        removeHeaderRow(0);

        //setRowHeaderMode(RowHeaderMode.INDEX);
        //setColumnHeaderMode(ColumnHeaderMode.HIDDEN);
        setSizeFull();

        setItems(movieRevenues.subList(0, Math.min(10,movieRevenues.size())));
        addColumn(new ValueProvider<MovieRevenue, Integer>() {
            int index = 0;
            @Override
            public Integer apply(MovieRevenue movieRevenue) {
                return index++;
            }
        }).setSortable(false).setWidth(30);

        addColumn(MovieRevenue::getTitle).setCaption("Title").setSortable(false).setExpandRatio(2);
        addColumn(MovieRevenue::getRevenue)
                .setCaption("Revenue")
                .setSortable(false)
                .setExpandRatio(1)
                .setStyleGenerator(item -> "v-align-right").setRenderer(new NumberRenderer("$#.##"));

//        setSortContainerPropertyId("revenue");
//        setSortAscending(false);
    }
}
