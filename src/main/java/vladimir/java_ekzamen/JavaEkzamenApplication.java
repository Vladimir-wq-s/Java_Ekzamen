package vladimir.java_ekzamen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JavaEkzamenApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaEkzamenApplication.class, args);
    }
}