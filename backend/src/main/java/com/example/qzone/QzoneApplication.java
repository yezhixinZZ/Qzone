package com.example.qzone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class QzoneApplication {
  public static void main(String[] args) { SpringApplication.run(QzoneApplication.class, args); }
}
