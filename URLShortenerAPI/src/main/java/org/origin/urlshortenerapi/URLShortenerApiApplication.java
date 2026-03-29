package org.origin.urlshortenerapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class URLShortenerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(URLShortenerApiApplication.class, args);
    }

}
