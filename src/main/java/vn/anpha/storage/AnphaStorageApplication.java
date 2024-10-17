package vn.anpha.storage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class)
public class AnphaStorageApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnphaStorageApplication.class, args);
    }
}
