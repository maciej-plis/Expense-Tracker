package matthias.reactive_test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
public class ReactiveTest {

    public static void main(String[] args) {
        SpringApplication.run(ReactiveTest.class, args);
    }

}

