package dk.medcom.vdx.organisation;

import org.springframework.boot.SpringApplication;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.Network;

public class TestApplication {
    public static void main(String[] args) {
        Network n = Network.newNetwork();
        var mysql = (MySQLContainer) new MySQLContainer("mysql:5.7")
                .withDatabaseName("orgdb")
                .withUsername("orguser")
                .withPassword("secret1234")
                .withNetwork(n);

        mysql.start();
        String jdbcUrl = mysql.getJdbcUrl();
        System.setProperty("jdbc.url", jdbcUrl);
        System.setProperty("usercontext.header.name", "e");
        System.setProperty("userattributes.role.key", "d");
        System.setProperty("userrole.admin.values", "c");
        System.setProperty("userrole.user.values", "b");
        System.setProperty("userattributes.org.key", "a");

        SpringApplication.run(Application.class, args);
    }
}
