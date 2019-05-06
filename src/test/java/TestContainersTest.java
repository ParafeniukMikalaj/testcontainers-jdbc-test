import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class TestContainersTest {

    @Container
    public MariaDBContainer mariaDB = new MariaDBContainer();

    @Test
    public void whitespacesRemovedFromInitScriptWithCStyleComment() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUsername(mariaDB.getUsername());
        dataSource.setPassword(mariaDB.getPassword());
        dataSource.setUrl("jdbc:tc:mariadb://localhost" + mariaDB.getFirstMappedPort() + "/test?TC_INITSCRIPT=init.sql" );
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        String result = jdbcTemplate.queryForObject("SELECT text FROM test LIMIT 1", String.class);

        Assertions.assertEquals("a     b", result, "Whitespace should be preserved");
    }

}
