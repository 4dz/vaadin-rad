package demo.ui.component;

import com.vaadin.ui.Grid;
import com.vaadin.ui.renderers.NumberRenderer;
import com.vaadin.ui.themes.ValoTheme;
import demo.domain.MovieRevenue;

import java.text.DecimalFormat;
import java.util.List;

public class TopTenMoviesTable extends Grid<MovieRevenue> {

    public TopTenMoviesTable(List<MovieRevenue> movieRevenues) {
        setCaption("Top 10 Titles by Revenue");

        addStyleNames(
                ValoTheme.TABLE_BORDERLESS,
                ValoTheme.TABLE_NO_STRIPES,
                ValoTheme.TABLE_NO_VERTICAL_LINES,
                ValoTheme.TABLE_SMALL);

        removeHeaderRow(0);
        setSizeFull();
        setItems(movieRevenues.subList(0, Math.min(10,movieRevenues.size())));

        addColumn(movieRevenue -> movieRevenues.indexOf(movieRevenue) + 1).setSortable(false).setExpandRatio(0);

        addColumn(m -> m.getMovie().getTitle()).setCaption("Title").setSortable(false).setExpandRatio(2);

        addColumn(MovieRevenue::getRevenue)
                .setCaption("Revenue")
                .setSortable(false)
                .setExpandRatio(1)
                .setStyleGenerator(item -> "v-align-right")
                .setRenderer(new NumberRenderer(new DecimalFormat("$#.##")));

    }
}
