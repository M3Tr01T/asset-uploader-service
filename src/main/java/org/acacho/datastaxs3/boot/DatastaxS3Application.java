package org.acacho.datastaxs3.boot;

import org.acacho.datastaxs3.adapter.out.SpringMongoAssetRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"org.acacho.datastaxs3"})
@EnableMongoRepositories(basePackageClasses = SpringMongoAssetRepository.class)
public class DatastaxS3Application {

  public static void main(String[] args) {
    SpringApplication.run(DatastaxS3Application.class, args);
  }
}
