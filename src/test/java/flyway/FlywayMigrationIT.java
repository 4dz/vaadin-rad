package flyway;


import org.flywaydb.core.Flyway;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;


public class FlywayMigrationIT {

    private final String JDBC_URL = "jdbc:h2:mem:flyway;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE";

    @Test
    public void shouldExecuteFlywayMigrations() {
        Flyway flyway = new Flyway();
        flyway.setDataSource(JDBC_URL, "sa", "");
        int result = flyway.migrate();
        assertThat(result, greaterThan(0));
    }
}
