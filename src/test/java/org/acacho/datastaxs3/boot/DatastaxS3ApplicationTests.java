package org.acacho.datastaxs3.boot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = {DatastaxS3Application.class},
    webEnvironment = WebEnvironment.RANDOM_PORT)
class DatastaxS3ApplicationTests {

  @Test
  void contextLoads() {
  }
}
