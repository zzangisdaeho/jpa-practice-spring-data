package study.datajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//@EnableJpaAuditing(modifyOnCreate = false)
@EnableJpaAuditing
@SpringBootApplication
public class Practice2DataJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(Practice2DataJpaApplication.class, args);
    }

}
