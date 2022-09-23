package dk.medcom.vdx.organisation;

import org.springframework.boot.SpringApplication;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.containers.Network;

public class TestApplication {
    public static void main(String[] args) {
        Network n = Network.newNetwork();
        var mariaDb = new MariaDBContainer<>("mariadb:10.6")
                .withDatabaseName("orgdb")
                .withUsername("orguser")
                .withPassword("secret1234")
                .withNetwork(n);

        mariaDb.start();
        String jdbcUrl = mariaDb.getJdbcUrl();
        System.setProperty("jdbc.url", jdbcUrl);

        SpringApplication.run(Application.class, args);
    }
}
